package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec

class StaticFieldTest: FreeSpec({
    "pretty printing lists should" - {
        "render a list of strings" - {
            val staticFieldsObject = StaticFieldsObject.FOO
            prettyPrint(staticFieldsObject) mapTo """
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

