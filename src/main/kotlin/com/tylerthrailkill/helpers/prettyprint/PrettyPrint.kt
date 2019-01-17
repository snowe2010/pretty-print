package com.tylerthrailkill.helpers.prettyprint

fun pp(obj: Any?) {
    obj?.javaClass?.fields?.forEach {
        println(it)
    }
}