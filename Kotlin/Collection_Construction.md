# Collection 생성
* 미리 정의되어있는 builder 를 통한 생성 가능(Mutable Collection only)
  * buildList, buildSet, buildMap
  * mutable empty collection 생성후, wrtie opertaion 으로 item 초기화 하며(성능저하 방지), 최종적으로 immutable collection 반환
```kotlin
val myList = buildList {
    add("1")
    add("2")
}
val mySet = buildSet {
    add("item_1")
    add("item_2")
    addAll(myList)
}
val myMap = buildMap {
    put("key_1", "val_1")
    put("key_2", "val_2")
}
```
* List의 경우 initial size 와 초기화 supplier 를 인자로 받는 생성자 제공
```kotlin
val list = List(10, {0})
val list2 = List(10, {index ->  index * 2})
```
* 구현체 직접 명시하여 생성 가능
```kotlin
val list = LinkedList(listOf(1, 2))
```
  * 내부적으로 ArrayList 생성후, 다시 LinkedList 에 addAll 하는 방식으로 동작하여 비효율적으로 동작
  * 따라서 empty LinkdList 로 생성후, 직접 add 나 addAll 호출하는것이 효율적
  ```kotlin
    val linkedList_2 = LinkedList<Integer>()
    linkedList_2.add(Integer(1))
    linkedList_2.add(Integer(2))
  ```

<br>

# Empty Collections 생성
* 빈 Collection 생성시, 초기화 구문의 item 을 비워두면 된다(단, item 이 없어 타입추론이 불가능하므로 type 명시 필수)
```Kotlin
val emptySet = mutableSetOf<String>()
```
* 혹은 미리정의되어있는 static 메서드 사용하여 생성
```kotlin
val emptyList = emptyList<String>()
val emptySystem = emptySet<String>()
val emptyMap = emptyMap<String, String>()
```

# Collection 복사
* toList, toSet 등의 to 시리즈 메서드들을 통해 Collection deep copy 지원
```kotlin
val list = listOf(1,2,3)
val copiedList = list.toList()

val set = setOf(1,2,3)
val copiedSet = set.toSet()

val map = buildMap { put("key_1", "val_1") }
val copiedMap = map.toMap()
```
* 또한 to 시리즈 메서드를 통해 다른 type Collection 이나 mutable Collection 으로 변환 가능
```kotlin
val list = listOf(1,2,3)
val parsedSet = list.toSet()
val parsedMutableList = list.toMutableList()

val map = buildMap { put("key", "val") }
val parsedList = map.toList()
val parsedSortedMap = map.toSortedMap()
```
* 다만 mutable Collection 을 immutable 로 변환하는 to 메서드는 없으며 별도의 형식으로 변환 필요
```kotlin
val mutableList = mutableListOf(1,2,3)
val immutableList: List<Int> = mutableList

val mutableSet = mutableSetOf(1,2,3)
val immutableSet: Set<Int> = mutableSet

val mutableMap = mutableMapOf("key" to "val")
val immutableMap: Map<String, String> = mutableMap
```