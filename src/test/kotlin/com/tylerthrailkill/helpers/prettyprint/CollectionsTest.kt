package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream

object CollectionsTest : Spek({
    val outContent by memoized { ByteArrayOutputStream() }
    val errContent by memoized { ByteArrayOutputStream() }
    setupStreams()

    describe("pretty printing lists should") {
        it("render a list of objects") {
            prettyPrint(listOf("a", "b", "c")) mapsTo """
                [
                  "a",
                  "b",
                  "c"
                ]
                """
        }
        it("render a list of mixed objects") {
            prettyPrint(listOf("a", 1, true)) mapsTo """
                [
                  "a",
                  1,
                  true
                ]
                """
        }
    }
})
