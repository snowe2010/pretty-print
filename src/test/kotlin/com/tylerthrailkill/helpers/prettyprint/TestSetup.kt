package com.tylerthrailkill.helpers.prettyprint

import mu.KotlinLogging
import org.spekframework.spek2.dsl.Root
import org.spekframework.spek2.dsl.TestBody
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

val logger = KotlinLogging.logger {}

fun Root.setup() {
    val outContent by memoized { ByteArrayOutputStream() }
}

/**
 *
 * Test helper function, defaults to providing no tab size and wrapping the print stream in
 * a ByteArrayOutputStream to test against
 */
fun TestBody.prettyPrint(obj: Any?, tabSize: Int? = null, wrappedLineWidth: Int? = null): ByteArrayOutputStream {
    val outContent by memoized<ByteArrayOutputStream>()
    if (tabSize == null) {
        pp(obj, printStream = PrintStream(outContent), wrappedLineWidth = wrappedLineWidth ?: 80) // TODO fix this, really messy
    } else {
        pp(obj, tabSize = tabSize, printStream = PrintStream(outContent), wrappedLineWidth = wrappedLineWidth ?: 80) // TODO ^
    }
    return outContent
}
/**
 * Helper function for testing the inline version of `pp`
 * It defaults to providing no tab size and wrapping the print stream in
 * a ByteArrayOutputStream to test against
 */
fun TestBody.prettyPrintInline(obj: Any?, tabSize: Int? = null): ByteArrayOutputStream {
    val outContent by memoized<ByteArrayOutputStream>()
    val printStream = PrintStream(outContent)
    if (tabSize == null) {
        inlineWrapper(obj.pp(printStream = printStream), printStream)
    } else {
        inlineWrapper(obj.pp(tabSize = tabSize, printStream = printStream), printStream)
    }
    return outContent
}

/**
 * Prints a test string
 */
fun inlineWrapper(obj: Any?, printStream: PrintStream) {
    printStream.println("inline wrapper function entered")
}

/**
 * Accepts an `expected` string, and compares it against `this`. Removes leading indents and normalizes newlines
 */
infix fun ByteArrayOutputStream.mapsTo(expected: String) {
    logger.info { "Actual output: $this" }
    logger.info { "Expected output: ${expected.trimIndent()}"}
    assertEquals(
        expected.trimIndent(),
        this.toString().trim().replace("\r", "")
    )
}
