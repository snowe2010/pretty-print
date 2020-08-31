package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec

class InlinePrintTest : FreeSpec({
    "pretty printing inline should" - {
        "print a test string before pretty printing" - {
            prettyPrintInline(TinyObject(1)) mapsTo """
                TinyObject(
                  int = 1
                )
                inline wrapper function entered
                """
        }
    }
})
