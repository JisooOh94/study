> [<EffectiveJava - 7.6 Optional>](https://github.com/JisooOh94/study/blob/master/Effective%20Java/Content/7.6%20Optional.md) 의 심화 학습


### Optional 의 의의(출처 : [Java 공식 API 문서!](https://docs.oracle.com/javase/9/docs/api/java/util/Optional.html))
```java
메서드가 반환할 결과값이 ‘없음’을 명백하게 표현할 필요가 있고, 
null을 반환하면 에러를 유발할 가능성이 높은 상황에서 
메서드의 반환 타입으로 Optional을 사용하자는 것이 Optional을 만든 주된 목적이다. 
Optional 타입의 변수의 값은 절대 null이어서는 안 되며, 항상 Optional 인스턴스를 가리켜야 한다.
```
1. 메서드가 null을 반환할 수 있고, 
2. 메서드를 호출한 클라이언트에서 null을 받았을시 에러가 발생할것으로 예상될때
3. Optional 을 쓰자!

### Optional이 필요한 경우
* select 쿼리로 db를 조회해 데이터를 가져오는 dao 메서드의 경우, select 쿼리 결과가 0이면 null을 반환
* dao 에선 데이터 과업에만 집중하기 위해 null check등의 비즈니스로직을 넣지 않아 그대로 select 쿼리 수행 결과를 return
* 이를 사용하는 클라이언트 코드에선 메서드 명만으로 db를 조회하는 dao 메서드인지, 타 서버 api를 호출하는 dao 메서드인지 코드를 뜯어보기 전까진 알 기 힘듦
* 나도 db 조회 dao 메서드를 클라이언트코드에서 사용할떄 null 방어코드를 추가하지 않아 버그를 낸 경험이 있고, 다른 분들 PR 리뷰해드릴때도 동일한 버그를 발견한 적이 많음
* 따라서 db 조회 dao 메서드 같은 특수 케이스에선 코스트가 들더라도 optional을 사용하는것이 좋다고 판단됨

# Optional api
## Optional 객체 생성
### of(Object)
* 파라미터 객체를 감싸는 Optional 객체 생성
* 파라미터 객체가 null 일경우 NPE 발생

```java
Optional<String> value = Optional.of("String");
```

### ofNullable(Object)
* 파라미터 객체를 감싸는 Optional 객체 생성
* 파라미터 객체가 null 일경우 empty Optional 객체 반환

```java
Optional<String> value = Optional.ofNullable("String");
Optional<String> value = Optional.ofNullable(null);
```

## Optional 객체 데이터 조회
### get()
* Optional 이 감싸고 있는 객체 조회
* 감싸고 있는 객체가 null 이어서 empty optional 객체일경우 NPE 발생
* isPresent() 로 null check 후 객체 조회하는것이 안전
```java
Optional<String> opt = Optional.ofNullable(null);

if(opt.isPresent()) {
    System.out.println(opt.get());
}
```

### orElse(Object)
* Optional 이 감싸고 있는 객체 조회
* Optional 이 감싸고 있는 객체가 null(empty optional) 일경우 파라미터 객체 return
* 파라미터로 새로 생성하는 객체를 전달할 경우, Optional 이 empty 가 아니면 쓰이지도 않을 객체를 생성하게되어 리소스 낭비 발생
   * 파라미터로 재사용 객체(static 객체)를 전달하거나 orElseGet()을 사용하여 리소스 낭비 방지
```java
Optional<Map> opt = Optional.ofNullable(map);
return opt.orElse(new HashMap<String, Object>());  //optional null check 전 미리 파라미터 객체를 생성해두므로 optional이 empty가 아니면 리소스 낭비가 됨

return opt.orElse(Collections.emptyMap());     //static 객체 전달
return opt.orElseGet(HashMap::new);        //orElseGet
```

### orElseGet(Supplier)
* Optional 이 감싸고 있는 객체 조회
* Optional 이 감싸고 있는 객체가 null 일경우 파라미터 supplier 로 객체 생성하여 return
* Optional 이 감싸고 있는 객체가 null 일경우에만 새로운 객체 생성하므로 리소스 낭비 발생하지 않음

```java
Optional<Map> opt = Optional.ofNullable(map);
return opt.orElseGet(HashMap::new);  //optional 이 empty 일 때에만 supplier 를 통해 객체 생성
```

### orElseThrow(Exception)
* Optional 이 감싸고 있는 객체 조회
* Optional 이 감싸고 있는 객체가 null 일 경우 파라미터 예외 throw  
```java
Optional<Map> opt = Optional.ofNullable(map);
return opt.orElseThrow(new RuntimeException());  //optional 이 empty 일 때에만 supplier 를 통해 객체 생성
```

## Primitive type Optional
* 리터럴을 감싸는 Optional 을 생성할경우 auto-boxing이 발생하게 되어 성능 저하
* 이를 방지하기 위해 Optional은 3가지 타입 리터럴용 optional 제공
   * OptionalInt(getAsInt), OptionalLong(getAsLong), OptionalDouble(getAsDouble)
* 리터럴용 optional은 전용 get api 보유
   * OptionalInt - getAsInt, OptionalLong - getAsLong, OptionalDouble - getAsDouble
* 리터럴은 null 이 없으므로 리터럴용 optional 도 ofNullable 지원하지 않음

```java
OptionalInt opt = Optional.of(5)
System.out.println(opt.getAsInt());
```

## 기타 Optional api
* empty() : 비어있는 optional 반환
* isPresent() : Optional이 감싸고 있는 객체 null check
* ifPresent(Consumer) : Optional이 감싸고 있는 객체가 null 이 아닐경우, 파라미터 람다식 수행

# Optional 사용 Tip
1. Optional 객체는 stream api 호출 가능
```java
int result = Optional.ofNullable("Stirng").map(str -> Integer.parseInt(str));
```

2. isPresent() - get() 대신 orElse() 사용하여 가독성 증대
3. orElse() 대신 orElseGet() 사용하여 불필요한 리소스 낭비 발생 방지
4. Optional 객체는 생성비용이 비싸므로 가급적 일반 null check 코드 사용
5. Optional 을 클래스 멤버필드나 메서드 파라미터로 사용 지양(Optional 객체가 복사생성되므로)
6. 리터럴 타입 데이터를 감싸는 Optional 은 리터럴용 Optional 사용하여 auto-boxing 방지

[[참고1]](http://tcpschool.com/java/java_stream_optional), [[참고2]](http://homoefficio.github.io/2019/10/03/Java-Optional-%EB%B0%94%EB%A5%B4%EA%B2%8C-%EC%93%B0%EA%B8%B0/)