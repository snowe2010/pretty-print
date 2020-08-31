package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec

class PojoTest : FreeSpec({
    "pretty print should print primitives correctly: " - {
        "string" - {
            prettyPrint("Goodbye, cruel world. Goodbye, cruel lamp.") mapsTo """
                "Goodbye, cruel world. Goodbye, cruel lamp."
                """
        }
        "int" - {
            prettyPrint(100) mapsTo "100"
        }
        "boolean" - {
            prettyPrint(true) mapsTo "true"
        }
        "double" - {
            prettyPrint(1.0) mapsTo "1.0"
        }
        "float" - {
            prettyPrint(100f) mapsTo "100.0"
        }
        "char" - {
            prettyPrint('a') mapsTo "a"
        }
    }
    "pretty print should print other objects correctly: " - {
        "string" - {
            prettyPrint("Goodbye, cruel world. Goodbye, cruel lamp.") mapsTo """
                "Goodbye, cruel world. Goodbye, cruel lamp."
                """
        }
    }
})
