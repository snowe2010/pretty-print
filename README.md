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

<table>
    <tr>
        <th>Code</th>
        <th>Pretty Print Output</th>
    </tr>
    <tr>
        <td>
            <div class="highlight highlight-source-kotlin">
                <pre>pp(
  NestedObjectWithCollection(
    listOf(NestedSmallObject(SmallObject("a", 1)))
  )
)</pre>
            </div>
        </td>
        <td>
            <pre>NestedObjectWithCollection(
  coll = [
           NestedSmallObject(
             smallObject = SmallObject(
               field1 = a
               field2 = 1
             )
           )
         ]
)</pre>
        </td>
     </tr>
    <tr>
        <td>
            <div class="highlight highlight-source-kotlin">
                <pre>fun callSomething(obj: Any?) {
    println("inline wrapper function entered")
}
callSomething(TinyObject(1).pp())</pre>
            </div>
        </td>
        <td>
            <pre>TinyObject(
  int = 1
)
inline wrapper function entered</pre>
        </td>
     </tr>
    <tr>
        <td>
            <div class="highlight highlight-source-kotlin">
                <pre>pp(
    mapOf(
        "key1" to "value1",
        "key2" to "value2",
        "key3" to "value3",
        "key4" to "value4"
    )
)</pre>
            </div>
        </td>
        <td>
            <pre>{
  "key1" -> "value1",
  "key2" -> "value2",
  "key3" -> "value3",
  "key4" -> "value4"
}</pre>
        </td>
    </tr>
    <tr>
        <td>
            <div class="highlight highlight-source-kotlin">
                <pre>pp("Goodbye, cruel world. Goodbye, cruel lamp.", wrappedLineWidth = 22)</pre>
            </div>
        </td>
        <td>
            <pre>"""
Goodbye, cruel world. 
Goodbye, cruel lamp.
"""</pre>
        </td>
    </tr>
</table>


```kotlin
pp(SmallObject("Goodbye, cruel world. Goodbye, cruel lamp.", 1))
```
prints

```
SmallObject(
  field1 = """
           Goodbye, cruel world. 
           Goodbye, cruel lamp.
           """
  field2 = 1
)
```

### Cyclic references

```kotlin
data class SmallCyclicalObject1(
    val c: SmallCyclicalObject2? = null
)
data class SmallCyclicalObject2(
    val c: SmallCyclicalObject1? = null
)
val sco1 = SmallCyclicalObject1()
val sco2 = SmallCyclicalObject2(sco1)
sco1.c = sco2
pp(sco1)
```
prints
```text
ObjectWithMap(
  map = {
          1 -> ObjectContainingObjectWithMap(
            objectWithMap = cyclic reference detected for 775386112
          )
        }
)[$id=775386112]
```

# ToDo

* test nullability cases
* allow printing of `java*` classes
* fix unicode line breaking with certain characters only under icu4j library
