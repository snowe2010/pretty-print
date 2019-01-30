package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream

object MassiveObjectTest : Spek({
    val outContent by memoized { ByteArrayOutputStream() }
    val errContent by memoized { ByteArrayOutputStream() }
    setupStreams()

    describe("massive objects") {
        it("should render") {
//            prettyPrint(
//
//            ) mapsTo """
//                TinyObject(
//                  int = 1
//                )
//                """
        }
    }
})
