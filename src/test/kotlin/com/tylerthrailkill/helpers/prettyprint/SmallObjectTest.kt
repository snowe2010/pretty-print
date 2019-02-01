package com.tylerthrailkill.helpers.prettyprint

import io.mockk.mockk
import io.mockk.verify
import mu.KotlinLogging
import org.hamcrest.CoreMatchers.startsWith
import org.spekframework.spek2.Spek
import java.io.PrintStream

object SmallObjectTest: Spek({
    val smallObject = SmallObject("field1", 2)
    val out = mockk<PrintStream>()

    group("small objects should") {
        System.setOut(out)
        test("render all fields nested one level") {
            pp(smallObject)
            verify { out.println(startsWith("Use:")) }
        }
    }
})
