package com.tylerthrailkill.helpers.prettyprint

import com.beust.klaxon.Klaxon
import io.kotest.core.spec.style.FreeSpec

class NaughtyStringsTest : FreeSpec({
    "naughty strings should render correctly" - {
        val naughtyString = javaClass.getResource("/naughty_strings.json").readText()
        val naughtyList = Klaxon().parseArray<String>(naughtyString)!!
        "in list" - {
            val expectedString = javaClass.getResource("/naughty_string_printed.txt").readText()
            prettyPrint(naughtyList, wrappedLineWidth = 1000) mapTo expectedString // don't wrap in the naughty strings test, as it can't be generated then.
        }
    }
})
