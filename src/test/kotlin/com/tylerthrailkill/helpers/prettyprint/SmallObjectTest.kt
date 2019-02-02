package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream

object SmallObjectTest : Spek({
    setup()

    describe("tiny object should") {
        context("render") {
            it("a single field ") {
                prettyPrint(TinyObject(1)) mapsTo """
                TinyObject(
                  int = 1
                )
                """
            }
        }
    }
    describe("small object should") {
        context("render ") {
            it("two fields") {
                prettyPrint(SmallObject("a", 1)) mapsTo """
                SmallObject(
                  field1 = a
                  field2 = 1
                )
            """
            }
        }
    }
})
