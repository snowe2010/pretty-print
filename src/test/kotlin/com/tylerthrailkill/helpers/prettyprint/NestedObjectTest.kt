package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec

class NestedObjectTest : FreeSpec({
    "small nested object should" - {
        "render a single field with multiple subfields" - {
            prettyPrint(NestedSmallObject(SmallObject("a", 1))) mapTo """
                NestedSmallObject(
                  smallObject = SmallObject(
                    field1 = "a"
                    field2 = 1
                  )
                )
                """

        }
    }

    "nested large object should" - {
        "render many nested fields" - {
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
            ) mapTo """
                NestedLargeObject(
                  nestedSmallObject = NestedSmallObject(
                    smallObject = SmallObject(
                      field1 = "smallObjectField1"
                      field2 = 777
                    )
                  )
                  smallObject = SmallObject(
                    field1 = "a field in top level nested large object"
                    field2 = 17
                  )
                  testString = "a test string in NestedLargeObject"
                  bigObject = NestedLargeObject(
                    nestedSmallObject = NestedSmallObject(
                      smallObject = SmallObject(
                        field1 = "inner small object field 1"
                        field2 = 888
                      )
                    )
                    smallObject = SmallObject(
                      field1 = "field 1 in nested small"
                      field2 = 12
                    )
                    testString = "a test string in NestedLargeObject inner"
                    bigObject = null
                  )
                )
                """
        }
    }

    "nested object with collection should" - {
        "render a single item in a collection" - {
            prettyPrint(
                NestedObjectWithCollection(
                    listOf(1)
                )
            ) mapTo """
                NestedObjectWithCollection(
                  coll = [
                           1
                         ]
                )
                """
        }

        "render a single string in a collection" - {
            prettyPrint(
                NestedObjectWithCollection(
                    listOf("a string with spaces")
                )
            ) mapTo """
                NestedObjectWithCollection(
                  coll = [
                           "a string with spaces"
                         ]
                )
                """
        }

        "render multiple objects in a collection, with commas" - {
            prettyPrint(
                NestedObjectWithCollection(
                    listOf(1, 2)
                )
            ) mapTo """
                NestedObjectWithCollection(
                  coll = [
                           1,
                           2
                         ]
                )
                """
        }

        "render a single nested object in a collection" - {
            prettyPrint(
                NestedObjectWithCollection(
                    listOf(NestedSmallObject(SmallObject("a", 1)))
                )
            ) mapTo """
                NestedObjectWithCollection(
                  coll = [
                           NestedSmallObject(
                             smallObject = SmallObject(
                               field1 = "a"
                               field2 = 1
                             )
                           )
                         ]
                )
                """
        }

        "render a multiple nested objects in a collection, with commas" - {
            prettyPrint(
                NestedObjectWithCollection(
                    listOf(
                        NestedSmallObject(SmallObject("a", 1)),
                        NestedSmallObject(SmallObject("a", 1)),
                        NestedSmallObject(SmallObject("a", 1)),
                        NestedSmallObject(SmallObject("a", 1))
                    )
                )
            ) mapTo """
                NestedObjectWithCollection(
                  coll = [
                           NestedSmallObject(
                             smallObject = SmallObject(
                               field1 = "a"
                               field2 = 1
                             )
                           ),
                           NestedSmallObject(
                             smallObject = SmallObject(
                               field1 = "a"
                               field2 = 1
                             )
                           ),
                           NestedSmallObject(
                             smallObject = SmallObject(
                               field1 = "a"
                               field2 = 1
                             )
                           ),
                           NestedSmallObject(
                             smallObject = SmallObject(
                               field1 = "a"
                               field2 = 1
                             )
                           )
                         ]
                )
                """
        }
    }
})
