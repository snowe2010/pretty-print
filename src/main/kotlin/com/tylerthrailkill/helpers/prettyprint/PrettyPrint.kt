package com.tylerthrailkill.helpers.prettyprint

import mu.KotlinLogging
import java.io.PrintStream

private val logger = KotlinLogging.logger {}
private var TAB_SIZE = 2
private var PRINT_STREAM = System.out

fun pp(obj: Any?, tabSize: Int = 2, printStream: PrintStream = System.out) {
    TAB_SIZE = tabSize
    PRINT_STREAM = printStream
    when (obj) {
        is Iterable<*> -> recurseIterable(obj, "")
        is Map<*, *> -> recurseMap(obj, "")
        else -> recurse(obj)
    }
}

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

private fun writeLine(str: Any?) {
    logger.debug { "writing $str" }
    PRINT_STREAM.println(str)
}

private fun write(str: Any?) {
    logger.debug { "writing $str" }
    PRINT_STREAM.print(str)
}

private fun deepen(currentDepth: String, modifier: Int? = null): String = " ".repeat(modifier ?: TAB_SIZE) + currentDepth