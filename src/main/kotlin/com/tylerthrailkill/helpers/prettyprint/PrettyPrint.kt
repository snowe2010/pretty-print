package com.tylerthrailkill.helpers.prettyprint

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
private const val TAB_SIZE = 2

fun pp(obj: Any?) {
    recurse(obj)
}

fun recurse(obj: Any?, numberOfTabs: Int = 0) {
    val padString = " ".repeat(numberOfTabs * TAB_SIZE)

    val className = "${obj?.javaClass?.simpleName}("
    writeLine(className)

    obj?.javaClass?.declaredFields?.forEach {
        it.isAccessible = true
        val padString = " ".repeat((numberOfTabs + 1) * TAB_SIZE) // shadow padString to nest properly
        write("$padString${it.name} = ")
        val fieldValue = it.get(obj)
        when {
            fieldValue is Iterable<*> -> recurseIterable(fieldValue, it.name.length + 3, numberOfTabs)
            fieldValue == null -> writeLine("null")
            fieldValue.javaClass.name.startsWith("java") -> writeLine(fieldValue.toString())
            else -> recurse(fieldValue, numberOfTabs + 1)
        }
    }
    writeLine("$padString)")
}

fun recurseIterable(obj: Iterable<*>, initialDepth: Int, numberOfTabs: Int) {
    val padDepth = (numberOfTabs + 1) * TAB_SIZE
    val iterableDepth = initialDepth + padDepth
    val iterableString = " ".repeat(iterableDepth)

    writeLine("[")
    obj.forEach {
        write(iterableString + " ".repeat(TAB_SIZE))
        if (it is String) {
            write('"')
        }
        write(it)
        if (it is String) {
            write('"')
        }
        println()
    }
    writeLine("$iterableString]")
}

fun writeLine(str: Any?) {
    logger.debug { "writing $str" }
    println(str)
}

fun write(str: Any?) {
    logger.debug { "writing $str" }
    print(str)
}