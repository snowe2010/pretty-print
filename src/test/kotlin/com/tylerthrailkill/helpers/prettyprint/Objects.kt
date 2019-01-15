package com.tylerthrailkill.helpers.prettyprint

// Simple objects

data class SmallObject(val field1: String, var field2: Int)

// Nested objects

data class NestedSmallObject(val smallObject: SmallObject)