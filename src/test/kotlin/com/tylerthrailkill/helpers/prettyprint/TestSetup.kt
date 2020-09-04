package com.tylerthrailkill.helpers.prettyprint

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import mu.KotlinLogging
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

val logger = KotlinLogging.logger {}


/**
 * Prints a test string
 */
fun inlineWrapper(obj: Any?, printStream: PrintStream) {
    printStream.println("inline wrapper function entered")
}

/**
 * Accepts an `expected` string, and compares it against `this`. Removes leading indents and normalizes newlines
 */
infix fun ByteArrayOutputStream.mapTo(expected: String) {
    logger.info { "Actual output: $this" }
    logger.info { "Expected output: ${expected.trimIndent()}"}
    assertEquals(
        expected.trimIndent(),
        this.toString().trim().replace("\r", "")
    )
}

/**
 *
 * Test helper function, defaults to providing no tab size and wrapping the print stream in
 * a ByteArrayOutputStream to test against
 */
fun prettyPrint(obj: Any?, tabSize: Int? = null, wrappedLineWidth: Int? = null): ByteArrayOutputStream {
    val outContent = ByteArrayOutputStream()
    if (tabSize == null) {
        pp(obj, writeTo = PrintStream(outContent), wrappedLineWidth = wrappedLineWidth ?: 80) // TODO fix this, really messy
    } else {
        pp(obj, indent = tabSize, writeTo = PrintStream(outContent), wrappedLineWidth = wrappedLineWidth ?: 80) // TODO ^
    }
    return outContent
}

/**
 * Helper function for testing the inline version of `pp`
 * It defaults to providing no tab size and wrapping the print stream in
 * a ByteArrayOutputStream to test against
 */
fun prettyPrintInline(obj: Any?, tabSize: Int? = null): ByteArrayOutputStream {
//    val outContent by memoized<ByteArrayOutputStream>()
    val outContent = ByteArrayOutputStream()
    val printStream = PrintStream(outContent)
    if (tabSize == null) {
        inlineWrapper(obj.pp(writeTo = printStream), printStream)
    } else {
        inlineWrapper(obj.pp(indent = tabSize, writeTo = printStream), printStream)
    }
    return outContent
}

fun mapTo(expected: String) = object : Matcher<ByteArrayOutputStream> {
    override fun test(value: ByteArrayOutputStream) =
        MatcherResult(
            expected.trimIndent() == value.toString().trim().replace("\r",""),
            "$value does not match expected output $expected",
            "$value does not match expected output $expected"
        )
}
