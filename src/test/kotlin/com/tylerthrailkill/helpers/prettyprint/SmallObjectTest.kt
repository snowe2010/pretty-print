package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec

class SmallObjectTest : FreeSpec({
    "tiny object should" - {
        "render" - {
            "a single field " - {
                prettyPrint(TinyObject(1)) mapsTo """
                TinyObject(
                  int = 1
                )
                """
            }
        }
    }
    "small object should" - {
        "render " - {
            "two fields" - {
                prettyPrint(SmallObject("a", 1)) mapsTo """
                SmallObject(
                  field1 = "a"
                  field2 = 1
                )
            """
            }
        }
    }
})
