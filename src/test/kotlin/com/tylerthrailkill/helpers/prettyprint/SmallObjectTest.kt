package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.Suite
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

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

    // Single test spot
    fun Suite.mapsTo(expected: String) {
        val testObject by memoized<Any>()
        it("test value") {
            pp(testObject)
//            debugPrint(outContent)
            outContent shouldRenderLike expected
        }
    }

    fun testObject(obj: Any?) {
        val testObject by memoized { obj }
    }
    
    describe("tiny object should") {
        context("render a single field") {
            testObject(TinyObject(1))
            mapsTo(
                """
                TinyObject(
                  int = 1
                )
                """
            )
        }
    }
    describe("small object should") {
        context("render two fields") {
            testObject(SmallObject("a", 1))
            mapsTo(
                """
                SmallObject(
                  field1 = a
                  field2 = 1
                )
            """
            )
        }
    }
})
