package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream

object CollectionsTest : Spek({
    val outContent by memoized { ByteArrayOutputStream() }
    val errContent by memoized { ByteArrayOutputStream() }
    setupStreams()

    describe("pretty printing lists should") {
        it("render a list of strings") {
            prettyPrint(listOf("a", "b", "c")) mapsTo """
                [
                  "a",
                  "b",
                  "c"
                ]
                """
        }
        it("render a list of mixed primitive objects") {
            prettyPrint(listOf("a", 1, true)) mapsTo """
                [
                  "a",
                  1,
                  true
                ]
                """
        }
        it("render null lists as null") {
            prettyPrint(NullableLists(null)) mapsTo """
            NullableLists(
              col = null
            )
            """
        }
        it("render lists with null items") {
            prettyPrint(NullableLists(listOf(null, null, null))) mapsTo """
            NullableLists(
              col = [
                      null,
                      null,
                      null
                    ]
            )
            """
        }
        it("render lists with mixed null and not-null items") {
            prettyPrint(NullableLists(listOf(null, 1, null, "a", null, true))) mapsTo """
            NullableLists(
              col = [
                      null,
                      1,
                      null,
                      "a",
                      null,
                      true
                    ]
            )
            """
        }
        it("render lists with a small object") {
            prettyPrint(NullableLists(listOf(TinyObject(1)))) mapsTo """
            NullableLists(
              col = [
                      TinyObject(
                        int = 1
                      )
                    ]
            )
            """
        }
        it("render lists with multiple small objects") {
            prettyPrint(
                NullableLists(
                    listOf(
                        TinyObject(1),
                        TinyObject(2),
                        TinyObject(3)
                    )
                )
            ) mapsTo """
            NullableLists(
              col = [
                      TinyObject(
                        int = 1
                      ),
                      TinyObject(
                        int = 2
                      ),
                      TinyObject(
                        int = 3
                      )
                    ]
            )
            """
        }
    }
})
