package com.tylerthrailkill.helpers.prettyprint

import java.math.BigDecimal
import java.util.*

// Simple objects

class EmptyObject

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

data class MassiveObject(
    val astring: String,
    val listOfObject: MutableList<AValueObject> = mutableListOf()
)

data class AValueObject(
    val uuid: UUID? = null,
    val number: BigDecimal,
    val emailAddresses: MutableList<EmailAddress> = mutableListOf(),
    val nestedObjectsListToMap: List<Map<String, NestedLargeObject>>,
    val nestedObjectsMapToList: Map<String, List<NestedLargeObject>>
)

data class EmailAddress(
    val emailAddress: String
) {
    private val serialVersionUUID = 1L
}
