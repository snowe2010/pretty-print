package com.tylerthrailkill.helpers.prettyprint

import mu.KotlinLogging
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

private val logger = KotlinLogging.logger {}
var defaultWrapWidth = 80
var defaultIndentSize = 2
var defaultAppendable: Appendable = System.out

/**
 * Pretty print function.
 *
 * Prints any object in a pretty format for easy debugging/reading
 *
 * @param [obj] the object to pretty print
 * @param [indent] optional param that specifies the number of spaces to use to indent. Defaults to 2.
 * @param [writeTo] optional param that specifies the [Appendable] to output the pretty print to. Defaults appending to `System.out`.
 */
fun pp(obj: Any?, indent: Int = defaultIndentSize, writeTo: Appendable = defaultAppendable) {
    defaultIndentSize = indent
    defaultAppendable = writeTo
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
fun <T> T.pp(indent: Int = defaultIndentSize, writeTo: Appendable = defaultAppendable): T =
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

    when {
        obj is Iterable<*> -> ppIterable(obj, visited, revisited, collectionItemPad)
        obj is Map<*, *> -> ppMap(obj, visited, revisited, collectionItemPad)
        obj is String -> ppString(obj, collectionItemPad)
        isAtomic(obj) -> write(obj)
        obj is Any -> ppPlainObject(obj, visited, revisited, objectFieldPad)
    }

    visited.remove(id)
}

private fun isAtomic(o: Any?): Boolean =
    o == null
            || o is Char || o is Number || o is Boolean || o is BigInteger || o is BigDecimal || o is UUID

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

private fun ppAtomic(obj: Any?, currentDepth: String) {
    write(obj.toString())
}

/**
 * Pretty print a plain object.
 */
private fun ppPlainObject(obj: Any, visited: MutableSet<Int>, revisited: MutableSet<Int>, currentDepth: String) {
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
            val extraIncreasedDepth = deepen(increasedDepth, it.name.length + 3) // " = ".length is 3 in prev line
            val fieldValue = it.get(obj)
            logger.debug { "field value is ${fieldValue.javaClass}" }
            ppAny(fieldValue, visited, revisited, extraIncreasedDepth, increasedDepth)
        }
    write(')')
    val id = System.identityHashCode(obj)
    if (revisited.contains(id)) write("[\$id=$id]")
    revisited.remove(id)
}

private fun ppString(s: String, currentDepth: String) {
    if (s.length > defaultWrapWidth) {
        val tripleDoubleQuotes = "\"\"\""
        writeLine(tripleDoubleQuotes)
        writeLine("$currentDepth${wordWrap(s, currentDepth)}")
        write("$currentDepth$tripleDoubleQuotes")
    } else {
        write("\"$s\"")
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
 * Writes to the defaultAppendable with a new line and adds logging
 */
private fun writeLine(str: Any? = "") {
    logger.debug { "writing $str" }
    defaultAppendable.append(str.toString()).appendln()
}

/**
 * Writes to the defaultAppendable and adds logging
 */
private fun write(str: Any?) {
    logger.debug { "writing $str" }
    defaultAppendable.append(str.toString())
}

/**
 * Generates a deeper string based on the current depth and tab size
 */
private fun wordWrap(text: String, padding: String): String {
    val words = text.split(' ')
    val sb = StringBuilder(words.first())
    var spaceLeft = defaultWrapWidth - words.first().length
    for (word in words.drop(1)) {
        val len = word.length
        if (len + 1 > spaceLeft) {
            sb.append("\n").append(padding).append(word)
            spaceLeft = defaultWrapWidth - len
        } else {
            sb.append(" ").append(word)
            spaceLeft -= (len + 1)
        }
    }
    return sb.toString()
}

private fun deepen(currentDepth: String, size: Int = defaultIndentSize): String =
    " ".repeat(size) + currentDepth