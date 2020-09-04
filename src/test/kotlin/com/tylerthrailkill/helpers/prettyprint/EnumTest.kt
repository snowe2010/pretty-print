package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec

class EnumTest : FreeSpec({
    "pretty printing enums should" - {
        "render all values of the enum" - {
            prettyPrint(TestEnum.BOOLEAN) mapTo """TestEnum.BOOLEAN"""
        }
        "render enum in a list" - {
            prettyPrint(listOf(TestEnum.BOOLEAN)) mapTo """
                [
                  TestEnum.BOOLEAN
                ]
                """.trimIndent()
        }
        "render enum as key in a map" - {
            prettyPrint(mapOf(TestEnum.BOOLEAN to "")) mapTo """
                {
                  TestEnum.BOOLEAN -> ""
                }
                """
        }
        "render enum as value in a map" - {
            prettyPrint(mapOf("" to TestEnum.BOOLEAN)) mapTo """
                {
                  "" -> TestEnum.BOOLEAN
                }
                """
        }
        "render enum in an object" - {
            prettyPrint(ObjectWithEnum(TestEnum.BOOLEAN)) mapTo """
                ObjectWithEnum(
                  enum = TestEnum.BOOLEAN
                )
                """
        }
    }
})
