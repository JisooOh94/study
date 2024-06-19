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

> Ref
> * https://deveric.tistory.com/123
