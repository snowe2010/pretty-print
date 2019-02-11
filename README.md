[![Build Status](https://travis-ci.com/snowe2010/pretty-print.svg?branch=master)](https://travis-ci.com/snowe2010/pretty-print)
[![Download](https://img.shields.io/bintray/v/snowe/maven/Pretty-Print.svg?label=bintray&style=flat)](https://bintray.com/snowe/maven/Pretty-Print/)
 
 
# Pretty Print - pp

adds a single method `pp` to pretty print any java or kotlin object. 

# Examples

```kotlin
pp(
    NestedObjectWithCollection(
        listOf(NestedSmallObject(SmallObject("a", 1)))
    )
)
```
prints

```
NestedObjectWithCollection(
  coll = [
           NestedSmallObject(
             smallObject = SmallObject(
               field1 = a
               field2 = 1
             )
           )
         ]
)
```
