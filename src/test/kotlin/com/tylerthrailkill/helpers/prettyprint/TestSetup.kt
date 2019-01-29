package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.dsl.Root
import org.spekframework.spek2.dsl.TestBody
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

fun Root.setupStreams() {
    val outContent by memoized<ByteArrayOutputStream>()
    val errContent by memoized<ByteArrayOutputStream>()
    val originalOut = System.out
    val originalErr = System.err

    // Create new byte stream for each test
    beforeEachTest {
        System.setOut(PrintStream(outContent))
        System.setErr(PrintStream(errContent))
    }
    afterEachTest {
        System.setOut(originalOut)
        System.setErr(originalErr)
    }
}

fun TestBody.prettyPrint(obj: Any?): ByteArrayOutputStream {
    pp(obj)
    val outContent by memoized<ByteArrayOutputStream>()
    return outContent
}

infix fun ByteArrayOutputStream.mapsTo(expected: String) {
    assertEquals(
        expected.trimIndent(),
        this.toString().trim().replace("\r", "")
    )
}