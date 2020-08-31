package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec

class NullabilityTest : FreeSpec({
//    setup()

//    "pretty printing lists should" - {
//        "render a list of strings" - {
//            prettyPrint(Unit) mapsTo """
//                [
//                  "a",
//                  "b",
//                  "c"
//                ]
//                """
//        }
//    }
    "pretty printing" - {
        "null should work" - {
            prettyPrint(null) mapsTo """
                null
                """
        }
        "objects with no fields should work" - {
            prettyPrint(EmptyObject()) mapsTo """
                EmptyObject(
                )
                """
        }
    }
})
