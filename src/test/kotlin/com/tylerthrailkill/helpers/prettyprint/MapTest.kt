package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.Suite
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream
import java.io.PrintStream

object MapTest : Spek({
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

    // Single test spot
    fun Suite.mapsTo(expected: String) {
        val testObject by memoized<Any>()
        it("test value") {
            pp(testObject)
//            p(outContent)
            outContent shouldRenderLike expected
        }
    }

    fun testObject(obj: Any?) {
        val testObject by memoized { obj }
    }
    describe("maps") {
        context("strings") {
            context("single key value pair") {
                testObject(mapOf("key" to "value"))
                mapsTo(
                    """
                {
                  "key" -> "value"
                }
                """
                )
            }
            context("render many key value pairs") {
                testObject(
                    mapOf(
                        "key1" to "value1",
                        "key2" to "value2",
                        "key3" to "value3",
                        "key4" to "value4"
                    )
                )
                mapsTo(
                    """
                {
                  "key1" -> "value1",
                  "key2" -> "value2",
                  "key3" -> "value3",
                  "key4" -> "value4"
                }
                """
                )
            }
        }

        context("objects") {
            context("single object") {
                testObject(
                    mapOf(
                        "key1" to SmallObject("field", 1)
                    )
                )
                mapsTo(
                    """
                {
                  "key1" -> SmallObject(
                    field1 = field
                    field2 = 1
                  )
                }
                """
                )
            }
        }
    }
})
