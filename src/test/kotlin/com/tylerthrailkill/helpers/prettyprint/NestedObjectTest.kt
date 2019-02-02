package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream

object NestedObjectTest : Spek({
    setup()

    describe("small nested object should") {
        it("render a single field with multiple subfields") {
            prettyPrint(NestedSmallObject(SmallObject("a", 1))) mapsTo """
                NestedSmallObject(
                  smallObject = SmallObject(
                    field1 = a
                    field2 = 1
                  )
                )
                """

        }
    }

    describe("nested large object should") {
        it("render many nested fields") {
            prettyPrint(
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
            ) mapsTo """
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

    describe("nested object with collection should") {
        it("render a single item in a collection") {
            prettyPrint(
                NestedObjectWithCollection(
                    listOf(1)
                )
            ) mapsTo """
                NestedObjectWithCollection(
                  coll = [
                           1
                         ]
                )
                """
        }

        it("render a single string in a collection") {
            prettyPrint(
                NestedObjectWithCollection(
                    listOf("a string with spaces")
                )
            ) mapsTo """
                NestedObjectWithCollection(
                  coll = [
                           "a string with spaces"
                         ]
                )
                """
        }

        it("render multiple objects in a collection, with commas") {
            prettyPrint(
                NestedObjectWithCollection(
                    listOf(1, 2)
                )
            ) mapsTo """
                NestedObjectWithCollection(
                  coll = [
                           1,
                           2
                         ]
                )
                """
        }

        it("render a single nested object in a collection") {
            prettyPrint(
                NestedObjectWithCollection(
                    listOf(NestedSmallObject(SmallObject("a", 1)))
                )
            ) mapsTo """
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

        it("render a multiple nested objects in a collection, with commas") {
            prettyPrint(
                NestedObjectWithCollection(
                    listOf(
                        NestedSmallObject(SmallObject("a", 1)),
                        NestedSmallObject(SmallObject("a", 1)),
                        NestedSmallObject(SmallObject("a", 1)),
                        NestedSmallObject(SmallObject("a", 1))
                    )
                )
            ) mapsTo """
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
