# Optional<T>
* java 8에 추가된 데이터 타입
* 데이터를 가지고있거나 가지고 있지 않음을 표현하는 데이터 타입으로서 일종의 원소 1개짜리 컬렉션
* 데이터를 감싸서 부가기능(데이터 null여부 판단)을 추가해주는 일종의 레퍼런스 타입
```java
pulic OptionalInt max(List<Integer> list) {
	if(list.isEmpty()) {
		return OptionalInt.empty();		//null 대신 Optional.empty 반환
	}
	...
}

OptionalInt optional = max(list;)
int maxVal = optional.orElse(-1);		//optional 이 empty이면 default value 반환
```

### Optional의 장점
* 일반적인 메서드에서 반환할 값이 없을시, null 을 반환하는데 이는 메서드를 호출한 클라이언트부에 null 처리 코드를 강제
```java
pulic Integer max(List<Integer> list) {
	if(list.isEmpty()) {
		return null;
	}
	...
}

Integer maxVal = max(list);
if(maxVal == null) {
	...
}
```
* 또한 클라이언트부에서 null 처리 코드를 빠트릴시, NPE 발생

```java
pulic Integer max(List<Integer> list) {
	if(list.isEmpty()) {
		return null;
	}
	...
}

Integer maxVal = max(list);

int gap = maxVal - minVal;
```

* Optional의 경우, 값이 없음을 Empty같은 개념으로 표현하기때문에, 반환타입이 Optional일시, 클라이언트부에 null 처리 코드가 없어도 되고, NPE 도 발생하지 않음

### Optional의 단점
* Null 처리 코드가 필요없고, NPE 도 발생하지 않지만, 대신에 Optional 처리코드가 필요하고, Empty 처리가 되지 않으면 NoSuchElementException 발생함
```java
OptionalInt optional = max(list);
int gap = 1000 - optional.getAsInt();		//optional 처리 코드 강제, optional이 Empty일시 NoSuchElementException 발생

int gap = optional.orElse(-1);		//Empty일시 -1을 반환하도록 하는 Empty 처리 코드
```

* Optional 객체를 생성하고 반환값을 그 객체에 담아 반환하는것이므로 성능상 저하 발생 

### 결론
1. 실수 방지
	* Null의 경우 처리코드가 명시되어있지 않아도 컴파일러가 잡아낼 수 없지만 Optional은 처리코드가 명시되어있지 않으면 컴파일러에서 체크 가능
	* 메서드 반환값이 null 일 수도 있음을 API 사용자가 소스코드까지 뜯어보지 않아도 알 수 있음
2. 가독성 증가
	* Null 처리코드에 비해 Optional 처리코드가 더 짧고 가독성이 좋음
```java
//null 방식
Integer maxVal = max(list);
if(maxVal == null) {
	maxVal = -1;
}

//Optional 방식
int maxVal = max(list).orElse(-1);
```

### Optional 사용하기 적절한 때
* 메서드의 반환값이 null 일 수 있고, 이 메서드를 사용하는 클라이언트에서 별도의 null 처리를 반드시 해주어야 할때
* 그리고 같이 협업하는 팀원들간의 지식 공유 및 사용 합의가 되었을때...