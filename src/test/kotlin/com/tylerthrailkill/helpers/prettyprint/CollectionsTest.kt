package com.tylerthrailkill.helpers.prettyprint

import com.google.common.collect.Collections2
import org.spekframework.spek2.Spek
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

object CollectionsTest : Spek({
    lateinit var outContent: ByteArrayOutputStream
    lateinit var errContent: ByteArrayOutputStream
    val originalOut = System.out
    val originalErr = System.err

    // Create new byte stream for each test
    beforeEachTest {
        outContent = ByteArrayOutputStream()
        errContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
        System.setErr(PrintStream(errContent))
    }
    afterEachTest {
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

    fun p(outContent: ByteArrayOutputStream) {
        System.setOut(originalOut)
        println(outContent)
    }

    group("pretty printing lists should") {
        test("render a list of objects") {
            val testObject = listOf("a", "b", "c")
            pp(testObject)
            p(outContent)
            outContent shouldRenderLike """
                [
                  "a",
                  "b",
                  "c"
                ]
                """
        }
    }
})
