package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.Suite
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

object NestedObjectTest : Spek({
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

    describe("small nested object should") {
        context("render a single field with multiple subfields") {
            testObject(NestedSmallObject(SmallObject("a", 1)))
            mapsTo(
                """
                NestedSmallObject(
                  smallObject = SmallObject(
                    field1 = a
                    field2 = 1
                  )
                )
                """
            )
        }
    }

    describe("nested large object should") {
        context("render many nested fields") {
            testObject(
                NestedLargeObject(
                    NestedSmallObject(SmallObject("smallObjectField1", 777)),
                    SmallObject("a field in top level nested large object", 17),
                    "a test string in NestedLargeObject",
                    NestedLargeObject(
                        NestedSmallObject(SmallObject("inner small object field 1", 888)),
                        SmallObject("field 1 in nested small", 12),
                        "a test string in NestedLargeObject inner"
                    )
                )
            )
            mapsTo(
                """
                NestedLargeObject(
                  nestedSmallObject = NestedSmallObject(
                    smallObject = SmallObject(
                      field1 = smallObjectField1
                      field2 = 777
                    )
                  )
                  smallObject = SmallObject(
                    field1 = a field in top level nested large object
                    field2 = 17
                  )
                  testString = a test string in NestedLargeObject
                  bigObject = NestedLargeObject(
                    nestedSmallObject = NestedSmallObject(
                      smallObject = SmallObject(
                        field1 = inner small object field 1
                        field2 = 888
                      )
                    )
                    smallObject = SmallObject(
                      field1 = field 1 in nested small
                      field2 = 12
                    )
                    testString = a test string in NestedLargeObject inner
                    bigObject = null
                  )
                )
                """
            )
        }
    }

    describe("nested object with collection should") {
        context("render a single item in a collection") {
            testObject(
                NestedObjectWithCollection(
                    listOf(1)
                )
            )
            mapsTo(
                """
                NestedObjectWithCollection(
                  coll = [
                           1
                         ]
                )
                """
            )
        }

        context("render a single string in a collection") {
            testObject(
                NestedObjectWithCollection(
                    listOf("a string with spaces")
                )
            )
            mapsTo(
                """
                NestedObjectWithCollection(
                  coll = [
                           "a string with spaces"
                         ]
                )
                """
            )
        }

        context("render multiple objects in a collection, with commas") {
            testObject(
                NestedObjectWithCollection(
                    listOf(1, 2)
                )
            )
            mapsTo(
                """
                NestedObjectWithCollection(
                  coll = [
                           1,
                           2
                         ]
                )
                """
            )
        }

        context("render a single nested object in a collection") {
            testObject(
                NestedObjectWithCollection(
                    listOf(NestedSmallObject(SmallObject("a", 1)))
                )
            )
            mapsTo(
                """
                NestedObjectWithCollection(
                  coll = [
                           NestedSmallObject(
                             smallObject = SmallObject(
                               field1 = a
                               field2 = 1
                             )
                           )
                         ]
                )
                """
            )
        }

        context("render a multiple nested objects in a collection, with commas") {
            testObject(
                NestedObjectWithCollection(
                    listOf(
                        NestedSmallObject(SmallObject("a", 1)),
                        NestedSmallObject(SmallObject("a", 1)),
                        NestedSmallObject(SmallObject("a", 1)),
                        NestedSmallObject(SmallObject("a", 1))
                    )
                )
            )
            mapsTo(
                """
                NestedObjectWithCollection(
                  coll = [
                           NestedSmallObject(
                             smallObject = SmallObject(
                               field1 = a
                               field2 = 1
                             )
                           ),
                           NestedSmallObject(
                             smallObject = SmallObject(
                               field1 = a
                               field2 = 1
                             )
                           ),
                           NestedSmallObject(
                             smallObject = SmallObject(
                               field1 = a
                               field2 = 1
                             )
                           ),
                           NestedSmallObject(
                             smallObject = SmallObject(
                               field1 = a
                               field2 = 1
                             )
                           )
                         ]
                )
                """
            )
        }
    }
})

infix fun ByteArrayOutputStream.shouldRenderLike(expected: String) {
    assertEquals(
        expected.trimIndent(),
        this.toString().trim().replace("\r", "")
    )
}

