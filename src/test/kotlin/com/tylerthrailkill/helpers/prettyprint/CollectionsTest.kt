package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream

class CollectionsTest : FreeSpec({
    "pretty printing lists should" - {
        "render a list of strings" - {
            prettyPrint(listOf("a", "b", "c")) mapsTo """
                [
                  "a",
                  "b",
                  "c"
                ]
                """
        }
        "render a list of mixed primitive objects" - {
            prettyPrint(listOf("a", 1, true)) mapsTo """
                [
                  "a",
                  1,
                  true
                ]
                """
        }

        "render null lists as null" - {
            prettyPrint(NullableLists(null)) mapsTo """
            NullableLists(
              col = null
            )
            """
        }
        "render lists with null items" - {
            prettyPrint(NullableLists(listOf(null, null, null))) mapsTo """
            NullableLists(
              col = [
                      null,
                      null,
                      null
                    ]
            )
            """
        }
        "render lists with mixed null and not-null items" - {
            prettyPrint(NullableLists(listOf(null, 1, null, "a", null, true))) mapsTo """
            NullableLists(
              col = [
                      null,
                      1,
                      null,
                      "a",
                      null,
                      true
                    ]
            )
            """
        }
        "render lists with a small object" - {
            prettyPrint(NullableLists(listOf(TinyObject(1)))) mapsTo """
            NullableLists(
              col = [
                      TinyObject(
                        int = 1
                      )
                    ]
            )
            """
        }
        "render lists with multiple small objects" - {
            prettyPrint(
                NullableLists(
                    listOf(
                        TinyObject(1),
                        TinyObject(2),
                        TinyObject(3)
                    )
                )
            ) mapsTo """
            NullableLists(
              col = [
                      TinyObject(
                        int = 1
                      ),
                      TinyObject(
                        int = 2
                      ),
                      TinyObject(
                        int = 3
                      )
                    ]
            )
            """
        }
        "lists with maps" - {
            prettyPrint(
                NullableLists(
                    listOf(
                        mapOf(1 to 2, 3 to 4),
                        mapOf(5 to 6, 7 to 8),
                        mapOf(9 to 0, 0 to 1)
                    )
                )
            ) mapsTo """
            NullableLists(
              col = [
                      {
                        1 -> 2,
                        3 -> 4
                      },
                      {
                        5 -> 6,
                        7 -> 8
                      },
                      {
                        9 -> 0,
                        0 -> 1
                      }
                    ]
            )
            """
        }

    }
})
