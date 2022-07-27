# Range
* 설정한 범위의 Iterable 을 손쉽게 생성해주는 기능
* 주로 in 절과 함께 사용되며, 특정 값이 설정한 범위의 Iterable에 포함되는지 검사하는데 주로 사용
* Range 값으로는 Int, Long, Char 가능하며, 필드로 저장 및 사용 가능
```kotlin
val intRange = 1..3
val longRange = 1L..4L step 2L
val charRange = 'c' downTo 'a'

for(i in intRange) { print(i) }
```
* Range 도 Collection 과 마찬가지로 Iterable 을 상속받으므로, Collection operator 사용 가능
```kotlin
(1..4).forEach{println(it)}
('a'..'b').map {it.toString()}
```

### Range 정의 방식
* from..to : from 부터 to 까지 1씩 증가하는 iterable (inclusive)
```kotlin
for(i in 1..3) { print(i) }
//123
```

* from downTo to : from 부터 to 까지 1씩 감소하는 iterable
  * (from..to).reversed() 와 동일 
```kotlin
for(i in 3 downTo 1) { print(i) }
//321
for(i in (1..3).reversed()) { print(i) }
```

* from..to step gap : from 부터 to 까지 gap 만큼씩 증가하는 iterable
  * gap 이 1 이상일경우, last item 은 range 범위의 마지막 값과 달라질 수 있음
```kotlin
for(i in 1..8 step 2) { print(i) }
//1357
```

* from until to : from 부터 to 까지 1씩 증가하는 iterable (exclusive)
```kotlin
for(i in 1 until 3) { print(i) }
//12
```