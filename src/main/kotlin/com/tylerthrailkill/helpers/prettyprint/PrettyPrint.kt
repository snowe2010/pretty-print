package com.tylerthrailkill.helpers.prettyprint

import mu.KotlinLogging
import java.io.PrintStream

private val logger = KotlinLogging.logger {}
private var indentSize = 2
private var appendable: Appendable = System.out
// TODO fix docs
/**
 * Pretty print function.
 *
 * Prints any object in a pretty format for easy debugging/reading
 *
 * @param[obj] the object to pretty print
 * @param[indent] optional param that specifies the number of spaces to use to indent
 * @param[appendable] optional param that specifies the [PrintStream] to use to pretty print. Defaults to `System.out`
 */
public fun pp(obj: Any?, indent: Int = 2, writeTo: Appendable = System.out) {
    indentSize = indent
    appendable = writeTo
    ppAny(obj)
}

private fun ppAny(
    obj: Any?,
    collectionPad: String = "",
    objectPad: String = collectionPad,
    shouldQuoteStrings: Boolean = true
) = when {
    obj is Iterable<*> -> ppIterable(obj, collectionPad)
    obj is Map<*, *> -> ppMap(obj, collectionPad)
    obj == null -> write("null")
    obj.javaClass.name.startsWith("java") -> {
        val fence = if (obj is String && shouldQuoteStrings) "\"" else ""
        write("$fence$obj$fence")
    }
    else -> ppPlainObject(obj, objectPad)
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
private fun ppPlainObject(obj: Any?, currentDepth: String) {
    val className = "${obj?.javaClass?.simpleName}("
    write(className)

    obj?.javaClass?.declaredFields?.forEach {
        val increasedDepth = deepen(currentDepth)
        val extraIncreasedDepth = deepen(increasedDepth, it.name.length + 3)
        it.isAccessible = true
        appendable.appendln()
        write("$increasedDepth${it.name} = ")
        val fieldValue = it.get(obj)
        logger.debug { "field value is ${fieldValue.javaClass}" }
        ppAny(fieldValue, extraIncreasedDepth, increasedDepth, false)
    }
    appendable.appendln()
    write("$currentDepth)")
}

/**
 * Same as `recurse`, but meant for iterables. Handles deepening in appropriate areas
 * and calling back to `recurse`, `recurseIterable`, or `recurseMap`
 */
private fun ppIterable(obj: Iterable<*>, currentDepth: String) {
    var commas = obj.count() // comma counter

    // begin writing the iterable
    writeLine("[")
    obj.forEach {
        val increasedDepth = deepen(currentDepth)
        write(increasedDepth) // write leading spacing
        ppAny(it, increasedDepth)
        // add commas if not the last element
        if (commas > 1) {
            write(',')
            commas--
        }
        appendable.appendln()
    }
    write("$currentDepth]")
}

/**
 * Same as `recurse`, but meant for maps. Handles deepening in appropriate areas
 * and calling back to `recurse`, `recurseIterable`, or `recurseMap`
 */
private fun ppMap(obj: Map<*, *>, currentDepth: String) {
    var commas = obj.count() // comma counter

    // begin writing the iterable
    writeLine("{")
    obj.forEach { (k, v) ->
        val increasedDepth = deepen(currentDepth)
        write(increasedDepth) // write leading spacing
        ppAny(k, increasedDepth)
        write(" -> ")
        ppAny(v, increasedDepth)
        // add commas if not the last element
        if (commas > 1) {
            write(',')
            commas--
        }
        appendable.appendln()
    }

    write("$currentDepth}")
}

/**
 * Helper functions. Replaces `println()` and adds logging
 */
private fun writeLine(str: Any?) {
    logger.debug { "writing $str" }
    appendable.append(str.toString()).appendln()
}

/**
 * Helper functions. Replaces `print()` and adds logging
 */
private fun write(str: Any?) {
    logger.debug { "writing $str" }
    appendable.append(str.toString())
}

/**
 * Helper function that generates a deeper string based on the current depth, tab size, and any modifiers
 * such as if we are currently iterating inside of a list or map
 */
private fun deepen(currentDepth: String, modifier: Int = indentSize): String =
    " ".repeat(modifier) + currentDepth