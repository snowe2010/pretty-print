package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

object SmallObjectTest : Spek({
    val tinyObject = TinyObject(1)
    val smallObject = SmallObject("a", 1)
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

    group("tiny object should") {
        test("render a single field") {
            pp(tinyObject)
            assertEquals(
                """
                TinyObject(
                  int = 1
                )
                """.trimIndent(),
                outContent.toString().trim().replace("\r", "")
            )
        }
    }
    group("small object should") {
        test("render two fields") {
            pp(smallObject)
            System.setOut(originalOut)
            println(outContent)
            assertEquals(
                """
                SmallObject(
                  field1 = a
                  field2 = 1
                )
            """.trimIndent(), outContent.toString().trim().replace("\r", "")
            )
        }
    }
})
