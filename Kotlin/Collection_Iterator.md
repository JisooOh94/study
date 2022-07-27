# Iterators
* 기본적인 iterator 동작 및 사용방식은 java 와 동일
```kotlin
val numbers = listOf("one", "two", "three", "four")
val numbersIterator = numbers.iterator()
while (numbersIterator.hasNext()) {
    println(numbersIterator.next())
}
for (item in numbers) {
    println(item)
}
numbers.forEach {
    println(it)
}
```
* mutable Collection 의 iterator 일경우, java 와 동일하게 remove 수행 가능
```kotlin
val mutableMap = mutableMapOf("1" to "val_1", "2" to "val_2")
val mutableIterator = mutableMap.iterator()
while(mutableIterator.hasNext()) {
    val entry = mutableIterator.next();
    if(entry.key.equals("1")) {
        mutableIterator.remove()
    }
}
```

### ListIterator
* 앞, 뒤 모두 순회할 수 있는 iterator
```kotlin
val numbers = listOf("one", "two", "three", "four")

val listIter = numbers.listIterator(numbers.size)
while(listIter.hasPrevious()) println(listIter.previous())
```
* Mutable List 의 ListIterator일 경우, item 추가 및 수정까지 가능
```kotlin
val mutableList = mutableListOf(1,2,3)
val mutableListIterator = mutableList.listIterator()

mutableListIterator.next()
mutableListIterator.add(4)

mutableListIterator.next()
mutableListIterator.set(5)
```



