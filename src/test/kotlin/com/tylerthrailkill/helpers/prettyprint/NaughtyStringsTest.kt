package com.tylerthrailkill.helpers.prettyprint

import com.beust.klaxon.Klaxon
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.*

object NaughtyStringsTest : Spek({
    setup()

    describe("naughty strings should render correctly") {
            val naughtyString = javaClass.getResource("/naughty_strings.json").readText()
            val naughtyList = Klaxon().parseArray<String>(naughtyString)!!
        it("in list") {
            val expectedString = javaClass.getResource("/naughty_string_printed.txt").readText()
            prettyPrint(naughtyList, wrappedLineWidth = 1000) mapsTo expectedString // don't wrap in the naughty strings test, as it can't be generated then.
        }
    }
})
