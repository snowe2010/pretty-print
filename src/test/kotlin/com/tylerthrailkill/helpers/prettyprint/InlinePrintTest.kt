package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object InlinePrintTest : Spek({
    setup()

    describe("pretty printing inline should") {
        it("print a test string before pretty printing") {
            prettyPrintInline(TinyObject(1)) mapsTo """
                TinyObject(
                  int = 1
                )
                inline wrapper function entered
                """
        }
    }
})
