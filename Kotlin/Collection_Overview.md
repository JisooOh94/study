# Collection
* 코틀린은 자체 컬렉션 기능을 제공하지 않고 자바의 표준 컬렉션을 가져와서 사용하기때문에 기존에 작성된 자바 코드와 상호작용하기가 쉽다.
* 자바와 동일한 컬렉션 클래스이지만 코틀린에서의 컬렉션은 더 많은 기능을 지원하며, 기본적으로 Mutable(변할 수 없는)과 Immutable(불변의)을 별개로 지원

![image](https://user-images.githubusercontent.com/48702893/181052411-aed3f806-96cc-4a0b-9034-8bc3cf8b14b9.png)

# List
* default 구현체 : ArrayList
### Immutable List
* listOf / listOf<type> 로 생성 및 초기화
```kotlin
val fruits= listOf<String>("apple", "banana", "kiwi", "peach")
val fruits= listOf("apple", "banana", "kiwi", "peach")
```
* immutable 이므로 getter 만 존재하며, 배열처럼 [index] 를 통한 조회도 가능
```kotlin
println("fruits.get(2): ${fruits.get(2)}")
println("fruits[3]: ${fruits[3]}")
```

### Mutable List
* mutableListOf / mutableListOf<type> 로 생성 및 초기화
```kotlin
val fruits= mutableListOf<String>("apple", "banana", "kiwi", "peach")
val fruits= mutableListOf("apple", "banana", "kiwi", "peach")
```
<br>

# Set
* default 구현체 : LinkedHashSet
### Immutable Set
```kotlin
val numbers = setOf(33, 22, 11, 1, 22, 3)
if(numbers.contains(1)) {...}
```

### Mutable
```kotlin
val numbers = mutableSetOf(33, 22, 11, 1, 22, 3)
numbers.add(100)
numbers.remove(33)
```

<br>

# Map
* default 구현체 : LinkedHashMap
### Immutable Map
* mapOf 로 생성 및 초기화하며 아이템은  Pair(key, value) 또는 key to value 로 표현
```kotlin
val numbersMap = mapOf("1" to "one", "2" to "two", "3" to "three")
val numbersMap = mapOf(Pair("1", "one"), Pair("2", "two"), Pair("3", "three"))
```
* list 와 마찬가지로 배열형식 [key] 조회 지원
```kotlin
println("numbersMap.get(\"1\"): ${numbersMap.get("1")}")
println("numbersMap[\"1\"]: ${numbersMap["1"]}")
```

#### Mutable Map
* mutableMapOf 로 생성 및 초기화
```kotlin
val numbersMap = mutableMapOf("1" to "one", "2" to "two", "3" to "three")
numbersMap.put("4", "four")
```
* 배열형식으로 조회 밑 값 추가, 수정 가능
```kotlin
val mutableMap = mutableMapOf("key_1" to "val_1", "key_2" to "val_2")
mutableMap["key_3"] = "val_3"
mutableMap["key_1"] = "new_val"
```
* mutableMapOf 로 생성 및 초기화시, 초기화만을 위한 Pair 객체를 생성하게 되므로 비효율적으로 동작
```kotlin
public fun <K, V> mutableMapOf(vararg pairs: Pair<K, V>): MutableMap<K, V> =
    LinkedHashMap<K, V>(mapCapacity(pairs.size)).apply { putAll(pairs) }

public fun <K, V> MutableMap<in K, in V>.putAll(pairs: Array<out Pair<K, V>>): Unit {
    for ((key, value) in pairs) {
        put(key, value)
    }
}
```
* 따라서 성능에 민감한 애플리케이션 개발시, empty mutableMap 생성후, 직접 put 수행
```kotlin
val mutableMap = mutableMapOf<String, String>()
mutableMap.put("key_1", "val_1")
mutableMap["key_2"] = "val_2"

val mutableMap = mutableMapOf<String, String>().apply{ this["key_1"] = "val_1"}
```