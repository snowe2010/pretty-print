package com.tylerthrailkill.helpers.prettyprint

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
private var indentSize = 2
private var appendable: Appendable = System.out

/**
 * Pretty print function.
 *
 * Prints any object in a pretty format for easy debugging/reading
 *
 * @param [obj] the object to pretty print
 * @param [indent] optional param that specifies the number of spaces to use to indent. Defaults to 2.
 * @param [writeTo] optional param that specifies the [Appendable] to output the pretty print to. Defaults appending to `System.out`.
 */
public fun pp(obj: Any?, indent: Int = indentSize, writeTo: Appendable = appendable) {
    indentSize = indent
    appendable = writeTo
    ppAny(obj)
}

/**
 * Pretty prints any object. `collectionItemPad` is how much more to indent the contents of a collection.
 * `objectFieldPad` is how much to indent the fields of an object. `shouldQuoteStrings` is whether or not a String
 * object should be quoted.
 */
private fun ppAny(
    obj: Any?,
    collectionItemPad: String = "",
    objectFieldPad: String = collectionItemPad,
    shouldQuoteStrings: Boolean = true
) = when {
    obj is Iterable<*> -> ppIterable(obj, collectionItemPad)
    obj is Map<*, *> -> ppMap(obj, collectionItemPad)
    obj == null -> write("null")
    obj.javaClass.name.startsWith("java") -> {
        val fence = if (obj is String && shouldQuoteStrings) "\"" else ""
        write("$fence$obj$fence")
    }
    else -> ppPlainObject(obj, objectFieldPad)
}

/**
 * Pretty print a plain object.
 */
private fun ppPlainObject(obj: Any?, currentDepth: String) {
    val className = "${obj?.javaClass?.simpleName}("
    val increasedDepth = deepen(currentDepth)

    writeLine(className)
    obj?.javaClass?.declaredFields?.forEach {
        val extraIncreasedDepth = deepen(increasedDepth, it.name.length + 3)
        it.isAccessible = true
        write("$increasedDepth${it.name} = ")
        ppAny(it.get(obj), extraIncreasedDepth, increasedDepth, false)
        writeLine()
    }
    write(currentDepth)
    write(')')
}

/**
 * Pretty print an Iterable.
 */
private fun ppIterable(obj: Iterable<*>, currentDepth: String) {
    var commas = obj.count() // comma counter
    val increasedDepth = deepen(currentDepth)

    writeLine('[')
    obj.forEach {
        write(increasedDepth)
        ppAny(it, increasedDepth)

        if (commas > 1) {
            write(',')
            commas--
        }
        writeLine()
    }
    write(currentDepth)
    write(']')
}

/**
 * Pretty print a Map.
 */
private fun ppMap(obj: Map<*, *>, currentDepth: String) {
    var commas = obj.count() // comma counter
    val increasedDepth = deepen(currentDepth)

    writeLine('{')
    obj.forEach { (k, v) ->
        write(increasedDepth)
        ppAny(k, increasedDepth)
        write(" -> ")
        ppAny(v, increasedDepth)

        if (commas > 1) {
            write(',')
            commas--
        }
        writeLine()
    }
    write(currentDepth)
    write('}')
}

/**
 * Writes to the appendable with a new line and adds logging
 */
private fun writeLine(str: Any? = "") {
    logger.debug { "writing $str" }
    appendable.append(str.toString()).appendln()
}

/**
 * Writes to the appendable and adds logging
 */
private fun write(str: Any?) {
    logger.debug { "writing $str" }
    appendable.append(str.toString())
}

/**
 * Generates a deeper string based on the current depth and tab size
 */
private fun deepen(currentDepth: String, size: Int = indentSize): String =
    " ".repeat(size) + currentDepth