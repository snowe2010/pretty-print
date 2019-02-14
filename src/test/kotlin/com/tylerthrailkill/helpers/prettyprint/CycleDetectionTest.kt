package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CycleDetectionTest : Spek({
    setup()

    describe("pretty printing") {
        context("plain objects with cycles") {
            it("should detect a cycle with plain Unit") {
                val unit = Unit
                val identity = System.identityHashCode(unit)
                prettyPrint(unit) mapsTo """
                Unit(
                  INSTANCE = cyclic reference detected for $identity
                )[${'$'}id=$identity]
                """
            }
            it("should detect no cycle when an element is repeated several times in the same objects fields") {
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
        context("maps with cycles") {
            it("should detect a cycle between an object with a map with an object with a cycle") {
                val objectWithMap = ObjectWithMap(
                    mutableMapOf(1 to null)
                )
                val objectContainingObjectWithMap = ObjectContainingObjectWithMap()
                objectContainingObjectWithMap.objectWithMap = objectWithMap
                objectWithMap.map[1] = objectContainingObjectWithMap
                val identity = System.identityHashCode(objectWithMap)
                prettyPrint(objectWithMap) mapsTo """
                ObjectWithMap(
                  map = {
                          1 -> ObjectContainingObjectWithMap(
                            objectWithMap = cyclic reference detected for $identity
                          )
                        }
                )[${'$'}id=$identity]
                """.trimIndent()
            }
            it("should detect a cycle of a map containing itself") {
                val outerMap: MutableMap<Int, Any?> = mutableMapOf(1 to null)
                val innerMap = mutableMapOf(1 to outerMap)
                outerMap[1] = innerMap
                val identity = System.identityHashCode(outerMap)
                prettyPrint(outerMap) mapsTo """
                {
                  1 -> {
                    1 -> cyclic reference detected for $identity
                  }
                }[${'$'}id=$identity]
                """.trimIndent()
            }
        }
        context("lists with cycles") {
            it("should detect a cycle between an object with a list with an object with a cycle") {
                val objectWithList = ObjectWithList(mutableListOf())
                val objectContainingObjectWithList = ObjectContainingObjectWithList()
                objectContainingObjectWithList.objectWithList = objectWithList
                objectWithList.list.add(objectContainingObjectWithList)
                val identity = System.identityHashCode(objectWithList)
                prettyPrint(objectWithList) mapsTo """
                ObjectWithList(
                  list = [
                           ObjectContainingObjectWithList(
                             objectWithList = cyclic reference detected for $identity
                           )
                         ]
                )[${'$'}id=$identity]
                """.trimIndent()
            }
            it("should detect a cycle of a list containing itself") {
                val outerList: MutableList<Any?> = mutableListOf()
                val innerList = mutableListOf(outerList)
                outerList.add(innerList)
                val identity = System.identityHashCode(outerList)
                prettyPrint(outerList) mapsTo """
                [
                  [
                    cyclic reference detected for $identity
                  ]
                ][${'$'}id=$identity]
                """.trimIndent()
            }
        }
    }
})
