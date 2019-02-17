package com.tylerthrailkill.helpers.prettyprint

import com.beust.klaxon.Klaxon
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.style.specification.describe

object NaughtyStringsTest : Spek({
    setup()

    describe(
        "naughty strings should render correctly",
        Skip.Yes("naughty_string_printed.txt must be updated to expect multiline strings if the original string was too long")
    ) {
        val naughtyString = javaClass.getResource("/naughty_strings.json").readText()
        val naughtyList = Klaxon().parseArray<String>(naughtyString)!!
        it("in list") {
            val expectedString = javaClass.getResource("/naughty_string_printed.txt").readText()
            prettyPrint(naughtyList) mapsTo expectedString
        }
    }
})
