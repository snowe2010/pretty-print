[![Build Status](https://travis-ci.com/snowe2010/pretty-print.svg?branch=master)](https://travis-ci.com/snowe2010/pretty-print)
[![Download](https://img.shields.io/bintray/v/snowe/maven/Pretty-Print.svg?label=bintray&style=flat)](https://bintray.com/snowe/maven/Pretty-Print/)
[![codecov](https://codecov.io/gh/snowe2010/pretty-print/branch/master/graph/badge.svg)](https://codecov.io/gh/snowe2010/pretty-print)
 
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

* implement cycle detection
* test nullability cases (requires cycle detection)
* allow printing of `java*` classes (requires cycle detection)
