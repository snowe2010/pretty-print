package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object MultilineStringTest : Spek({
    setup()

    describe("really long strings should wrap to a nice format") {
        it("when there are plain spaces") {
            // @formatter:off
            prettyPrint(
                    wrappedLineWidth = 22,
                    obj = SmallObject("Goodbye, cruel world. Goodbye, cruel lamp.", 1)
            ) mapsTo """
                SmallObject(
                  field1 = ""${'"'}
                           Goodbye, cruel world.
                           Goodbye, cruel lamp.
                           ""${'"'}
                  field2 = 1
                )
                """
            // @formatter:on
        }
        it("TEST [#] and [—]") {
            val parts = listOf(listOf('#'), listOf('—'))
            val padding = "                                       "
            logger.info { "TEST [#] and [—]" }
            logger.debug { parts }
            prettyPrint(wrappedLineWidth = 1,
                    obj = LongString(parts.flatten().joinToString(""))
            ) mapsTo """
                        LongString(
                          longString = ""${'"'}
                                       ${parts.joinToString("\n$padding") { it.joinToString("") }}
                                       ""${'"'}
                        )
                        """
        }
        context("break should occur between ") {
            javaClass.getResource("/LineBreakTest.txt").readText().lines().forEach nextTest@{ testLine ->
                if (testLine.startsWith('#') or testLine.isBlank()) {
                    return@nextTest
                }
                val parts = mapUnicodeTestLineToParts(testLine)
                val padding = "                                       "
                val testName = parts.joinToString(separator = " and ")
                it(testName) {
                    logger.info { testName }
                    logger.debug { parts }
                    prettyPrint(wrappedLineWidth = 1,
                            obj = LongString(parts.flatten().joinToString(""))
                    ) mapsTo """
                        LongString(
                          longString = ""${'"'}
                                       ${parts.joinToString("\n$padding") { it.joinToString("") }}
                                       ""${'"'}
                        )
                        """
                }
            }
        }
        it("when there are unicode breaking space characters at the break point") {
            prettyPrint(
                    wrappedLineWidth = 22,
                    obj = SmallObject("Goodbye, cruel world.\u1680Goodbye, cruel lamp.", 1)
            ) mapsTo """
                SmallObject(
                  field1 = ""${'"'}
                           Goodbye, cruel world. 
                           Goodbye, cruel lamp.
                           ""${'"'}
                  field2 = 1
                )
                """
        }
    }
})

private fun mapUnicodeTestLineToParts(testLine: String): List<List<String>> {
    return testLine.replaceAfter('	', "")
            .replace("	", "")
            .replace(" ", "")
            .removeSurrounding("×", "÷")
            .split('÷')
            .map { part -> part.split('×') }
            .map { mapOfCodePoints ->
                mapOfCodePoints.map { codePoint ->
                    Integer.parseInt(codePoint, 16) // parse int as hexadecimal
                }
            }
            .map { breakParts ->
                breakParts.map { codePoint ->
                    StringBuilder().appendCodePoint(codePoint).toString()
                }
            }
}
