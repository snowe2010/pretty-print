package com.tylerthrailkill.helpers.prettyprint

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
private const val TAB_SIZE = 2

fun pp(obj: Any?) {
    when (obj) {
        is Iterable<*> -> recurseIterable(obj, "")
        is Map<*, *> -> recurseMap(obj, "")
        else -> recurse(obj)
    }
}

fun recurse(obj: Any?, currentDepth: String = "") {
    val className = "${obj?.javaClass?.simpleName}("
    write(className)

    obj?.javaClass?.declaredFields?.forEach {
        val pad = deepen(currentDepth)
        it.isAccessible = true
        println()
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
    println()
    write("$currentDepth)")
}

fun recurseIterable(obj: Iterable<*>, currentDepth: String) {
    var commas = obj.count() // comma counter

    // begin writing the iterable
    writeLine("[")
    obj.forEach {
        val increasedDepth = currentDepth + " ".repeat(TAB_SIZE)
        write(increasedDepth) // write leading spacing
        when {
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
        println()
    }
    write("$currentDepth]")
}

fun recurseMap(obj: Map<*, *>, currentDepth: String) {
    var commas = obj.count() // comma counter

    // begin writing the iterable
    writeLine("{")
    obj.forEach {(k, v) ->
        val increasedDepth = currentDepth + " ".repeat(TAB_SIZE)
        write(increasedDepth) // write leading spacing
        when {
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
        println()
    }
    write("$currentDepth}")
}

fun writeLine(str: Any?) {
    logger.debug { "writing $str" }
    println(str)
}

fun write(str: Any?) {
    logger.debug { "writing $str" }
    print(str)
}

fun deepen(currentDepth: String, modifier: Int? = null): String = " ".repeat(modifier ?: TAB_SIZE) + currentDepth