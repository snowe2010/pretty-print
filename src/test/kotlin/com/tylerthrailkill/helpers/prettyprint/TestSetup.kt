package com.tylerthrailkill.helpers.prettyprint

import mu.KotlinLogging
import org.spekframework.spek2.dsl.Root
import org.spekframework.spek2.dsl.TestBody
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

private val logger = KotlinLogging.logger {}

fun Root.setup() {
    val outContent by memoized { ByteArrayOutputStream() }
}

/**
 *
 * Test helper function, defaults to providing no tab size and wrapping the print stream in
 * a ByteArrayOutputStream to test against
 */
fun TestBody.prettyPrint(obj: Any?, tabSize: Int? = null): ByteArrayOutputStream {
    val outContent by memoized<ByteArrayOutputStream>()
    if (tabSize == null) {
        pp(obj, printStream = PrintStream(outContent))
    } else {
        pp(obj, tabSize = tabSize, printStream = PrintStream(outContent))
    }
    return outContent
}

/**
 * Accepts an `expected` string, and compares it against `this`. Removes leading indents and normalizes newlines
 */
infix fun ByteArrayOutputStream.mapsTo(expected: String) {
    logger.info { "Actual output: $this" }
    assertEquals(
        expected.trimIndent(),
        this.toString().trim().replace("\r", "")
    )
}