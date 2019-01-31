[![Build Status](https://travis-ci.com/snowe2010/pretty-print.svg?branch=master)](https://travis-ci.com/snowe2010/pretty-print)

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