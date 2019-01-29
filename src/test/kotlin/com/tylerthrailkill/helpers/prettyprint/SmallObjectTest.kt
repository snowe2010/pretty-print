package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.Suite
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream
import java.io.PrintStream

object SmallObjectTest : Spek({
    val outContent by memoized { ByteArrayOutputStream() }
    val errContent by memoized { ByteArrayOutputStream() }
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

    fun debugPrint(outContent: ByteArrayOutputStream) {
        System.setOut(originalOut)
        println(outContent)
    }


    fun testObject(obj: Any?) {
        val testObject by memoized { obj }
    }

    fun Suite.validateTestOutput(testObject: Any?, expected: String) {
        it("test value") {
            pp(testObject)
            outContent shouldRenderLike expected
        }
    }

    describe("tiny object should") {
        infix fun Any.mapsTo(expected: String) = validateTestOutput(this, expected)

        context("render a single field") {
            TinyObject(1) mapsTo """
                TinyObject(
                  int = 1
                )
                """

        }
    }
    describe("small object should") {
        infix fun Any.mapsTo(expected: String) = validateTestOutput(this, expected)
        context("render two fields") {
            SmallObject("a", 1) mapsTo """
                SmallObject(
                  field1 = a
                  field2 = 1
                )
            """
        }
    }
})
