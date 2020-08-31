package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec

class EnumTest : FreeSpec({
    "pretty printing enums should" - {
        "render all values of the enum" - {
            prettyPrint(TestEnum.BOOLEAN) mapsTo """TestEnum.BOOLEAN"""
        }
        "render enum in a list" - {
            prettyPrint(listOf(TestEnum.BOOLEAN)) mapsTo """
                [
                  TestEnum.BOOLEAN
                ]
                """.trimIndent()
        }
        "render enum as key in a map" - {
            prettyPrint(mapOf(TestEnum.BOOLEAN to "")) mapsTo """
                {
                  TestEnum.BOOLEAN -> ""
                }
                """
        }
        "render enum as value in a map" - {
            prettyPrint(mapOf("" to TestEnum.BOOLEAN)) mapsTo """
                {
                  "" -> TestEnum.BOOLEAN
                }
                """
        }
        "render enum in an object" - {
            prettyPrint(ObjectWithEnum(TestEnum.BOOLEAN)) mapsTo """
                ObjectWithEnum(
                  enum = TestEnum.BOOLEAN
                )
                """
        }
    }
})
