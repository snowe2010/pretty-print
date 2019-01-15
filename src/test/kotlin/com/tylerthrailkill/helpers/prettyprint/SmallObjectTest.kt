package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.Suite

//class SmallObjectTest {
//
//}

object MyTest: Spek({
    println("this is the root")
    group("some group") {
        println("some group")
        it("some test") {
            println("some test")
        }
    }

    group("another group") {
        println("another group")
        it("another test") {
            println("another test")
        }
    }
})