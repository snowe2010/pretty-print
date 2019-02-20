package com.tylerthrailkill.helpers.prettyprint

import com.ibm.icu.text.BreakIterator
import mu.KotlinLogging
import java.math.BigDecimal
import java.math.BigInteger
import java.util.UUID

const val DEFAULT_WRAP_WIDTH = 80
const val DEFAULT_INDENT = 2
val DEFAULT_OUT: Appendable = System.out

/**
 * Pretty print function.
 *
 * Prints any object in a pretty format for easy debugging/reading
 *
 * @param [obj] the object to pretty print
 * @param [indent] optional param that specifies the number of spaces to use to indent. Defaults to 2.
 * @param [writeTo] optional param that specifies the [Appendable] to output the pretty print to. Defaults appending to `System.out`.
 * @param [wrappedLineWidth] optional param that specifies how many characters of a string should be on a line.
 */
fun pp(
    obj: Any?,
    indent: Int = DEFAULT_INDENT,
    writeTo: Appendable = DEFAULT_OUT,
    wrappedLineWidth: Int = DEFAULT_WRAP_WIDTH
) = PrettyPrinter(indent, writeTo, wrappedLineWidth).pp(obj)


/**
 * Inline helper method for printing withing method chains. Simply delegates to [pp]
 *
 * Example:
 *   val foo = op2(op1(bar).pp())
 *
 * @param [T] the object to pretty print
 * @param [indent] optional param that specifies the number of spaces to use to indent. Defaults to 2.
 * @param [writeTo] optional param that specifies the [Appendable] to output the pretty print to. Defaults appending to `System.out`
 * @param [wrappedLineWidth] optional param that specifies how many characters of a string should be on a line.
 */
fun <T> T.pp(
    indent: Int = DEFAULT_INDENT,
    writeTo: Appendable = DEFAULT_OUT,
    wrappedLineWidth: Int = DEFAULT_WRAP_WIDTH
): T = this.also { pp(it, indent, writeTo, wrappedLineWidth) }

/**
 * Class used to perform pretty printing operations.
 */
private class PrettyPrinter(val tabSize: Int, val writeTo: Appendable, val wrappedLineWidth: Int) {
    private val lineInstance = BreakIterator.getLineInstance()
    private val logger = KotlinLogging.logger {}

    fun pp(obj: Any?) {
        ppAny(obj, mutableSetOf(), mutableSetOf())
        writeLine()
    }

    /**
     * Pretty prints any object. `collectionElementPad` is how much more to indent the contents of a collection.
     * `objectFieldPad` is how much to indent the fields of an object.
     */
    private fun ppAny(
        obj: Any?,
        visited: MutableSet<Int>,
        revisited: MutableSet<Int>,
        collectionElementPad: String = "",
        objectFieldPad: String = collectionElementPad
    ) {
        val id = System.identityHashCode(obj)

        if (!isAtomic(obj) && visited.contains(id)) {
            write("cyclic reference detected for $id")
            revisited.add(id)
            return
        }

        if (!isAtomic(obj)) visited.add(id)

        when {
            obj is Iterable<*> -> ppIterable(obj, visited, revisited, collectionElementPad)
            obj is Map<*, *> -> ppMap(obj, visited, revisited, collectionElementPad)
            obj is String -> ppString(obj, collectionElementPad)
            isAtomic(obj) -> ppAtomic(obj)
            obj is Any -> ppPlainObject(obj, visited, revisited, objectFieldPad)
        }

        visited.remove(id)

        if (revisited.contains(id)) {
            write("[\$id=$id]")
            revisited.remove(id)
        }
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

    private fun ppAtomic(obj: Any?) {
        write(obj.toString())
    }

    /**
     * Pretty print a plain object.
     */
    private fun ppPlainObject(obj: Any, visited: MutableSet<Int>, revisited: MutableSet<Int>, currentDepth: String) {
        val increasedDepth = deepen(currentDepth)
        val className = obj.javaClass.simpleName

        writeLine("$className(")
        obj.javaClass.declaredFields
            .filterNot { it.isSynthetic }
            .toList()
            .ppContents(currentDepth) {
                it.isAccessible = true
                write("$increasedDepth${it.name} = ")
                val extraIncreasedDepth = deepen(increasedDepth, it.name.length + 3) // 3 is " = ".length in prev line
                val fieldValue = it.get(obj)
                logger.debug { "field value is ${fieldValue.javaClass}" }
                ppAny(fieldValue, visited, revisited, extraIncreasedDepth, increasedDepth)
            }
        write(')')
    }

    private fun ppString(s: String, currentDepth: String) {
        if (s.length > wrappedLineWidth) {
            val tripleDoubleQuotes = "\"\"\""
            writeLine(tripleDoubleQuotes)
            writeLine(wordWrap(s, currentDepth))
            write("$currentDepth$tripleDoubleQuotes")
        } else {
            write("\"$s\"")
        }
    }

    /**
     * Pretty print an Iterable.
     */
    private fun ppIterable(
        obj: Iterable<*>,
        visited: MutableSet<Int>,
        revisited: MutableSet<Int>,
        currentDepth: String
    ) {
        val increasedDepth = deepen(currentDepth)

        writeLine('[')
        obj.ppContents(currentDepth, ",") {
            write(increasedDepth)
            ppAny(it, visited, revisited, increasedDepth)
        }
        write(']')
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
    }

    /**
     * Writes to the writeTo with a new line and adds logging
     */
    private fun writeLine(str: Any? = "") {
        logger.debug { "writing $str" }
        writeTo.append(str.toString()).appendln()
    }

    /**
     * Writes to the writeTo and adds logging
     */
    private fun write(str: Any?) {
        logger.debug { "writing $str" }
        writeTo.append(str.toString())
    }

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
        breakableLocations.drop(1).forEach {
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

    private fun deepen(currentDepth: String, size: Int = tabSize): String = " ".repeat(size) + currentDepth
}
