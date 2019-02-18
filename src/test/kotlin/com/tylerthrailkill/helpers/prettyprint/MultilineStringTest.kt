package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object MultilineStringTest : Spek({
    setup()

    describe("really long string reformatting") {
        context("when the long string is not part of another object") {
            it("renders as a multiline string") {
                val s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa a"
                prettyPrint(s) mapsTo """
                    ${"\"\"\""}
                    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
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
                           Yes, if you make it look like an electrical fire. When you do things right,
                           people won't be sure you've done anything at all. Too much work. Let's burn it
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
                      aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
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
                      aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
                      a
                      ${"\"\"\""} -> ${"\"\"\""}
                      bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb
                      b
                      ${"\"\"\""}
                    }
                    """
            }
        }
    }
})
