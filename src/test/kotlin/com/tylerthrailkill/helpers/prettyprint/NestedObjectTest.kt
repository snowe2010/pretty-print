package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

object NestedObjectTest : Spek({
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

    group("small nested object should") {
        val nestedObject = NestedSmallObject(SmallObject("a", 1))
        test("render a single field with multiple subfields") {
            pp(nestedObject)
            p(outContent)
            outContent shouldRenderLike """
                NestedSmallObject(
                  smallObject = SmallObject(
                    field1 = a
                    field2 = 1
                  )
                )
                """
        }
    }

    group("nested large object should") {
        val nestedObject = NestedLargeObject(
            NestedSmallObject(SmallObject("smallObjectField1", 777)),
            SmallObject("a field in top level nested large object", 17),
            "a test string in NestedLargeObject",
            NestedLargeObject(
                NestedSmallObject(SmallObject("inner small object field 1", 888)),
                SmallObject("field 1 in nested small", 12),
                "a test string in NestedLargeObject inner"
            )
        )
        test("render many nested fields") {
            pp(nestedObject)
            p(outContent)
            outContent shouldRenderLike """
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
        }
    }

    group("nested object with collection should") {
        test("render a single item in a collection") {
            val nestedObject = NestedObjectWithCollection(
                listOf(1)
            )
            pp(nestedObject)
            p(outContent)
            outContent shouldRenderLike """
                NestedObjectWithCollection(
                  coll = [
                           1
                         ]
                )
                """
        }

        test("render a single string in a collection") {
            val nestedObject = NestedObjectWithCollection(
                listOf("a string with spaces")
            )
            pp(nestedObject)
            p(outContent)
            outContent shouldRenderLike """
                NestedObjectWithCollection(
                  coll = [
                           "a string with spaces"
                         ]
                )
                """
        }

        test("render multiple objects in a collection, with commas") {
            val nestedObject = NestedObjectWithCollection(
                listOf(1, 2)
            )
            pp(nestedObject)
            p(outContent)
            outContent shouldRenderLike """
                NestedObjectWithCollection(
                  coll = [
                           1,
                           2
                         ]
                )
                """
        }

        test("render a single nested object in a collection") {
            val nestedObject = NestedObjectWithCollection(
                listOf(NestedSmallObject(SmallObject("a", 1)))
            )
            pp(nestedObject)
            p(outContent)
            outContent shouldRenderLike """
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
        }

        test("render a multiple nested objects in a collection, with commas") {
            val nestedObject = NestedObjectWithCollection(
                listOf(NestedSmallObject(SmallObject("a", 1)),
                    NestedSmallObject(SmallObject("a", 1)),
                    NestedSmallObject(SmallObject("a", 1)),
                    NestedSmallObject(SmallObject("a", 1)))
            )
            pp(nestedObject)
            p(outContent)
            outContent shouldRenderLike """
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
        }
    }
})

infix fun ByteArrayOutputStream.shouldRenderLike(expected: String) {
    assertEquals(
        expected.trimIndent(),
        this.toString().trim().replace("\r", "")
    )
}

