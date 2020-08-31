package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class StaticFieldTest: DescribeSpec({
    describe("pretty printing lists should") {
        it("render a list of strings") {
            val staticFieldsObject = StaticFieldsObject.FOO
            prettyPrint(staticFieldsObject) mapsTo """
            StaticFieldsObject(
              FOO = StaticFieldsObject.<static cyclic class reference>
              BAR = StaticFieldsObject.<static cyclic class reference>
              BAZ = StaticFieldsObject.<static cyclic class reference>
              BIZ = StaticFieldsObject.<static cyclic class reference>
              BOOZE = StaticFieldsObject.<static cyclic class reference>
              n = 0
            )
            """.trimIndent()
        }
    }
})

