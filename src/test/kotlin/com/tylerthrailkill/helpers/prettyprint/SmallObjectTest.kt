package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek

object SmallObjectTest: Spek({
    val smallObject = SmallObject("field1", 2)

    group("small objects should") {
        test("render all fields nested one level") {
            pp(smallObject)
        }
    }
})
