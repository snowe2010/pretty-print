package com.tylerthrailkill.helpers.prettyprint

import io.kotest.core.spec.style.FreeSpec

class PojoTest : FreeSpec({
    "pretty print should print primitives correctly: " - {
        "string" - {
            prettyPrint("Goodbye, cruel world. Goodbye, cruel lamp.") mapTo """
                "Goodbye, cruel world. Goodbye, cruel lamp."
                """
        }
        "int" - {
            prettyPrint(100) mapTo "100"
        }
        "boolean" - {
            prettyPrint(true) mapTo "true"
        }
        "double" - {
            prettyPrint(1.0) mapTo "1.0"
        }
        "float" - {
            prettyPrint(100f) mapTo "100.0"
        }
        "char" - {
            prettyPrint('a') mapTo "a"
        }
    }
    "pretty print should print other objects correctly: " - {
        "string" - {
            prettyPrint("Goodbye, cruel world. Goodbye, cruel lamp.") mapTo """
                "Goodbye, cruel world. Goodbye, cruel lamp."
                """
        }
    }
})
