package com.tylerthrailkill.helpers.prettyprint

import com.ibm.icu.text.BreakIterator
import mu.KotlinLogging
import java.io.PrintStream
import java.util.*

typealias CurrentNodeIdentityHashCodes = ArrayDeque<Int>
typealias DetectedCycles = MutableList<Int>

/**
 * Pretty print function.
 *
 * Prints any object in a pretty format for easy debugging/reading
 *
 * @param[obj] the object to pretty print
 * @param[tabSize] optional param that specifies the number of spaces to use to indent
 * @param[printStream] optional param that specifies the [PrintStream] to use to pretty print. Defaults to `System.out`
 */
fun pp(obj: Any?, tabSize: Int = 2, printStream: PrintStream = System.out, wrappedLineWidth: Int = 80) {
    val prettyPrint = PrettyPrint(tabSize, printStream, wrappedLineWidth)
    val nodeList = CurrentNodeIdentityHashCodes()
    val detectedCycles: DetectedCycles = mutableListOf()
    prettyPrint.recurse(obj, nodeList, detectedCycles, "")
    printStream.println()
}

/**
 * Inline helper method for printing withing method chains. Simply delegates to [pp]
 *
 * Example:
 *   val foo = op2(op1(bar).pp())
 *
 * @param[T] the object to pretty print
 * @param[tabSize] optional param that specifies the number of spaces to use to indent
 * @param[printStream] optional param that specifies the [PrintStream] to use to pretty print. Defaults to `System.out`
 */
fun <T> T.pp(tabSize: Int = 2, printStream: PrintStream = System.out): T =
        this.also { pp(it, tabSize, printStream) }


private class PrettyPrint(
        val tabSize: Int,
        val printStream: PrintStream,
        val wrappedLineWidth: Int) {
    private val lineInstance = BreakIterator.getLineInstance()
    private val logger = KotlinLogging.logger {}

    /**
     * PrettyPrint a plain object
     * Recurses through a plain object, retrieving fields and printing them
     *
     * If null, print null and end recursion
     * If String, print "string here" and end recursion
     * If java.* then write using toString() and end recursion
     * If Iterable then recurse and deepen the tab size
     * If Map then recurse and deepen the tab size
     * else recurse back into this function
     */
    fun pp(
            obj: Any,
            nodeList: CurrentNodeIdentityHashCodes,
            detectedCycles: DetectedCycles,
            currentDepth: String = ""
    ) {
        val currentObjectIdentity = System.identityHashCode(obj)
        checkCycledStart(currentObjectIdentity, nodeList, detectedCycles) { return }
        val className = "${obj.javaClass.simpleName}("
        write(className)

        obj.javaClass.declaredFields.filter { !it.isSynthetic }.forEach {
            val pad = deepen(currentDepth)
            it.isAccessible = true
            printStream.println()
            write("$pad${it.name} = ")
            val fieldValue = it.get(obj)
            recurse(fieldValue, nodeList, detectedCycles, pad, it.name.length + 3)
        }
        printStream.println()
        write("$currentDepth)")
        checkCycleEnd(currentObjectIdentity, nodeList, detectedCycles)
    }

    /**
     * Same as `pp`, but meant for iterables. Handles deepening in appropriate areas
     * and calling back to `pp`, `ppIterable`, or `ppMap`
     */
    fun ppIterable(
            obj: Iterable<*>,
            nodeList: CurrentNodeIdentityHashCodes,
            detectedCycles: DetectedCycles,
            currentDepth: String
    ) {
        val currentObjectIdentity = System.identityHashCode(obj)
        checkCycledStart(currentObjectIdentity, nodeList, detectedCycles) { return }
        var commas = obj.count() // comma counter

        // begin writing the iterable
        writeLine("[")
        obj.forEach {
            val increasedDepth = currentDepth + " ".repeat(tabSize)
            write(increasedDepth) // write leading spacing
            recurse(it, nodeList, detectedCycles, increasedDepth)
            // add commas if not the last element
            if (commas > 1) {
                write(',')
                commas--
            }
            printStream.println()
        }
        write("$currentDepth]")
        checkCycleEnd(currentObjectIdentity, nodeList, detectedCycles)
    }

    /**
     * Same as `recurse`, but meant for maps. Handles deepening in appropriate areas
     * and calling back to `recurse`, `ppIterable`, or `ppMap`
     */
    fun ppMap(
            obj: Map<*, *>,
            nodeList: CurrentNodeIdentityHashCodes,
            detectedCycles: DetectedCycles,
            currentDepth: String
    ) {
        val currentObjectIdentity = System.identityHashCode(obj)
        checkCycledStart(currentObjectIdentity, nodeList, detectedCycles) { return }
        var commas = obj.count() // comma counter

        // begin writing the iterable
        writeLine("{")
        obj.forEach { (k, v) ->
            val increasedDepth = currentDepth + " ".repeat(tabSize)
            write(increasedDepth) // write leading spacing
            ppKey(k, nodeList, detectedCycles, increasedDepth)
            write(" -> ")
            ppValue(v, nodeList, detectedCycles, increasedDepth)
            // add commas if not the last element
            if (commas > 1) {
                write(',')
                commas--
            }
            printStream.println()
        }
        write("$currentDepth}")
        checkCycleEnd(currentObjectIdentity, nodeList, detectedCycles)
    }

    fun recurse(value: Any?,
                nodeList: CurrentNodeIdentityHashCodes,
                detectedCycles: DetectedCycles,
                padding: String,
                extraPadding: Int? = 0) {
        logger.debug { "value is ${value?.javaClass}" }
        when {
            value is Iterable<*> -> ppIterable(value, nodeList, detectedCycles, deepen(padding, extraPadding))
            value is Map<*, *> -> ppMap(value, nodeList, detectedCycles, deepen(padding, extraPadding))
            value == null -> write("null")
            value.javaClass.name.startsWith("java") -> ppJavaClass(value, padding, extraPadding)
            else -> pp(value, nodeList, detectedCycles, padding)
        }
    }

    inline fun checkCycledStart(currentObjectIdentity: Int, nodeList: CurrentNodeIdentityHashCodes, detectedCycles: DetectedCycles, ret: () -> Unit) {
        if (nodeList.contains(currentObjectIdentity)) {
            write("cyclic reference detected for $currentObjectIdentity")
            detectedCycles.add(currentObjectIdentity)
            ret()
        }
        nodeList.push(currentObjectIdentity)
    }

    fun checkCycleEnd(currentObjectIdentity: Int, nodeList: CurrentNodeIdentityHashCodes, detectedCycles: DetectedCycles) {
        if (detectedCycles.contains(currentObjectIdentity)) {
            write("[\$id=$currentObjectIdentity]")
            detectedCycles.remove(currentObjectIdentity)
        }
        nodeList.pop()
    }

    fun ppKey(key: Any?,
              nodeList: CurrentNodeIdentityHashCodes,
              detectedCycles: DetectedCycles,
              increasedDepth: String) {
        when {
            key is Iterable<*> -> ppIterable(key, nodeList, detectedCycles, increasedDepth)
            key is Map<*, *> -> ppMap(key, nodeList, detectedCycles, increasedDepth)
            key == null -> write("null")
            key.javaClass.name.startsWith("java") -> ppJavaClass(key, increasedDepth)
            else -> pp(key, nodeList, detectedCycles, increasedDepth)
        }
    }

    fun ppValue(value: Any?,
                nodeList: CurrentNodeIdentityHashCodes,
                detectedCycles: DetectedCycles,
                increasedDepth: String) {
        when {
            value is Iterable<*> -> ppIterable(value, nodeList, detectedCycles, increasedDepth)
            value is Map<*, *> -> ppMap(value, nodeList, detectedCycles, increasedDepth)
            value == null -> write("null")
            value.javaClass.name.startsWith("java") -> ppJavaClass(value, increasedDepth)
            else -> pp(value, nodeList, detectedCycles, increasedDepth)
        }
    }

    fun ppJavaClass(fieldValue: Any, padding: String, extraPadding: Int? = null) {
        when (fieldValue) {
            is String -> ppString(fieldValue, padding, extraPadding)
            else -> write(fieldValue)
        }
    }

    fun ppString(text: Any, padding: String, extraPadding: Int? = null) {
        val str = text.toString()
        if (str.length > wrappedLineWidth) {
            writeLine("\"\"\"")
            val padding = if (extraPadding != null) {
                deepen(padding, extraPadding)
            } else {
                padding
            }
            writeLine(wordWrap(str, padding))
            write("$padding\"\"\"")
        } else {
            write("\"")
            write(str)
            write("\"")
        }
    }

    /**
     * Helper functions. Replaces `println()` and adds logging
     */
    fun writeLine(str: Any?) {
        logger.debug { "writing $str" }
        printStream.println(str)
    }

    /**
     * Helper functions. Replaces `print()` and adds logging
     */
    private fun write(str: Any?) {
        logger.debug { "writing $str" }
        printStream.print(str)
    }

    /**
     * Helper function that generates a deeper string based on the current depth, tab size, and any modifiers
     * such as if we are currently iterating inside of a list or map
     */
    private fun deepen(currentDepth: String, modifier: Int? = null): String = " ".repeat(modifier ?: tabSize) + currentDepth

    private fun wordWrap(text: String, padding: String): String {
        lineInstance.setText(text)
        var start = lineInstance.first()
        var end = lineInstance.next()
        val breakableLocations = mutableListOf<String>()
        while (end != BreakIterator.DONE) {
            val substring = text.substring(start, end)
            breakableLocations.add(substring)
            start = end
            end = lineInstance.next()
        }
        val arr = mutableListOf(mutableListOf<String>())
        var index = 0
        arr[index].add(breakableLocations[0])
        breakableLocations.drop(1).forEach lit@{
            val currentSize = arr[index].joinToString(separator = "").length
            if (currentSize + it.length <= wrappedLineWidth) {
                arr[index].add(it)
            } else {
                arr.add(mutableListOf(it))
                index += 1
            }
        }
        return arr.flatMap { listOf("$padding${it.joinToString(separator = "")}") }.joinToString("\n")
    }
}
