# Filter
* Collection 에서 설정한 조건으로  item 을 필터링해주는 java 의 동작과 기본적으로 동일하나 몇가지 Kotlin 만의 메서드 지원
* filterIndexed()
  * item filtering 조건에 item index 도 필요한경우 사용
* filterNot()
  * filtering 조건에 '!' 을 추가한것과 동일하나 좀 더 가독성 향상
* filterIsInstance<type>()
  * List<Any> 등의 여러 타입의 item이 섞여있는 Collection 에서 특정 타입의 item 만 필터링하고자 할 경우 사용
```kotlin
val numbers = listOf(null, 1, "two", 3.0, "four")
numbers.filterIsInstance<String>()
  .forEach { print(it.uppercase() + ", ")}

TWO, FOUR
```
* filterNotNull()
  * Collection 내에 null 인 item 들 필터링

# Parition
* 필터링을 수행하는것은 Filter() 와 동일하나, 필터링을 통과한 item collection, 필터링된 item collection 두개 모드를 Pair<List, List> 타입으로 반환
```kotlin
val numbers = listOf("one", "two", "three", "four")
val (match, rest) = numbers.partition { it.length > 3 }

println(match)
println(rest)

/*
[three, four]
[one, two]
 */
```

# Test Predicates
* all(), any(), none() 은 java 와 동일하게 동작
* any(), none() 은 predicate 파라미터 전달 없이 사용할 수 있으며, 이경우 Collection 의 empty 판별
```kotlin
val numbers = listOf("one", "two", "three", "four")
val empty = emptyList<String>()

println(numbers.any())
println(empty.any())

println(numbers.none())
println(empty.none())
/*
true
false
false
true
 */
```