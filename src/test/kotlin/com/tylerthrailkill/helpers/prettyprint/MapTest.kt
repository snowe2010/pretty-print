package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec

class MapTest : FreeSpec({

    "maps" - {
        "strings" - {
            "single key value pair" - {
                prettyPrint(mapOf("key" to "value")) mapTo """
                {
                  "key" -> "value"
                }
                """
            }
            "render many key value pairs" - {
                prettyPrint(
                    mapOf(
                        "key1" to "value1",
                        "key2" to "value2",
                        "key3" to "value3",
                        "key4" to "value4"
                    )
                ) mapTo """
                {
                  "key1" -> "value1",
                  "key2" -> "value2",
                  "key3" -> "value3",
                  "key4" -> "value4"
                }
                """
            }
        }

        "objects" - {
            "single object as value" - {
                prettyPrint(
                    mapOf(
                        "key1" to SmallObject("field", 1)
                    )
                ) mapTo """
                {
                  "key1" -> SmallObject(
                    field1 = "field"
                    field2 = 1
                  )
                }
                """
            }
            "single object as key" - {
                prettyPrint(
                    mapOf(
                        SmallObject("field", 1) to "value1"
                    )
                ) mapTo """
                {
                  SmallObject(
                    field1 = "field"
                    field2 = 1
                  ) -> "value1"
                }
                """
            }
            "multiple objects" - {
                prettyPrint(
                    mapOf(
                        "key1" to SmallObject("field", 1),
                        "key2" to SmallObject("field2", 2)
                    )
                ) mapTo """
                {
                  "key1" -> SmallObject(
                    field1 = "field"
                    field2 = 1
                  ),
                  "key2" -> SmallObject(
                    field1 = "field2"
                    field2 = 2
                  )
                }
                """
            }
            "map of lists" - {
                prettyPrint(
                    mapOf(
                        listOf(1, 2, 3) to listOf(4, 5, 6),
                        listOf(7, 8, 9) to listOf(0, 1, 2)
                    )
                ) mapTo """
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
            "map of maps" - {
                prettyPrint(
                    mapOf(
                        mapOf(1 to 2, 2 to 3) to mapOf(3 to 4, 4 to 5),
                        mapOf(6 to 7, 7 to 8) to mapOf(8 to 9, 9 to 0)
                    )
                ) mapTo """
                {
                  {
                    1 -> 2,
                    2 -> 3
                  } -> {
                    3 -> 4,
                    4 -> 5
                  },
                  {
                    6 -> 7,
                    7 -> 8
                  } -> {
                    8 -> 9,
                    9 -> 0
                  }
                }
                """
            }
        }
        "null stuff" - {
            "null keys" - {
                prettyPrint(
                    mapOf(
                        null to SmallObject("field", 1)
                    )
                ) mapTo """
                {
                  null -> SmallObject(
                    field1 = "field"
                    field2 = 1
                  )
                }
                """
            }
            "null values" - {
                prettyPrint(
                    mapOf(
                        "key1" to null
                    )
                ) mapTo """
                {
                  "key1" -> null
                }
                """
            }
            "multiple null values" - {
                prettyPrint(
                    mapOf(
                        "key1" to null,
                        "key2" to null
                    )
                ) mapTo """
                {
                  "key1" -> null,
                  "key2" -> null
                }
                """
            }
            "null key and null value" - {
                prettyPrint(
                    mapOf(
                        null to null
                    )
                ) mapTo """
                {
                  null -> null
                }
                """
            }
            "null key and null value 2" - {
                prettyPrint(
                    mapOf(
                        null to SmallObject("field", 1),
                        "key2" to null
                    )
                ) mapTo """
                {
                  null -> SmallObject(
                    field1 = "field"
                    field2 = 1
                  ),
                  "key2" -> null
                }
                """
            }
        }
    }
})
