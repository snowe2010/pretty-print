package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertFalse

object DeepObjectNestingTest : Spek({
    setup()

    describe(
        "pretty printing an object that has deep nesting",
        Skip.Yes("pretty print still implemented with mutual recursion")
    ) {
        it("should not cause a stack overflow when printing") {
            var obj = SinglyLinkedIntList(1, null)
            for (i in 1..1100) obj = SinglyLinkedIntList(1, obj)

            var didStackOverflow = false
            try {
                prettyPrint(obj)
            } catch (e: StackOverflowError) {
                didStackOverflow = true
            }

            assertFalse(didStackOverflow)
        }
    }
})
