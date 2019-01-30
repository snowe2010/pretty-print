package com.tylerthrailkill.helpers.prettyprint

// Simple objects

data class TinyObject(var int: Int)

data class SmallObject(val field1: String, var field2: Int)

// Nested objects

data class NestedSmallObject(val smallObject: SmallObject)

data class NestedLargeObject(
    val nestedSmallObject: NestedSmallObject,
    val smallObject: SmallObject,
    val testString: String,
    val bigObject: NestedLargeObject? = null
)

data class NestedObjectWithCollection(
    val coll: List<Any>
)

data class NullableLists(
    val col: List<Any?>?
)

// Massive Objects, with every type

