package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream

object CollectionsTest : Spek({
    setup()

    describe("pretty printing array should") {
        it("render an array") {
            prettyPrint(Array(1){ 0 }) mapsTo """
                [
                  0
                ]
                """
        }
        it("render an array of array") {
            prettyPrint(Array(1){Array(1) { 0 } }) mapsTo """
                [
                  [
                    0
                  ]
                ]
                """
        }
    }

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
        it("lists with maps") {
            prettyPrint(
                NullableLists(
                    listOf(
                        mapOf(1 to 2, 3 to 4),
                        mapOf(5 to 6, 7 to 8),
                        mapOf(9 to 0, 0 to 1)
                    )
                )
            ) mapsTo """
            NullableLists(
              col = [
                      {
                        1 -> 2,
                        3 -> 4
                      },
                      {
                        5 -> 6,
                        7 -> 8
                      },
                      {
                        9 -> 0,
                        0 -> 1
                      }
                    ]
            )
            """
        }
    }
})
