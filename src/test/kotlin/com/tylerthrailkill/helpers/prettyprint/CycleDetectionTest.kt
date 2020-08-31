package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec

class CycleDetectionTest : FreeSpec({
    "pretty printing" - {
        "plain objects with cycles" - {
            "should detect a cycle with plain Unit" - {
                val unit = Unit
                prettyPrint(unit) mapsTo """
                Unit(
                  INSTANCE = Unit.<static cyclic class reference>
                )
                """
            }
            "should detect a cycle with two small objects" - {
                val sco1 = SmallCyclicalObject1()
                val sco2 = SmallCyclicalObject2(sco1)
                sco1.c = sco2
                val identity = System.identityHashCode(sco1)
                prettyPrint(sco1) mapsTo """
                SmallCyclicalObject1(
                  c = SmallCyclicalObject2(
                    c = cyclic reference detected for $identity
                  )
                )[${'$'}id=$identity]
                """
            }
            "should detect no cycle when an element is repeated several times in the same objects fields" - {
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
                      field1 = "a string in small object"
                      field2 = 777
                    )
                  )
                  smallObject = SmallObject(
                    field1 = "a string in small object"
                    field2 = 777
                  )
                  testString = "test string, please don't break"
                  bigObject = null
                )
                """
            }
        }
        "maps with cycles" - {
            "should detect a cycle between an object with a map with an object with a cycle" - {
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
            "should detect a cycle of a map containing itself" - {
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
        "lists with cycles" - {
            "should detect a cycle between an object with a list with an object with a cycle" - {
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
            "should detect a cycle of a list containing itself" - {
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
        "a cycle of 3 objects" - {
            "a list contains 2 of these objects" - {
                "should only show ID of outermost object in the cycle each time the cycle gets printed" - {
                    // outermost meaning the first object in the cycle that pretty print calls on
                    val anyMap = { mutableMapOf<Any, Any>() }
                    val a = anyMap()
                    val b = anyMap()
                    val c = anyMap()

                    a["foo"] = b
                    b["fie"] = c
                    c["fum"] = a

                    prettyPrint(listOf(a, b)) mapsTo """
                        [
                          {
                            "foo" -> {
                              "fie" -> {
                                "fum" -> cyclic reference detected for ${System.identityHashCode(a)}
                              }
                            }
                          }[${'$'}id=${System.identityHashCode(a)}],
                          {
                            "fie" -> {
                              "fum" -> {
                                "foo" -> cyclic reference detected for ${System.identityHashCode(b)}
                              }
                            }
                          }[${'$'}id=${System.identityHashCode(b)}]
                        ]
                        """.trimIndent()
                }
            }
        }
    }
})
