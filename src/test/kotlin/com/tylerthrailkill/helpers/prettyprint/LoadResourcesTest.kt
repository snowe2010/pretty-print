package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.*

object LoadResourcesTest : Spek({
    describe("can't load ") {
        it("resources") {
            val naughtyFileString = javaClass.getResource("naughty_strings.txt").readText()
            println(naughtyFileString)
        }
    }
})
