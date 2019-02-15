package com.tylerthrailkill.helpers.prettyprint

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
private val WRAPPED_LINE_WIDTH = 80
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
    ppAny(obj, mutableSetOf(), mutableSetOf())
    writeLine()
}

/**
 * Inline helper method for printing withing method chains. Simply delegates to [pp]
 *
 * Example:
 *   val foo = op2(op1(bar).pp())
 *
 * @param[T] the object to pretty print
 * @param[indent] optional param that specifies the number of spaces to use to indent. Defaults to 2.
 * @param[writeTo] optional param that specifies the [Appendable] to output the pretty print to. Defaults appending to `System.out`
 */
public fun <T> T.pp(indent: Int = indentSize, writeTo: Appendable = appendable): T =
    this.also { pp(it, indent, writeTo) }

/**
 * Pretty prints any object. `collectionItemPad` is how much more to indent the contents of a collection.
 * `objectFieldPad` is how much to indent the fields of an object.
 */
private fun ppAny(
    obj: Any?,
    visited: MutableSet<Int>,
    revisited: MutableSet<Int>,
    collectionItemPad: String = "",
    objectFieldPad: String = collectionItemPad
) {
    val id = System.identityHashCode(obj)

    if (!isAtomic(obj) && visited.contains(id)) {
        write("cyclic reference detected for $id")
        revisited.add(id)
        return
    }

    if (!isAtomic(obj)) visited.add(id)

    when (obj) {
        is Iterable<*> -> ppIterable(obj, visited, revisited, collectionItemPad)
        is Map<*, *> -> ppMap(obj, visited, revisited, collectionItemPad)
        is Any -> ppPlainObject(obj, visited, revisited, objectFieldPad)
        else -> write("null")
    }

    visited.remove(id)
}

private fun isAtomic(o: Any?): Boolean =
    o == null
            || o is Char
            || o is Byte
            || o is Short
            || o is Int
            || o is Long
            || o is Float
            || o is Double
            || o is Boolean
            || o is String

/**
 * Pretty prints the contents of the Iterable receiver. The given function is applied to each element. The result
 * of an application to each element is on its own line, separated by a separator. `currentDepth` specifies the
 * indentation level of any closing bracket.
 */
private fun <T> Iterable<T>.ppContents(currentDepth: String, separator: String = "", f: (T) -> Unit) {
    val list = this.toMutableList()
    if (!list.isEmpty()) {
        f(list.removeAt(0))
        list.forEach {
            writeLine(separator)
            f(it)
        }
        writeLine()
    }

    write(currentDepth)
}

/**
 * Pretty print a plain object.
 */
private fun ppPlainObject(obj: Any, visited: MutableSet<Int>, revisited: MutableSet<Int>, currentDepth: String) {
    if (obj.javaClass.name.startsWith("java")) {
        val fence = if (obj is String) "\"" else ""
        write("$fence$obj$fence")
    } else {
        val increasedDepth = deepen(currentDepth)
        val className = obj.javaClass.simpleName

        write(className)
        writeLine('(')
        obj.javaClass.declaredFields
            .filterNot { it.isSynthetic }
            .toList()
            .ppContents(currentDepth) {
                it.isAccessible = true
                write("$increasedDepth${it.name} = ")
                val extraIncreasedDepth = deepen(increasedDepth, it.name.length + 3)
                val fieldValue = it.get(obj)
                logger.debug { "field value is ${fieldValue.javaClass}" }
                ppAny(fieldValue, visited, revisited, extraIncreasedDepth, increasedDepth)
            }
        write(')')
        val id = System.identityHashCode(obj)
        if (revisited.contains(id)) write("[\$id=$id]")
        revisited.remove(id)
    }
}

/**
 * Pretty print an Iterable.
 */
private fun ppIterable(obj: Iterable<*>, visited: MutableSet<Int>, revisited: MutableSet<Int>, currentDepth: String) {
    val increasedDepth = deepen(currentDepth)

    writeLine('[')
    obj.ppContents(currentDepth, ",") {
        write(increasedDepth)
        ppAny(it, visited, revisited, increasedDepth)
    }
    write(']')
    val id = System.identityHashCode(obj)
    if (revisited.contains(id)) write("[\$id=$id]")
}

/**
 * Pretty print a Map.
 */
private fun ppMap(obj: Map<*, *>, visited: MutableSet<Int>, revisited: MutableSet<Int>, currentDepth: String) {
    val increasedDepth = deepen(currentDepth)

    writeLine('{')
    obj.entries.ppContents(currentDepth, ",") {
        write(increasedDepth)
        ppAny(it.key, visited, revisited, increasedDepth)
        write(" -> ")
        ppAny(it.value, visited, revisited, increasedDepth)
    }
    write('}')
    val id = System.identityHashCode(obj)
    if (revisited.contains(id)) write("[\$id=$id]")
    revisited.remove(id)
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
private fun wordWrap(text: String, padding: String): String {
    val words = text.split(' ')
    val sb = StringBuilder(words.first())
    var spaceLeft = WRAPPED_LINE_WIDTH - words.first().length
    for (word in words.drop(1)) {
        val len = word.length
        if (len + 1 > spaceLeft) {
            sb.append("\n").append(padding).append(word)
            spaceLeft = WRAPPED_LINE_WIDTH - len
        } else {
            sb.append(" ").append(word)
            spaceLeft -= (len + 1)
        }
    }
    return sb.toString()
}

private fun deepen(currentDepth: String, size: Int = indentSize): String =
    " ".repeat(size) + currentDepth