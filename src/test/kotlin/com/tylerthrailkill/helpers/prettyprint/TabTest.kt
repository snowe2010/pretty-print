package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec

class TabTest : FreeSpec({

    "pretty printing an object should" - {
        "render a list with 4 spaces for the tab size instead of 2" - {
            prettyPrint(
                tabSize = 4,
                obj = listOf("a", "b", "c")
            ) mapTo """
                [
                    "a",
                    "b",
                    "c"
                ]
                """
        }
        "render a list of lists with 4 spaces for the tab size instead of 2" - {
            prettyPrint(
                tabSize = 4,
                obj = listOf(listOf("a", "b", "c"), listOf("a", "b", "c"), listOf("a", "b", "c"))
            ) mapTo """
                [
                    [
                        "a",
                        "b",
                        "c"
                    ],
                    [
                        "a",
                        "b",
                        "c"
                    ],
                    [
                        "a",
                        "b",
                        "c"
                    ]
                ]
                """
        }
        "render a map with 4 spaces for the tab size instead of 2" - {
            prettyPrint(
                tabSize = 4,
                obj = mapOf(mapOf(1 to 2, 2 to 3) to mapOf(3 to 4, 4 to 5))
            ) mapTo """
                {
                    {
                        1 -> 2,
                        2 -> 3
                    } -> {
                        3 -> 4,
                        4 -> 5
                    }
                }
                """
        }
    }
})
