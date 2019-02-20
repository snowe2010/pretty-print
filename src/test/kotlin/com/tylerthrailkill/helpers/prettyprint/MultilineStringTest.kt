//@formatter:off
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

const val SPACE = ' '

object MultilineStringTest : Spek({
    setup()

    describe("really long string reformatting") {
        it("when there are plain spaces") {
            prettyPrint(
                wrappedLineWidth = 22,
                obj = SmallObject("Goodbye, cruel world. Goodbye, cruel lamp.", 1)
            ) mapsTo """
            SmallObject(
              field1 = ""${'"'}
                       Goodbye, cruel world.$SPACE
                       Goodbye, cruel lamp.
                       ""${'"'}
              field2 = 1
            )
            """
        }

        context("when the long string is not part of another object") {
            it("renders as a multiline string") {
                val s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa a"
                prettyPrint(s) mapsTo """
                    ${"\"\"\""}
                    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa$SPACE
                    a
                    ${"\"\"\""}
                    """
            }
        }

        context("when the long string is the value of a field") {
            it("renders as a multiline string") {
                prettyPrint(
                    SmallObject(
                        "Yes, if you make it look like an electrical fire. When you do things right, people won't be sure you've done anything at all. Too much work. Let's burn it and say we dumped it in the sewer. Goodbye, cruel world. Goodbye, cruel lamp.",
                        1
                    )
                ) mapsTo """
                SmallObject(
                  field1 = ${"\"\"\""}
                           Yes, if you make it look like an electrical fire. When you do things right,$SPACE
                           people won't be sure you've done anything at all. Too much work. Let's burn it$SPACE
                           and say we dumped it in the sewer. Goodbye, cruel world. Goodbye, cruel lamp.
                           ${"\"\"\""}
                  field2 = 1
                )
                """
            }
        }

        context("when the long string is a member of a list") {
            it("renders as a multiline string") {
                prettyPrint(
                    listOf(
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa a"
                    )
                ) mapsTo """
                    [
                      ${"\"\"\""}
                      aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa$SPACE
                      a
                      ${"\"\"\""}
                    ]
                    """
            }
        }

        context("when the long string is a key or value in a map") {
            it("renders as a multiline string") {
                prettyPrint(
                    mapOf(
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa a"
                                to "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb b"
                    )
                ) mapsTo """
                    {
                      ${"\"\"\""}
                      aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa$SPACE
                      a
                      ${"\"\"\""} -> ${"\"\"\""}
                      bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb$SPACE
                      b
                      ${"\"\"\""}
                    }
                    """
            }
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
                    prettyPrint(
                        wrappedLineWidth = 1,
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
        context("in a list") {
            it("with plain spaces") {
                prettyPrint(
                    wrappedLineWidth = 22,
                    obj = listOf("Goodbye, cruel world. Goodbye, cruel lamp.")
                ) mapsTo """
                [
                  ""${'"'}
                  Goodbye, cruel world. 
                  Goodbye, cruel lamp.
                  ""${'"'}
                ]
                """
            }
        }
        context("in a map") {
            it("as a value with plain spaces") {
                prettyPrint(
                    wrappedLineWidth = 22,
                    obj = mapOf(1 to "Goodbye, cruel world. Goodbye, cruel lamp.")
                ) mapsTo """
                {
                  1 -> ""${'"'}
                  Goodbye, cruel world. 
                  Goodbye, cruel lamp.
                  ""${'"'}
                }
                """
            }
            it("as a key with plain spaces") {
                prettyPrint(
                    wrappedLineWidth = 22,
                    obj = mapOf("Goodbye, cruel world. Goodbye, cruel lamp." to 1)
                ) mapsTo """
                {
                  ""${'"'}
                  Goodbye, cruel world. 
                  Goodbye, cruel lamp.
                  ""${'"'} -> 1
                }
                """
            }
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
