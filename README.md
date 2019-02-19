[![Build Status](https://travis-ci.com/snowe2010/pretty-print.svg?branch=master)](https://travis-ci.com/snowe2010/pretty-print)
[![Download](https://api.bintray.com/packages/snowe/maven/Pretty-Print/images/download.svg)](https://bintray.com/snowe/maven/Pretty-Print/_latestVersion)
[![codecov](https://codecov.io/gh/snowe2010/pretty-print/branch/master/graph/badge.svg)](https://codecov.io/gh/snowe2010/pretty-print)
 
# Pretty Print - pp

adds a `pp(Any?)` and `<T>.pp()` method to pretty print any java or kotlin object.

`pp(Any?)` takes any object and will print it in a pretty format.
`<T>pp()` can be called on any object inline and will allow you to pretty print inside of a method chain

# API

`pp(obj: Any?, tabSize: Int = 2, printStream: PrintStream = System.out, wrappedLineWidth: Int = 80)`

`tabSize` allows you to change the number of spaces used to indent each level of the output. Default is `2`
`printStream` allows you to change the `PrintStream` you are printing to. Default is `System.out`
`wrappedLineWidth` allows you to change how many characters are allowed before wrapping in a multiline string. Default is `80`
 
# Examples
### Main API
###### Top level method
```kotlin
data class TinyObject(var int: Int)
pp(TinyObject(1))
```
```text
TinyObject(
  int = 1
)
```
###### Inline method
```kotlin
data class TinyObject(var int: Int)
fun callSomething(obj: Any?) {
    println("inline wrapper function entered")
}
callSomething(TinyObject(1).pp())
```
```
TinyObject(
  int = 1
)
inline wrapper function entered
```

### Lists
###### List
```kotlin
pp(listOf("1", 2, 3.0, true))
```
```text
[
  "1",
  2,
  3.0,
  true
]
```
###### Object with list
```kotlin
data class OL(val list: List<String>)
pp(OL(listOf("1")))
```
```
OL(
  list = [
           "1"
         ]
)
```

###### map
```kotlin
pp(mapOf("key1" to "value1", "key2" to "value2"))
```
```
{
  "key1" -> "value1",
  "key2" -> "value2"
}
```
###### Object with map
```kotlin
data class OM(val map: Map<Any, Any>)
pp(OM(mapOf(1 to "value", "key" to 1)))
```
```text
OM(
  map = {
          1 -> "value",
          "key" -> 1
        }
)
```

###### multiline strings
```kotlin
pp("Goodbye, cruel world. Goodbye, cruel lamp.", wrappedLineWidth = 22)
```
```
"""
Goodbye, cruel world. 
Goodbye, cruel lamp.
"""
```

###### multiline strings with unicode line breaking
```kotlin
pp("Goodbye, cruel world. Good­bye, cruel lamp.", wrappedLineWidth = 27)
```
```
"""
Goodbye, cruel world. Good­
bye, cruel lamp.
"""
```
```kotlin
pp("❤️🥞❤️", wrappedLineWidth = 3)
```
```text
"""
❤️
🥞
❤️
"""
```

###### multiple fields
```kotlin
pp(SmallObject("Goodbye, cruel world. Goodbye, cruel lamp.", 1))
```
```
SmallObject(
  field1 = "Goodbye, cruel world. Goodbye, cruel lamp."
  field2 = 1
)
```

###### Cyclic references

```kotlin
data class O1(var c: O2? = null)
data class O2(var c: O1? = null)
val sco1 = O1()
val sco2 = O2(sco1)
sco1.c = sco2
pp(sco1)
```
```text
O1(
  c = O2(
    c = cyclic reference detected for 50699452
  )
)[$id=50699452]
```

# ToDo

* test nullability cases
* allow printing of `java*` classes
* fix unicode line breaking with certain characters only under icu4j library
