package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object TabTest : Spek({
    setup()

    describe("pretty printing an object should") {
        it("render a list with 4 spaces for the tab size instead of 2") {
            prettyPrint(
                tabSize = 4,
                obj = listOf("a", "b", "c")
            ) mapsTo """
                [
                    "a",
                    "b",
                    "c"
                ]
                """
        }
    }
})
