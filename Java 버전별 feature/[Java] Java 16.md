# Java 16
```
Vector API (Incubator)
Enable C++14 Language Features
Migrate from Mercurial to Git
Migrate to GitHub
ZGC: Concurrent Thread-Stack Processing
Unix-Domain Socket Channels
Alpine Linux Port
Elastic Metaspace
Windows/AArch64 Port
Foreign Linker API (Incubator)
Warnings for Value-Based Classes
Packaging Tool
Foreign-Memory Access API (Third Incubator)
Pattern Matching for instanceof
Records
Strongly Encapsulate JDK Internals by Default
Sealed Classes (Second Preview)
```

### ZGC: Concurrent Thread-Stack Processing [[ref]](https://sangheon.github.io/2021/04/30/kor-zgc-jdk16.html) [[JEP 376]](https://openjdk.java.net/jeps/376)
* 기존 ZGC 에서 STW 로 수행되던 marking 과정을 concurrent로 수행되도록 수정, 이를 통해 ZGC 의 모든과정이 Concurrent 로 수행
* 힙 크기, 라이브 객체 크기에 상관없이 항상 O(1) 의 STW 보장, STW 시간 또한 1ms 미만 보장(평균 0.05ms, 최대 0.5ms)

### Elastic Metaspace [[JEP 387]](https://openjdk.java.net/jeps/387)
* 사용하지 않는 HotSpot class metadata (즉 metaspace) memory를 운영체제에 보다 신속하게 반환 및 metatspace footprint 감축, metaspace code 단순화를 통해 관리 비용 감소

### Packaging Tool [[ref]](https://www.baeldung.com/java14-jpackage) [[JEP 392]](https://openjdk.java.net/jeps/392)
* java 어플리케이션을 자동으로 설치해주는 설치 파일 생성 기능
* 명령행 명령을 통해 생성 가능
```java
jpackage --input target/ \
  --name JPackageDemoApp \
  --main-jar JPackageDemoApp.jar \
  --main-class com.baeldung.java14.jpackagedemoapp.JPackageDemoApp \
  --type dmg \
  --java-options '--enable-preview'
```
* java 어플리케이션 jar, 사용된 써드파티 라이브러리, jar 실행에 필요한 java, 환경 설정 등 실행에 필요한 모든 구성요소들이 설치 파일에 포함
* 설치파일을 받은 사용자는 여타 다른 프로그램 설치할떄와 마찬가지로 설치파일 실행만으로 java 어플리케이션 설치 및 사용가능, 별도의 설정이나 설치 불필요

### Pattern Matching for instanceof [[JEP 394]](https://openjdk.java.net/jeps/394)
* instanceOf 의 사용성을 개선하여, 타입 체크한 객체 자동 형변환 수행 기능 추가
* AS-IS
```java
if (obj instanceof String) {
    String text = (String) obj;
}
```
* TO-BE
```java
if (obj instanceof String str) {
    logger.info(str);
}

if (obj instanceof String str && str.length() > 2) {
    logger.info(str);
}
```

### Records [[JEP 395]](https://openjdk.java.net/jeps/395)
* C의 구조체, 코틀린의 데이터 클래스 처럼 데이터 저장이 목적인 특수 클래스 
* 클래스 정의가 일반 클래스에 비해 훨씬 더 간결하고 가볍기 때문에 Entity 혹은 Dto 클래스 정의시 용이할것으로 보임
* AS-IS
```
public class SampleRecord {
   private final String name;
   private final Integer age;
   private final Address address;
 
   public SampleRecord(String name, Integer age, Address address) {
      this.name = name;
      this.age = age;
      this.address = address;
   }
 
   public String getName() {
      return name;
   }
 
   public Integer getAge() {
      return age;
   }
 
   public Address getAddress() {
      return address;
   }
}
```
* TO-BE
```
public record SampleRecord(
   String name,
   Integer age,
   Address address
) {}
```

* 멤버필드는 자동으로 immutable(private final) 로 선언되며, 모든 멤버필드를 초기화하는 생성자가 자동으로 추가됨
* 용도는 데이터 저장이나, 필요에 따라 static 필드, static 메서드, public 메서드 추가 가능
* getter 메서드가 자동으로 추가되며, getXXX() 가 아닌, 필드명을 그대로 getter 메서드 명으로 사용 (name(), age(), address())
* 상속 불가능
* 사용할때는 일반 클래스와 동일한 방식으로 사용
```java
SampleRecord sampleRecord = new SampleRecord("foo", 29, "Seoul");
```
* Record 에 Serializable 을 implements시, 직렬화/역직렬화 하여 사용 가능하며, 이떄 내부적으로 자동으로 생성된 AllArgsConstructor 및, getter 를 사용하여 역직렬화/직렬화 수행 [[docs]](https://docs.oracle.com/en/java/javase/15/serializable-records/index.html)
* jackson 라이브러리를 통한 json 직렬화/역직렬화는 jackson 2.12 + 부터 지원 [[Jackson-Release-2.12]](https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.12)
    > Annotation scope 이 class 또는 field 인 jackson annotation 들(e.g. @JsonProperty, JsonInclude 등) 은 그대로 사용이 가능하나, scope 이 method 인 annotation 은 조사 필요(@JsonSetter, @JsonAnyGetter 등)
    > jackson 2.12 이하 버전에서도 편법을 통해 사용은 가능 [[ref]](https://dev.to/brunooliveira/practical-java-16-using-jackson-to-serialize-records-4og4)