package com.tylerthrailkill.helpers.prettyprint

import com.google.common.collect.Collections2
import org.spekframework.spek2.Spek
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

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
    
    group("maps") {
        group("strings") {
            test("single key value pair") {
                val testObject = mapOf("key" to "value")
                pp(testObject)
                p(outContent)
                outContent shouldRenderLike """
                {
                  "key" -> "value"
                }
                """
            }

            test("render many key value pairs") {
                val testObject = mapOf(
                    "key1" to "value1",
                    "key2" to "value2",
                    "key3" to "value3",
                    "key4" to "value4"
                )
                pp(testObject)
                p(outContent)
                outContent shouldRenderLike """
                {
                  "key1" -> "value1",
                  "key2" -> "value2",
                  "key3" -> "value3",
                  "key4" -> "value4"
                }
                """
            }
        }

        test("render key value pairs with object as value") {
            val testObject = mapOf(
                "key1" to SmallObject("field", 1)
            )
            pp(testObject)
            p(outContent)
            outContent shouldRenderLike """
                {
                  "key1" -> SmallObject(
                    field1 = field
                    field2 = 1
                  )
                }
                """
        }

        test("render key value pairs with object as value") {
            val testObject = mapOf(
                "key1" to SmallObject("field", 1)
            )
            pp(testObject)
            p(outContent)
            outContent shouldRenderLike """
                {
                  "key1" -> SmallObject(
                    field1 = field
                    field2 = 1
                  )
                }
                """
        }
    }
})
