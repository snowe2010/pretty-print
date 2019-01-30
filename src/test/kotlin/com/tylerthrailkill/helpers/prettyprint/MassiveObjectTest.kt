package com.tylerthrailkill.helpers.prettyprint

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.util.*

object MassiveObjectTest : Spek({
    val outContent by memoized { ByteArrayOutputStream() }
    val errContent by memoized { ByteArrayOutputStream() }
    setupStreams()

    describe("massive objects") {
        val smallObject = SmallObject("a string in small object", 777)
        val nestedLargeObjectNull = NestedLargeObject(
            NestedSmallObject(smallObject),
            smallObject,
            "test string, please don't break",
            null
        )
        val nestedLargeObject = NestedLargeObject(
            NestedSmallObject(smallObject),
            smallObject,
            "test string, please don't break",
            NestedLargeObject(
                NestedSmallObject(smallObject),
                smallObject,
                "test string, please don't break",
                nestedLargeObjectNull
            )
        )
        val emailList = mutableListOf(
            EmailAddress("a@b.com"),
            EmailAddress("\uD83C\uDF83@zack.is"),
            EmailAddress("ñoñó1234@server.com"),
            EmailAddress("δοκιμή@παράδειγμα.δοκιμή"),
            EmailAddress("我買@屋企.香港"),
            EmailAddress("二ノ宮@黒川.日本"),
            EmailAddress("чебурашка@ящик-с-апельсинами.рф"),
            EmailAddress("संपर्क@डाटामेल.भारत"),
            EmailAddress("simple@example.com"),
            EmailAddress("very.common@example.com"),
            EmailAddress("disposable.style.email.with+symbol@example.com"),
            EmailAddress("other.email-with-hyphen@example.com"),
            EmailAddress("fully-qualified-domain@example.com"),
            EmailAddress("user.name+tag+sorting@example.com"),
            EmailAddress("x@example.com"),
            EmailAddress("example-indeed@strange-example.com"),
            EmailAddress("admin@mailserver1"),
            EmailAddress("example@s.example"),
            EmailAddress("\" \"@example.org"),
            EmailAddress("\"john..doe\"@example.org")
        )
        it("should render") {
            // DON'T INDENT THIS STRING. IT'S TOO LARGE FOR THE JVM 😂😂😂
            prettyPrint(
                MassiveObject(
                    "a string",
                    mutableListOf(
                        AValueObject(
                            UUID.fromString("b2558c12-d4d3-4abd-94e4-8600902f1edf"),
                            BigDecimal.ONE,
                            emailList,
                            listOf(
                                mapOf(
                                    "a" to nestedLargeObject,
                                    "b" to nestedLargeObject,
                                    "c" to nestedLargeObject
                                ),
                                mapOf(
                                    "d" to nestedLargeObject,
                                    "e" to nestedLargeObject,
                                    "f" to nestedLargeObject
                                ),
                                mapOf(
                                    "g" to nestedLargeObject,
                                    "h" to nestedLargeObject,
                                    "i" to nestedLargeObject
                                )
                            ),
                            mapOf(
                                "listA" to listOf(
                                    nestedLargeObject,
                                    nestedLargeObject,
                                    nestedLargeObject
                                ),
                                "listB" to listOf(
                                    nestedLargeObject,
                                    nestedLargeObject,
                                    nestedLargeObject
                                ),
                                "listC" to listOf(
                                    nestedLargeObject,
                                    nestedLargeObject,
                                    nestedLargeObject
                                )
                            )
                        )
                    )
                )
            ) mapsTo """
MassiveObject(
  astring = a string
  listOfObject = [
                   AValueObject(
                     uuid = b2558c12-d4d3-4abd-94e4-8600902f1edf
                     number = 1
                     emailAddresses = [
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = a@b.com
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = 🎃@zack.is
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = ñoñó1234@server.com
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = δοκιμή@παράδειγμα.δοκιμή
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = 我買@屋企.香港
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = 二ノ宮@黒川.日本
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = чебурашка@ящик-с-апельсинами.рф
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = संपर्क@डाटामेल.भारत
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = simple@example.com
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = very.common@example.com
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = disposable.style.email.with+symbol@example.com
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = other.email-with-hyphen@example.com
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = fully-qualified-domain@example.com
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = user.name+tag+sorting@example.com
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = x@example.com
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = example-indeed@strange-example.com
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = admin@mailserver1
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = example@s.example
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = " "@example.org
                                        ),
                                        EmailAddress(
                                          serialVersionUUID = 1
                                          emailAddress = "john..doe"@example.org
                                        )
                                      ]
                     nestedObjectsListToMap = [
                                                {
                                                  "a" -> NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  ),
                                                  "b" -> NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  ),
                                                  "c" -> NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  )
                                                },
                                                {
                                                  "d" -> NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  ),
                                                  "e" -> NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  ),
                                                  "f" -> NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  )
                                                },
                                                {
                                                  "g" -> NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  ),
                                                  "h" -> NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  ),
                                                  "i" -> NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  )
                                                }
                                              ]
                     nestedObjectsMapToList = {
                                                "listA" -> [
                                                  NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  ),
                                                  NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  ),
                                                  NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  )
                                                ],
                                                "listB" -> [
                                                  NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  ),
                                                  NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  ),
                                                  NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  )
                                                ],
                                                "listC" -> [
                                                  NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  ),
                                                  NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  ),
                                                  NestedLargeObject(
                                                    nestedSmallObject = NestedSmallObject(
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                    )
                                                    smallObject = SmallObject(
                                                      field1 = a string in small object
                                                      field2 = 777
                                                    )
                                                    testString = test string, please don't break
                                                    bigObject = NestedLargeObject(
                                                      nestedSmallObject = NestedSmallObject(
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                      )
                                                      smallObject = SmallObject(
                                                        field1 = a string in small object
                                                        field2 = 777
                                                      )
                                                      testString = test string, please don't break
                                                      bigObject = NestedLargeObject(
                                                        nestedSmallObject = NestedSmallObject(
                                                          smallObject = SmallObject(
                                                            field1 = a string in small object
                                                            field2 = 777
                                                          )
                                                        )
                                                        smallObject = SmallObject(
                                                          field1 = a string in small object
                                                          field2 = 777
                                                        )
                                                        testString = test string, please don't break
                                                        bigObject = null
                                                      )
                                                    )
                                                  )
                                                ]
                                              }
                   )
                 ]
)
                """
        }
    }
})
