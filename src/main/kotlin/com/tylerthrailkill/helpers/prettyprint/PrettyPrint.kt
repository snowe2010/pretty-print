package com.tylerthrailkill.helpers.prettyprint

import mu.KotlinLogging
import java.io.PrintStream

private val logger = KotlinLogging.logger {}
private var TAB_SIZE = 2
private var PRINT_STREAM = System.out

/**
 * Pretty print function.
 *
 * Prints any object in a pretty format for easy debugging/reading
 *
 * @param[obj] the object to pretty print
 * @param[tabSize] optional param that specifies the number of spaces to use to indent
 * @param[printStream] optional param that specifies the [PrintStream] to use to pretty print. Defaults to `System.out`
 */
fun pp(obj: Any?, tabSize: Int = 2, printStream: PrintStream = System.out) {
    TAB_SIZE = tabSize
    PRINT_STREAM = printStream
    when (obj) {
        is Iterable<*> -> recurseIterable(obj, "")
        is Map<*, *> -> recurseMap(obj, "")
        else -> recurse(obj)
    }
}

/**
 * Recurse over plain objects
 * If null, print null and end recursion
 * If String, print "string here" and end recursion
 * If java.* then write using toString() and end recursion
 * If Iterable then recurse and deepen the tab size
 * If Map then recurse and deepen the tab size
 * else recurse back into this function
 */
private fun recurse(obj: Any?, currentDepth: String = "") {
    val className = "${obj?.javaClass?.simpleName}("
    write(className)

    obj?.javaClass?.declaredFields?.forEach {
        val pad = deepen(currentDepth)
        it.isAccessible = true
        PRINT_STREAM.println()
        write("$pad${it.name} = ")
        val fieldValue = it.get(obj)
        logger.debug { "field value is ${fieldValue.javaClass}"}
        when {
            fieldValue is Iterable<*> -> recurseIterable(fieldValue, deepen(pad, it.name.length + 3))
            fieldValue is Map<*, *> -> recurseMap(fieldValue, deepen(pad, it.name.length + 3))
            fieldValue == null -> write("null")
            fieldValue.javaClass.name.startsWith("java") -> write(fieldValue.toString())
            else -> recurse(fieldValue, deepen(currentDepth))
        }
    }
    PRINT_STREAM.println()
    write("$currentDepth)")
}

/**
 * Same as `recurse`, but meant for iterables. Handles deepening in appropriate areas
 * and calling back to `recurse`, `recurseIterable`, or `recurseMap`
 */
private fun recurseIterable(obj: Iterable<*>, currentDepth: String) {
    var commas = obj.count() // comma counter

    // begin writing the iterable
    writeLine("[")
    obj.forEach {
        val increasedDepth = currentDepth + " ".repeat(TAB_SIZE)
        write(increasedDepth) // write leading spacing
        when {
            it is Iterable<*> -> recurseIterable(it, increasedDepth)
            it is Map<*, *> -> recurseMap(it, increasedDepth)
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
            else -> recurse(it, increasedDepth)
        }
        // add commas if not the last element
        if (commas > 1) {
            write(',')
            commas--
        }
        PRINT_STREAM.println()
    }
    write("$currentDepth]")
}

/**
 * Same as `recurse`, but meant for maps. Handles deepening in appropriate areas
 * and calling back to `recurse`, `recurseIterable`, or `recurseMap`
 */
private fun recurseMap(obj: Map<*, *>, currentDepth: String) {
    var commas = obj.count() // comma counter

    // begin writing the iterable
    writeLine("{")
    obj.forEach {(k, v) ->
        val increasedDepth = currentDepth + " ".repeat(TAB_SIZE)
        write(increasedDepth) // write leading spacing
        when {
            k is Iterable<*> -> recurseIterable(k, increasedDepth)
            k is Map<*, *> -> recurseMap(k, increasedDepth)
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
            else -> recurse(k, increasedDepth)
        }
        write(" -> ")
        when {
            v is Iterable<*> -> recurseIterable(v, increasedDepth)
            v is Map<*, *> -> recurseMap(v, increasedDepth)
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
            else -> recurse(v, increasedDepth)
        }
        // add commas if not the last element
        if (commas > 1) {
            write(',')
            commas--
        }
        PRINT_STREAM.println()
    }
    write("$currentDepth}")
}

/**
 * Helper functions. Replaces `println()` and adds logging
 */
private fun writeLine(str: Any?) {
    logger.debug { "writing $str" }
    PRINT_STREAM.println(str)
}

/**
 * Helper functions. Replaces `print()` and adds logging
 */
private fun write(str: Any?) {
    logger.debug { "writing $str" }
    PRINT_STREAM.print(str)
}

/**
 * Helper function that generates a deeper string based on the current depth, tab size, and any modifiers
 * such as if we are currently iterating inside of a list or map
 */
private fun deepen(currentDepth: String, modifier: Int? = null): String = " ".repeat(modifier ?: TAB_SIZE) + currentDepth