# String Constant Pool
* 문자열 리터럴을 저장하는 독립된 메모리 영역으로서 Metaspace 영역에 존재하고 GC 대상에서 제외됨 (GC 옵션을 통해 특정케이스에 GC 수행 가능) 
* Java String 은 불변객체이기 때문에 문자열 리터럴 사용시, String Constant Pool 에 존재한다면 재사용, 없다면 String Constant Pool 에 저장후 사용

```java
String str = "hello"; // String constant pool에 저장
String str2 = "hello"; // String constant pool에서 재사용
System.out.println(str == str2); // true (같은 객체를 재사용하기 때문에)
```

* 문자열 리터럴이 아닌, new 연산자를 통한 명시적 생성은 String Constant Pool 을 사용하지 않고 heap 영역에 독립된 객체로 생성됨
    * 중복되는 문자열을 가진 객체가 생성될 수 있음. 성능 및 메모리 최적화를 위해 가급적 사용 지양

```java
String doNotUse = new String("Hello World!");  // heap 영역에 저장됨
```

![image](https://github.com/JisooOh94/study/assets/48702893/929c892b-0d60-4e29-8ba8-decc4a482916)


# String '+' 처리
### Java 5 이전
* StringBuffer 를 이용하여 문자열 결합 수행 (StringBuffer 는 동기화처리를 지원하지만, 그만큼 성능이 떨어짐)
```java
String result = "Hello, " + "world" + "!";
// 컴파일러는 내부적으로 다음과 같이 변환:
String result = new StringBuffer().append("Hello, ").append("world").append("!").toString();
```

* 문자열 변수 + 문자열 리터럴 처리시 내부적으로 메모리 재할당 및 문자열 복사가 수행되기때문에 비효율적으로 동작

```java
String name = "world";
String greeting = "Hello, " + name + "!";
// 컴파일러는 내부적으로 다음과 같이 변환:
String greeting = new StringBuffer().append("Hello, ").append(name).append("!").toString();
```

### Java 5 이후
* StringBuffer 대신 StringBuilder 를 이용하여 문자열 결합 (동기화처리는 지원하지 않지만 상대적으로 그만큼 성능이 좋음)
```java
String result = "Hello, " + "world" + "!";
// 컴파일러는 내부적으로 다음과 같이 변환:
String result = new StringBuilder().append("Hello, ").append("world").append("!").toString();
```

* 문자열 변수 + 문자열 리터럴 처리시 이전과 마찬가지로 메모리 재할당 및 문자열 복사가 수행되기떄문에 여전히 비효율적으로 동작

```java
String name = "world";
String greeting = "Hello, " + name + "!";
// 컴파일러는 내부적으로 다음과 같이 변환:
String greeting = new StringBuilder().append("Hello, ").append(name).append("!").toString();
```

### Java 9 이후
* Java 9 부터 invokedDynamic 을 이용한 문자열 결합 최적화가 도입 
  * invokedynamic 기반의 StringConcatFactory를 사용하여 문자열 결합
  * StringConcatFactory 는 런타임에 결합할 문자열 데이터를 기반으로 최적의 문자열 결합 방법을 선택하여 수행
  * 컴파일 시점이 아닌 런타임 시점에 문자열 결합 방법을 결정하여 더 효율적인 성능을 제공

```java
String result = "Hello, " + "world" + "!";
// Java 9 이상에서는 invokedynamic을 사용하여 런타임에 최적화된 방법으로 문자열을 결합. 따라서 컴파일 시점에서는 코드가 어떻게 최적화되는지 볼 수 없음
```

```java
String name = "world";
String greeting = "Hello, " + name + "!";
// Java 9 이상에서는 invokedynamic을 사용하여 런타임에 최적화된 방법으로 문자열을 결합
```

> Ref
> * https://deveric.tistory.com/123
