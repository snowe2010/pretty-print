package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object MultilineStringTest : Spek({
    setup()

    describe("really long strings should wrap to a nice format") {
        it("when there are plain spaces") {
            prettyPrint(SmallObject("Yes, if you make it look like an electrical fire. When you do things right, people won't be sure you've done anything at all. Too much work. Let's burn it and say we dumped it in the sewer. Goodbye, cruel world. Goodbye, cruel lamp.", 1)) mapsTo """
                SmallObject(
                  field1 = ""${'"'}
                           Yes, if you make it look like an electrical fire. When you do things right,
                           people won't be sure you've done anything at all. Too much work. Let's burn it
                           and say we dumped it in the sewer. Goodbye, cruel world. Goodbye, cruel lamp.
                           ""${'"'}
                  field2 = 1
                )
                """
        }
    }
})
