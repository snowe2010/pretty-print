package com.tylerthrailkill.helpers.prettyprint

fun pp(obj: Any?) {
    println("starting pretty print")
    println(obj.toString())
    obj?.javaClass?.declaredFields?.forEach {
        it.isAccessible = true
        println("${it.name} = ${it.get(obj)}")
    }
}