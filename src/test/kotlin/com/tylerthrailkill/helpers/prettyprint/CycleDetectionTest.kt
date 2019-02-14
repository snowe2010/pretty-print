package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CycleDetectionTest : Spek({
    setup()

    describe("tiny object should") {
        context("render") {
            it("a single field ") {
                val unit = Unit
                val identity = System.identityHashCode(unit)
                prettyPrint(unit) mapsTo """
                Unit(
                  INSTANCE = cyclic reference detected for $identity
                )[${'$'}id=$identity]
                """
            }
            it("detects no cycle when an element is repeated several times in the same objects fields") {
                val smallObject = SmallObject("a string in small object", 777)
                val nestedLargeObjectNull = NestedLargeObject(
                    NestedSmallObject(smallObject),
                    smallObject,
                    "test string, please don't break",
                    null
                )
                prettyPrint(nestedLargeObjectNull) mapsTo """
                NestedLargeObject(
                  nestedSmallObject = NestedSmallObject(
                    smallObject = SmallObject(
                      field1 = a string in small object
                      field2 = 777
                    )
                  )
                  smallObject = SmallObject(
                    field1 = a string in small object
                    field2 = 777
                  )
                  testString = test string, please don't break
                  bigObject = null
                )
                """
            }
        }
    }
})
