package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object EnumTest : Spek({
    setup()
    
    describe("pretty printing enums should") {
        it("render all values of the enum") {
            prettyPrint(TestEnum.BOOLEAN) mapsTo """TestEnum.BOOLEAN"""
        }
        it("render enum in a list") {
            prettyPrint(listOf(TestEnum.BOOLEAN)) mapsTo """
                [
                  TestEnum.BOOLEAN
                ]
                """.trimIndent()
        }
        it("render enum as key in a map") {
            prettyPrint(mapOf(TestEnum.BOOLEAN to "")) mapsTo """
                {
                  TestEnum.BOOLEAN -> ""
                }
                """
        }
        it("render enum as value in a map") {
            prettyPrint(mapOf("" to TestEnum.BOOLEAN)) mapsTo """
                {
                  "" -> TestEnum.BOOLEAN
                }
                """
        }
        it("render enum in an object") {
            prettyPrint(ObjectWithEnum(TestEnum.BOOLEAN)) mapsTo """
                ObjectWithEnum(
                  enum = TestEnum.BOOLEAN
                )
                """
        }
    }
})
