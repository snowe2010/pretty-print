package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

// TODO add these tests back when you figure out why https://stackoverflow.com/questions/54756894/breakiterator-failing-on-unicode-ucd-linebreaktest
// is happening
val testsToSkipCurrently = listOf(
        "  × [0.3] HYPHEN-MINUS (HY) ÷ [999.0] NUMBER SIGN (AL) ÷ [0.3]",
        "  × [0.3] HYPHEN-MINUS (HY) × [9.0] COMBINING DIAERESIS (CM1_CM) ÷ [999.0] NUMBER SIGN (AL) ÷ [0.3]",
        "  × [0.3] HYPHEN-MINUS (HY) ÷ [999.0] SECTION SIGN (AI_AL) ÷ [0.3]",
        "  × [0.3] HYPHEN-MINUS (HY) × [9.0] COMBINING DIAERESIS (CM1_CM) ÷ [999.0] SECTION SIGN (AI_AL) ÷ [0.3]",
        "  × [0.3] HYPHEN-MINUS (HY) ÷ [999.0] <reserved-50005> (XX_AL) ÷ [0.3]",
        "  × [0.3] HYPHEN-MINUS (HY) × [9.0] COMBINING DIAERESIS (CM1_CM) ÷ [999.0] <reserved-50005> (XX_AL) ÷ [0.3]",
        "  × [0.3] HYPHEN-MINUS (HY) ÷ [999.0] THAI CHARACTER KO KAI (SA_AL) ÷ [0.3]",
        "  × [0.3] HYPHEN-MINUS (HY) × [9.0] COMBINING DIAERESIS (CM1_CM) ÷ [999.0] THAI CHARACTER KO KAI (SA_AL) ÷ [0.3]"
)
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
        context("break should occur between ") {
            javaClass.getResource("/LineBreakTest.txt").readText().lines().forEach nextTest@{ testLine ->
                if (testLine.startsWith('#') or testLine.isBlank()) {
                    return@nextTest
                }
                val parts = mapUnicodeTestLineToParts(testLine)
                val padding = "                                       "
                val testName = testLine.split('#')[1]
                if (testsToSkipCurrently.contains(testName)) {
                    return@nextTest
                }
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
