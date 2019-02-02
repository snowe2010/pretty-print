package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream

object MapTest : Spek({
    setup()

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
            it("map of lists") {
                prettyPrint(
                    mapOf(
                        listOf(1, 2, 3) to listOf(4, 5, 6),
                        listOf(7, 8, 9) to listOf(0, 1, 2)
                    )
                ) mapsTo """
                {
                  [
                    1,
                    2,
                    3
                  ] -> [
                    4,
                    5,
                    6
                  ],
                  [
                    7,
                    8,
                    9
                  ] -> [
                    0,
                    1,
                    2
                  ]
                }
                """
            }
        }
        context("null stuff") {
            it("null keys") {
                prettyPrint(
                    mapOf(
                        null to SmallObject("field", 1)
                    )
                ) mapsTo """
                {
                  null -> SmallObject(
                    field1 = field
                    field2 = 1
                  )
                }
                """
            }
            it("null values") {
                prettyPrint(
                    mapOf(
                        "key1" to null
                    )
                ) mapsTo """
                {
                  "key1" -> null
                }
                """
            }
            it("multiple null values") {
                prettyPrint(
                    mapOf(
                        "key1" to null,
                        "key2" to null
                    )
                ) mapsTo """
                {
                  "key1" -> null,
                  "key2" -> null
                }
                """
            }
            it("null key and null value") {
                prettyPrint(
                    mapOf(
                        null to null
                    )
                ) mapsTo """
                {
                  null -> null
                }
                """
            }
            it("null key and null value") {
                prettyPrint(
                    mapOf(
                        null to SmallObject("field", 1),
                        "key2" to null
                    )
                ) mapsTo """
                {
                  null -> SmallObject(
                    field1 = field
                    field2 = 1
                  ),
                  "key2" -> null
                }
                """
            }
        }
    }
})
