package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object NullabilityTest : Spek({
    setup()

//    describe("pretty printing lists should") {
//        it("render a list of strings") {
//            prettyPrint(Unit) mapsTo """
//                [
//                  "a",
//                  "b",
//                  "c"
//                ]
//                """
//        }
//    }
    describe("pretty printing") {
        it("null should work") {
            prettyPrint(null) mapsTo """
                null
                """
        }
        it("objects with no fields should work") {
            prettyPrint(EmptyObject()) mapsTo """
                EmptyObject(
                )
                """
        }
    }
})
