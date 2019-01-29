package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.Suite
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream
import java.io.PrintStream

object MapTest : Spek({
    val outContent by memoized { ByteArrayOutputStream() }
    val errContent by memoized { ByteArrayOutputStream() }
    setupStreams()

    describe("maps") {
        context("strings") {
            it("single key value pair") {
                prettyPrint(mapOf("key" to "value")) mapsTo """
                {
                  "key" -> "value"
                }
                """
            }
            it("render many key value pairs") {
                prettyPrint(
                    mapOf(
                        "key1" to "value1",
                        "key2" to "value2",
                        "key3" to "value3",
                        "key4" to "value4"
                    )
                ) mapsTo """
                {
                  "key1" -> "value1",
                  "key2" -> "value2",
                  "key3" -> "value3",
                  "key4" -> "value4"
                }
                """
            }
        }

        context("objects") {
            it("single object") {
                prettyPrint(
                    mapOf(
                        "key1" to SmallObject("field", 1)
                    )
                ) mapsTo """
                {
                  "key1" -> SmallObject(
                    field1 = field
                    field2 = 1
                  )
                }
                """
            }
            it("multiple objects") {
                prettyPrint(
                    mapOf(
                        "key1" to SmallObject("field", 1),
                        "key2" to SmallObject("field2", 2)
                    )
                ) mapsTo """
                {
                  "key1" -> SmallObject(
                    field1 = field
                    field2 = 1
                  ),
                  "key2" -> SmallObject(
                    field1 = field2
                    field2 = 2
                  )
                }
                """
            }
        }
    }
})
