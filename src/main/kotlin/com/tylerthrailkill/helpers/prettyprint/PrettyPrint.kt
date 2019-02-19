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
    when (obj) {
        is Iterable<*> -> prettyPrint.ppIterable(obj, nodeList, detectedCycles, "")
        is Map<*, *> -> prettyPrint.ppMap(obj, nodeList, detectedCycles, "")
        is Any -> prettyPrint.pp(obj, nodeList, detectedCycles)
        else -> prettyPrint.writeLine("null")
    }
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
        if (nodeList.contains(currentObjectIdentity)) {
            write("cyclic reference detected for $currentObjectIdentity")
            detectedCycles.add(currentObjectIdentity)
            return
        }
        nodeList.push(currentObjectIdentity)
        val className = "${obj.javaClass.simpleName}("
        write(className)

        obj.javaClass.declaredFields.filter { !it.isSynthetic }.forEach {
            val pad = deepen(currentDepth)
            it.isAccessible = true
            printStream.println()
            write("$pad${it.name} = ")
            val fieldValue = it.get(obj)
            logger.debug { "field value is ${fieldValue.javaClass}" }
            when {
                fieldValue is Iterable<*> -> ppIterable(fieldValue, nodeList, detectedCycles, deepen(pad, it.name.length + 3))
                fieldValue is Map<*, *> -> ppMap(fieldValue, nodeList, detectedCycles, deepen(pad, it.name.length + 3))
                fieldValue == null -> write("null")
                fieldValue.javaClass.name.startsWith("java") -> {
                    when (fieldValue) {
                        is String -> {
                            val str = fieldValue.toString()
                            if (str.length > wrappedLineWidth) {
                                writeLine("\"\"\"")
                                val newPad = deepen(pad, it.name.length + 3)
                                writeLine(wordWrap(str, newPad))
                                write("$newPad\"\"\"")
                            } else {
                                write("\"")
                                write(str)
                                write("\"")
                            }
                        }
                        else -> write(fieldValue)
                    }
                }
                else -> pp(fieldValue, nodeList, detectedCycles, deepen(currentDepth))
            }
        }
        printStream.println()
        write("$currentDepth)")
        if (detectedCycles.contains(currentObjectIdentity)) {
            write("[\$id=$currentObjectIdentity]")
            detectedCycles.remove(currentObjectIdentity)
        }
        nodeList.pop()
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
        if (nodeList.contains(currentObjectIdentity)) {
            write("cyclic reference detected for $currentObjectIdentity")
            detectedCycles.add(currentObjectIdentity)
            return
        }
        nodeList.push(currentObjectIdentity)
        var commas = obj.count() // comma counter

        // begin writing the iterable
        writeLine("[")
        obj.forEach {
            val increasedDepth = currentDepth + " ".repeat(tabSize)
            write(increasedDepth) // write leading spacing
            when {
                it is Iterable<*> -> ppIterable(it, nodeList, detectedCycles, increasedDepth)
                it is Map<*, *> -> ppMap(it, nodeList, detectedCycles, increasedDepth)
                it == null -> write("null")
                it.javaClass.name.startsWith("java") -> {
                    if (it is String) {
                        write('"')
                    }
                    write(it)
                    if (it is String) {
                        write('"')
                    }
                }
                else -> pp(it, nodeList, detectedCycles, increasedDepth)
            }
            // add commas if not the last element
            if (commas > 1) {
                write(',')
                commas--
            }
            printStream.println()
        }
        write("$currentDepth]")
        if (detectedCycles.contains(currentObjectIdentity)) {
            write("[\$id=$currentObjectIdentity]")
            detectedCycles.remove(currentObjectIdentity)
        }
        nodeList.pop()
    }

    /**
     * Same as `recurse`, but meant for maps. Handles deepening in appropriate areas
     * and calling back to `recurse`, `ppIterable`, or `ppMap`
     */
    fun ppMap(
            obj: Map<*, *>,
            nodeList: CurrentNodeIdentityHashCodes,
            detectedCycles: DetectedCycles, currentDepth: String
    ) {
        val currentObjectIdentity = System.identityHashCode(obj)
        if (nodeList.contains(currentObjectIdentity)) {
            write("cyclic reference detected for $currentObjectIdentity")
            detectedCycles.add(currentObjectIdentity)
            return
        }
        nodeList.push(currentObjectIdentity)
        var commas = obj.count() // comma counter

        // begin writing the iterable
        writeLine("{")
        obj.forEach { (k, v) ->
            val increasedDepth = currentDepth + " ".repeat(tabSize)
            write(increasedDepth) // write leading spacing
            when {
                k is Iterable<*> -> ppIterable(k, nodeList, detectedCycles, increasedDepth)
                k is Map<*, *> -> ppMap(k, nodeList, detectedCycles, increasedDepth)
                k == null -> write("null")
                k.javaClass.name.startsWith("java") -> {
                    if (k is String) {
                        write('"')
                    }
                    write(k)
                    if (k is String) {
                        write('"')
                    }
                }
                else -> pp(k, nodeList, detectedCycles, increasedDepth)
            }
            write(" -> ")
            when {
                v is Iterable<*> -> ppIterable(v, nodeList, detectedCycles, increasedDepth)
                v is Map<*, *> -> ppMap(v, nodeList, detectedCycles, increasedDepth)
                v == null -> write("null")
                v.javaClass.name.startsWith("java") -> {
                    if (v is String) {
                        write('"')
                    }
                    write(v)
                    if (v is String) {
                        write('"')
                    }
                }
                else -> pp(v, nodeList, detectedCycles, increasedDepth)
            }
            // add commas if not the last element
            if (commas > 1) {
                write(',')
                commas--
            }
            printStream.println()
        }
        write("$currentDepth}")
        if (detectedCycles.contains(currentObjectIdentity)) {
            write("[\$id=$currentObjectIdentity]")
            detectedCycles.remove(currentObjectIdentity)
        }
        nodeList.pop()
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
        var sot = true
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
