package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object PojoTest : Spek({
    setup()

    describe("pretty print should print primitives correctly: ") {
        it("string") {
            prettyPrint("Goodbye, cruel world. Goodbye, cruel lamp.") mapsTo """
                "Goodbye, cruel world. Goodbye, cruel lamp."
                """
        }
        it("int") {
            prettyPrint(100) mapsTo "100"
        }
        it("boolean") {
            prettyPrint(true) mapsTo "true"
        }
        it("double") {
            prettyPrint(1.0) mapsTo "1.0"
        }
        it("float") {
            prettyPrint(100f) mapsTo "100.0"
        }
        it("char") {
            prettyPrint('a') mapsTo "a"
        }
    }
    describe("pretty print should print other objects correctly: ") {
        it("string") {
            prettyPrint("Goodbye, cruel world. Goodbye, cruel lamp.") mapsTo """
                "Goodbye, cruel world. Goodbye, cruel lamp."
                """
        }
    }
})
