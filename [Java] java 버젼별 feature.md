# Java 8
https://www.oracle.com/java/technologies/javase/8-whats-new.html

### 함수형 프로그래밍
https://github.com/JisooOh94/study/blob/master/%5BJava%5D%20%ED%95%A8%EC%88%98%ED%98%95%20%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D.md
https://github.com/JisooOh94/study/blob/master/JAVA%EC%9D%98%20%EC%A0%95%EC%84%9D/Content/5.%20%EB%9E%8C%EB%8B%A4%EC%8B%9D.md
https://github.com/JisooOh94/study/blob/master/JAVA%EC%9D%98%20%EC%A0%95%EC%84%9D/Content/5.2.%20%EB%9E%8C%EB%8B%A4%EC%8B%9D%20%ED%99%9C%EC%9A%A9.md

* lambda
* stream
* funcional interface

* lambda 성능 비교 추가 정리

### HashMap 성능 개선
https://github.com/JisooOh94/study/blob/master/%5BJava%5D%20HashMap%20Internal.md
* 보조 해시 함수 추가 정리

### etc
* DateTime package, Optional, CompletableFuture 추가
* PermGen area > Metaspace
  * https://github.com/JisooOh94/study/blob/master/%5BJava%5D%20HashMap%20Internal.md
  * 추가 정리
* interface default, static method 추가 가능

<br>

# Java 9
https://docs.oracle.com/javase/9/whatsnew/toc.htm#JSNEW-GUID-5B808B2F-E891-43CD-BF6E-78787E547071
https://www.popit.kr/%EB%82%98%EB%A7%8C-%EB%AA%A8%EB%A5%B4%EA%B3%A0-%EC%9E%88%EB%8D%98-java9-%EB%B9%A0%EB%A5%B4%EA%B2%8C-%EB%B3%B4%EA%B8%B0/
https://velog.io/@jerry92/Java-Jigsaw-%EB%AA%A8%EB%93%88-%EC%8B%9C%EC%8A%A4%ED%85%9C
https://greatkim91.tistory.com/197

* CMS GC depreacted

### jigsaw
Java 플랫폼 모듈 시스템
Class는 field와 method를 포함한다.
Package는 Class와 Enum, Interface, 설정파일들을 포함한다.
Module은 Package와 다른 데이터 자원을 포함한다.
Module 시스템 메커니즘은 Module이 다른 Module을 읽고 다른 Module에서 접근할 수 있는 방법을 제어하는 가독성과 접근성을 제공한다. Module에는 세 종류가 있다.
named module
unnamed module
automatic module
Java는 runtime시점에 classpath를 검색하여 서비스 공급자를 찾을 수 있는 java.util.ServiceLoader 클래스를 가지고 있다.

Jigsaw Project의주요목표로써,유연한런타임이미지를만들수있도록Java 플랫폼을모듈화하는것§JDK가모듈구조로변경되면,작은부분으로나눌수있으며필요한부분만골라배포가가능해짐(=소형디바이스의지원)

### jshell
Java9는 테스트 프로젝트나 main 메소드없이 code snippets을 신속하게 테스트 할 수 있는 대화식 REPL(Read-Eval-Print-Loop) 도구를 제공한다. 따라서 우리는 Java 기능을 쉽게 배우거나 평가해 볼 수 있다. 이제 우리는 Code테스트를 위해  java프로젝트를 만들거나 public static void main(String[] args)를 정의할 필요가 없다. 오직 코드를 작성하고 즉시 실행하면 된다.

### unified jvm logging
JVM컴포넌트에 대한 공통 로깅 시스템을 제공한다. 통합된 JVM로깅은 모든 로깅 설정에 대한 새로운 명령줄 옵션 -Xlog를 사용하여 복잡한 수준의 JVM 구성요소에 대한 근본 원인 분석을 수행할 수 있는 구성하기 쉬운 정밀 도구를 제공한다.

Log메세지는 tag(os, gc, modules..)를 사용하여 분류된다.  하나의 메세지는  다수의 tag(tag 세트)를 가질 수 있다.
로깅 레벨 : error, warning, info, debug, trace, develop.
3가지 유형의 Output제공 : stdout, stderr, 또는 text file.
메세지는 time, uptime, pid, tid, level, tags 등으로 꾸밀 수 있다.

* try-with-resource 개선
  Allow effectively final variables to be used as resources in the try-with-resources statement
* private 메소드도 interface 내에 생성할 수 있게 됨.
  * 코드재사용률을높이고,개발자의의도를명확히전달하기위해Private method를제공
* immutable collecion
  * Read-Only 한Collections 를생성하기위한신규방법을제공함§Java 8에서는Collections.unmodifiable~~~() 메소드를사용하였음-> 장황한메소드이름과사용법§Java 9에서는List.of(), Map.of() 형태의of() API를제공함
* 프로퍼티 파일에 UTF-8 지원
  익명 클래스에 대한 Diamond Operator 허용
  Reactive Stream Api 추가
  HTTP2 Client
  Arrays 메서드 추가
  compare(a, b) : 두 배열(a,b) 를 비교해서 값을 반환

* @Deprecated 주석을강화
* CompletableFuture 개선 - 타임아웃과 지연 기능 추가
* ava 9 에서는Stream Interface 를제공하며,takeWhile(), dropWhile()메소드를제공
* HTTP/2 프로토콜과WebSocket기능을지원하기위해HTTP 2 Client 도입§Java.net.http패키지에새로운HTTP 2 Client API 도입§HTTP / 1.1, HTTP / 2 프로토콜지원§동기모드(Blocking),비동기모드(Non-Blocking, WebSocketAPI 사용시)를지원

<br>

# Java 10
### Local-Variable Type Inference
초기화된 로컬 변수 선언 시
반복문에서 지역변수 선언 시 (enhanced for loop 포함)
var 사용가능 ( 초기 로컬 변수, 반복문 지역변수)
```
var numbers = List.of(1, 2, 3, 4, 5); for (var number : numbers) { System.out.println(number); } for (var i = 0; i < numbers.size(); i++) { System.out.println(numbers.get(i)); }
```

기존에 자바의 RHS(lamda, generics, diamond)에서 이미 타입 추론을 하고 있기 때문에, 해당 표현식의 LHS에 var 로 대입하면 추론이 실패될 수 있습니다.
```java
jshell> var f = () -> { };
|  Error:
|  cannot infer type for local variable f
|    (lambda expression needs an explicit target-type)
|  var f = () -> { };
|  ^----------------^
```

### Parallel Full GC for G1
G1은 full GC 를 피하도록 설계되었지만, concurrent GC 에서 충분한 young area 를 확보하지 못하면 full GC가 발생한다.
G1 에서도 full GC 를 병렬화 시켜 G1의 최악의 케이스에서 지연시간을 개선시켰다.
자바 9에 디폴트 GC로 선정된 concurrent collector인 G1에 full parallel GC 기능을 탑재하였다. 본래 G1은 full GC를 회피하도록 고안되었지만, Young area가 충분히 확보되지 못하면, full GC가 수행될 수 밖에 없다. 그런데, concurrent collector는 parallel collector보다 full GC 속도가 느리다. 이에 따라 자바 10에서는 G1의 본래 싱글 스레드 기반 full GC를 parallel full GC로 바꾸었다. 쓰레드의 수는
-XX:ParallelGCThreads 옵션으로 조절할 수 있다.
참고: http://openjdk.java.net/jeps/307

### Application Class-Data Sharing
기존의 Class-Data Sharing(CDS) 기능을 확장해 어플리케이션 클래스를 공유 아카이브에 배치하고 서로 다른 자바 프로세스들이 공유할 수 있도록 개선
stratup 시간 단축 및 메모리 사용량이 최적화 되었다.
자바 5에 존재하던 Class-Data Sharing(CDS)는 class들을 사전 처리하여 공유 아카이브에 넣어놓고, 다른 자바 프로세스들이 공유할 수 있어 startup과 footprint 시간을 감소시킬 수 있는 기능이다. 하지만 이는 bootstrap class loader만이 아카이브 class를 로드할 수 있도록 하였다. 이번 자바 10에서는 기존 CDS를 app class loader까지 확장 적용하여 보다 더 startup, footprint 시간을 감소시킬 수 있게 되었다. (AppCDS)
참고: http://openjdk.java.net/jeps/310

### Thread-Local handshakes
모든 쓰레드를 동시에 멈춰야 했던 기존과 달리 쓰레드를 개별로 멈출 수 있게 되었고,
VM safepoint(Stop the world) 를 수행하지 않고도 개별 Thread에서 콜백을 실행할 수 있다.
JVM에는 가비지컬렉션(GC)이외에도 많은 STW(Stop The World, 완전 정지시간)이 존재합니다.
예를 들어, 예외발생과 같이 JIT컴파일의 전제가 되는 최적화 조건이 뒤집히는 경우, JVM은 STW를 생성하고 그것을 이용하고 있는 스레드에 대해 JIT컴파일된 코드를 파기합니다.
그러나, 해당코드를 사용하지 않은 스레드까지 멈추어 버리는 것은 성능에 좋지 않습니다.
그래서 도입된 것이 Thread-Local Handshake입니다. 이 방법을 사용하면 특정 스레드만을 타겟팅해서 고정처리할 수 있습니다.
이 기능은 JVMTI(JVM Tool Interface)호출스택얻기등 디버거를 위한 API에서도 효과를 발휘합니다.
VM safepoint를 수행할 필요 없이 개별 쓰레드를 stop시키고, 콜백을 수행하도록 할 수 있는 기능을 도입하였다.
VM safepoint란?
"Stop The World"로 표현되며, 모든 쓰레드를 일시 정지시키는 작업이다. safepoint를 발생시키는 몇몇 경우는 다음과 같다.
- Garbage collection pauses
- Code deoptimization
- Flusing code cache
- Class redefinition (e.g. hot swap or instrumentation)
- Biased lock revocation
- Various debug operation (e.g. deadlock check or stacktrace dump)
  주로 GC 작업 때 발생한다.

### Heap Allocation on Alternative Memory Devices
JVM heap 영역을 NVDIMM(비휘발성 NAND 플래시 메모리) 혹은 사용자 지정과 같은 대체 메모리 장치에 할당할 수 있게됩니다. (이제 정전 걱정은 할 필요 없는 것일까요?)
이 기능은 새로 추가된 옵션인 -XX:AllocateHeapAt=<path>를 이용해 사용할 수 있습니다.

### Root Certificates
자바에도 HTTPS 암호화 통신에 쓰는 SSL/TLS 인증서를 발급해주는 인증기관인 CA(Certificate Authority) 가
${java_HOME}/lib/security 에 cacerts 파일로 단순한 keystore가 있다.
기존에 OpenJDK 버전은 빈파일이 기본이지만, root CA 목록이 기본으로 포함된다.

HTTPS 암호화 통신에 쓰는 SSL/TLS 인증서를 발급해주는 인증기관을 Certificate Authority(CA)라고 부릅니다.
보통 브라우저는 최상위 인증기관(trusted root CA) 목록을 자체적으로 가지고 있어, 인증서가 신용있다고 판단한 CA로부터 서명된 것인지 확인합니다.


1. Time-Based Release Versioning: 자바 릴리즈 주기 변경으로 versioning format 변경
   (참고: http://openjdk.java.net/jeps/322)

2. Experimental Java-Based JIT Compiler: 자바 기반 JIT compiler인 Graal을 Linux/x64 플랫폼에서 사용할 수 있게 한다. (아직 실헙 단계, -XX:+UnlockExperimentalVMOptions -XX:+UseJVMCICompiler 옵션을 사용한다.)
   참고: http://openjdk.java.net/jeps/317

3. Additional Unicode Language-Tag Extensions (참고: http://openjdk.java.net/jeps/314)

3. <br>

# Java 11
Oracle vs. Open JDK
Java 10은 라이선스없이 상업적으로 사용할 수있는 마지막 무료 Oracle JDK 릴리스입니다. Java 11부터는 Oracle의 무료 LTS (장기 지원)가 없습니다.

Nest-Based Access Control
Dynamic Class-File Constants
Improve Aarch64 Intrinsics
Epsilon: A No-Op Garbage Collector
Remove the Java EE and CORBA Modules
HTTP Client (Standard)
Local-Variable Syntax for Lambda Parameters
Key Agreement with Curve25519 and Curve448
Unicode 10
Flight Recorder
ChaCha20 and Poly1305 Cryptographic Algorithms
Launch Single-File Source-Code Programs
Low-Overhead Heap Profiling
Transport Layer Security (TLS) 1.3
ZGC: A Scalable Low-Latency Garbage Collector

API 추가
* String : repeat, strip, isBlank, lines,
* File : readString, writeString
* Collection : toArray
* Lambda : Predicate.not
```java
//repeat
String output = "La ".repeat(2) + "Land";
//strip
is("\n\t  hello   \u2005".strip()).equals("hello");
isBlank
        assertTrue("\n\t\u2005  ".isBlank());

//Files
        Path filePath = Files.writeString(Files.createTempFile(tempDir, "demo", ".txt"), "Sample text");
        String fileContent = Files.readString(filePath);
        assertThat(fileContent).isEqualTo("Sample text");
        
//Collection
        List sampleList = Arrays.asList("Java", "Kotlin");
        String[] sampleArray = sampleList.toArray(String[]::new);
        assertThat(sampleArray).containsExactly("Java", "Kotlin");

        // Lambda
        List<String> sampleList = Arrays.asList("Java", "\n \n", "Kotlin", " ");
        List withoutBlanks = sampleList.stream()
        .filter(Predicate.not(String::isBlank))
        .collect(Collectors.toList());
        assertThat(withoutBlanks).containsExactly("Java", "Kotlin");
```
* lambda parameter 타입 추론 지원
  Java 10 때 'var' 변수가 도입되었다. 그러나 람다식의 parameter에는 사용하지 못했다.
  통일성을 위해 람다식의 parameter에도 'var'타입으로 선언할 수 있도록 하였다. 아래는 같은 의미이다.
  (var x, var y) → x.process(y)        ==        (x,y) → x.process(y)
  아래처럼 'var'변수 사용과 미사용을 혼용할 수는 없다.
  (var x, y) → x.process(y)   (X)
  아래의 경우도 불가하다.
  (var x, int y) → x. process(y) (X)
```java
List<String> sampleList = Arrays.asList("Java", "Kotlin");
String resultString = sampleList.stream()
  .map((@Nonnull var x) -> x.toUpperCase())
  .collect(Collectors.joining(", "));
assertThat(resultString).isEqualTo("JAVA, KOTLIN");
```

엡실론 가비지 걸렉터
엡실론 가비지 수집기는 할당을 처리하지만 메모리를 회수하지는 않는고 힙이 소진되면 JVM이 종료된다. 엡실론은 수명이 짧은 서비스와 가비지를 사용하지 않는 것으로 알려진 애플리케이션에 유용하다.
Epsilon GC를 사용할 경우 우리가 작성한 어플리케이션이 외부 환경으로부터 고립된 채로 실행되기 때문에 실제 내 어플리케이션이 얼마나 메모리를 사용하는 지에 대한 임계치나 어플리케이션 퍼포먼스 등을 보다 정확하게 측정할 수 있다.

ZGC
대용량 힙을 고성능으로 지원하기 위해 개발된 GC
점점 대용량이 되어가는 Heap을 최대의 성능으로 관리하려는 목표를 가지고 나온것이다.
자바 쓰레드가 수행 중에 실행되는 concurrent garbage collector이다. (아직 실험 단계) ZGC는 다음과 같은 goal을 가지고 있다.
정지 시간이 10ms을 초과하지 않는다.
정지 시간이 heap or live-set size에 따라 증가하지 않는다.
몇 백 메가바이트에서 수 테라바이트 사이즈까지의 힙을 핸들한다.

HTTP 클라이언트 표준화
Java 9, 10에 사용되었던 jdk.incubator.http 패키지가 표준화되어 java.net.http 패키지로 추가
HTTP Client API는 비동기 방식으로 작동하고, http2 프로토콜을 지원한다는 특징을 갖고 있다.

네스트 기반 액세스 컨트롤(Nest-based access controls)
중첩클래스에서 reflection으로 외부 private 변수 접근이 불가능 햇던것이 가능하게 바꼈다. (java 8 IllegalAccessException)
```java
class Test {  
    static class Nest1 {  
        private int nest1Var; 
     } 
     static class Nest2 {  
        private int nest2Var;  
    }
 }
```
위와 같이 nested class의 경우, 'Test', 'Nest1', 'Nest2'는 모두 'nestmate'이다. 기존 JVM 상에서는 nestmate끼리 private 멤버 변수를 접근하려면 컴파일러가 중간에 bridge method를 만들어야 했다. 따라서, reflection을 사용하여 nestmate class의 private 멤버 변수에 접근하려고 하면, llegalAccessException이 발생한다. 이러한 모순을 해결하고자, 새로운 'nest'라는 class file 개념을 도입해 하나의 중첩 클래스이지만 서로 다른 클래스파일로 분리하여 bridge method의 도움 없이도 서로의 private 멤버에 접근할 수 있도록 하였다.

Flight Recorder
Java Flight Recorder (JFR) is now open-source in Open JDK, whereas it used to be a commercial product in Oracle JDK.
troubleshooting, monitoring, profiling을 위해 application, jvm, os 상에서 발생하는 이벤트들을 기록한다. 기존 툴에 비해 low overhead를 가진다.
(Oracle JDK에서는 이전 버전부터 이미 제공하던 툴)

Dynamic Class-File Constants
constantDynamic이라는 새 JVM 바이트코드 명세를 추가하였다. constantDynamic은 JVM 7에 도입되었던invokeDynamic과 비슷하지만 invokeDynamic으로 상수를 처리하기에는 불필요한 오버헤드가 많이 발생하여 새 명세의 필요성에 따라 추가되었다. (invokeDynamic은 대표적으로 람다의 구현에 쓰인다.)
constantDynamic이 invokeDynamic과 다른 점
1. Condy는 상수를 다룬다. Indy에서 사용하는 CallSite, MethodHandle 객체가 필요하지 않다.
2. Condy linkage 상태가 공유된다.

constantDynamic in Java 11
1. Null
2. Enum Constants
3. Primitive type mirrors(ex. int.class)
4. VarHandles

Lunch Single-File Source-Code Programs


<br>

# Java 12
https://openjdk.java.net/projects/jdk/12/


Shenandoah: A Low-Pause-Time Garbage Collector (Experimental)
Shenandoah라는 이름의 GC(Garbage Collector) 알고리즘이 새로 추가되었다. 실행 중인 Java 스레드와 동시에 GC를 실행하여 GC 중지 시간을 단축하는 알고리즘으로. 힙 크기와는 무관하게 동일한 중지 시간을 유지하므로 대용량 heap 애플리케이션에 유용하다. 아직 실험적인 기능으로 Shenandoah GC를 활성화하려면 다음 옵션을 사용해야 한다.
-XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC


Microbenchmark Suite
성능측정도구
JDK 소스 코드 자체를 추가하거나 수정하려는 사람들을 위해 JDK 소스 코드에 다양한 JMH(Java Microbenchmark Harness) 벤치 마크가 추가되었다. JDK 소스 코드에 기본 마이크로 벤치 마크 제품군을 추가하면 개발자가 기존 마이크로 벤치 마크를 쉽게 실행하고 새 마이크로 벤치 마크를 쉽게 만들 수 있다. 추가되는 마이크로 벤치마크는 JMH(Java Microbenchmark Harness)를 기반으로 하며 JVM에서 돌아가는 프로그램의 성능을 테스트한다. (작성하는 방법은

Switch Expressions (Preview)
기존에 제공하던 switch문을 이후 JDK 14에서 Preview로 제공될 instance of의 패턴 매칭(JEP 305)을 사용할 수 있도록 단순화된 표현으로 변경되었다.
전통적인 switch 문에서는 값을 변수에 할당하여 값을 반환해야 했다. 하지만 Java 12에서는 break 또는 case value ->를 사용하여 스위치에서 값을 반환할 수 있다.
```java
String result;

//before jdk12
switch (number){
    case 1, 2 :
        result = "case_1";
        break;
    case 3 :
        result = "case_2";
        break;
    case 4, 5:
        result = "case_3";
        break;
}

//after jdk12
result = switch (number){
    case 1, 2 :
        break "case_1";
    case 3 :
        break "case_2";
    case 4, 5:
        break "case_3";
}

result = switch (number){
    case 1, 2 -> "case_1";
    case 3 -> "case_2";
    case 4, 5 -> "case_3";
    default -> "case_4";
}
```

JVM Constants API
모든 클래스는 cp_info라고 명시된, 내부의 메서드와 클래스 그리고 String과 Integer 같은 값을 바이트코드 형태로 저장하는 constant pool을 가진다.
cp_info {
u1 tag;
u1 info[];
}
constant pool은 어떤 메서드나 필드를 참조할 때, JVM이 해당 메서드와 필드의 실제 메모리상 주소를 알기 위해서 참조하는 테이블이다. 이 제안에서는 이러한&nbsp;constant pool에서 사용할 수 있는, 키 클래스 파일 및 런타임 아티팩트(예 : 상수 풀)를 모델링하기 위한 새로운 패키지( java.lang.invoke.constant ) API를 소개하고 있다.


One AArch64 Port, Not Two
Java 12 이전에는 64 비트 ARM 아키텍처를 위한 두 가지 소스 코드 또는 포트가 중복으로 존재함.
* oracle - src/hotspot/cpu/arm
* RedHat - src/hotspot/cpu/aarch64
  Java 12에서는 Oracle src/hotspot/cpu/arm 포트를 제거하고 하나의 포트 src/hotspot/cpu/aarch64만 유지하여 aarch64를 64비트 ARM 아키텍처의 기본 빌드로 만들었다.


Default CDS Archives
64비트 플랫폼에서의 JDK 빌드 프로세스를 개선하여 CDS(Class Data Sharing) 아카이브를 생성하는 것을 목표로 하는 제안. CDS는 이전 Java10에 언급된 내용으로 JVM 기동시에 성능을 향상하거나, 여러 대의 JVM이 하나의 물리 장비 또는 가상장비에서 돌아가는 경우, 자원에 미치는 영향을 줄이기 위해 개발된 기능이다.
이 제안에서는 사용자가 직접 실행할 필요 없이, -Xshare:dump 옵션을 통해서 CDS를 사용할 수 있도록 제안하고 있다. 또한, JDK 11의 VM에는 -Xshare:auto가 기본으로 사용되도록 설정되어 있기 때문에 CDS의 이점을 사용할 수 있으며, 이를 사용하지 않기 위해서는 -Xshare:off 명령어를 사용하면 된다.
java -Xshare:off HelloWorld.java

Abortable Mixed Collections for G1
GC(Garbage Collection) 중 하나인 G1이, 효율적으로 동작하도록 하기 위해 중단 가능한 Collection을 가지도록 변경하는 제안.
GC가 발생할 경우, STW(Stop The World)라는 이름의 동작으로 불필요한 데이터들을 수집할 수 있는 시간을 가지는데, 이 시간은 일반적으로 애플리케이션 동작 중간에 들어가는 시간이다. 따라서 이 시간이 길어질수록, 애플리케이션은 느려지게 된다. 따라서, 정해진 시간 내에 불필요한 객체들을 수집하지 못할 경우, GC를 중단할 수 있도록 하는 제안이다.
GC 대상을 80%의 필수 대상(Mandatory)과 20%의 선택적 대상(Optional)으로 나눈 뒤, 우선적으로 필수적인 부분에서 수집을 진행한다. 이후 남은 시간에 선택적 부분에서 수집을 진행하되, 남아있는 정지시간에 선택적 부분을 수집하지 못하리라 판단하면, 이를 다음 GC 시간의 선택적 부분으로 넘기게 된다.
- G1 GC는 힙 공간을 2048개의 regions로 나누고, 각 영역은 youg과 old 로 논리적으로 구분된다. G1 GC는 가비지 컬렉션을 수행하기 위해 수행 대상 regions를 선택하는데 (collection set), young과 old가 섞이게 되는 mixed collections에서 old regions가 너무 많이 잡히게 되면 pause time이 길어져 목표치를 초과하게 될 수 있다. 이를 방지하기 위해서는 G1 GC가 collection set을 만드는 과정에서 잘못된 수의 regions을 반복적으로 select하는 때를 감지하여 실제 가비지 컬렉션 과정 중간에 작동을 취소할 수 있게끔 해야 한다 (aborable).
  위 목표를 실현하기 위해 Java 12 의 G1 GC는 잘못된 수의 regions가 반복적으로 select됨을 감지하면 더욱 점진적인 mixed collections 과정을 취한다: collection set을 mandatory part, optional part 둘로 나눈다. mandatory part는 young regions와 같이 G1 GC가 점진적 처리를 할 수 없는 영역으로 구성하지만, 효율성을 위해 old regions도 포함할 수 있다.
  그리고 나머지는 old regions로만 구성된 optional part가 된다. 이 과정의 결과로 collection set은, 예를 들어 80%의 mandatory part, 20%의 optional part와 같이 나뉘게 된다.
  우선 mandatory part의 가비지 컬렉션을 마치고 optional part에 대해서는 훨씬 더 세분화된 방법을 취한다. pause time 여유가 남아 있다면 그 시간에 비례하여 old regions를 처리하는데, 최대 한 번에 한 old region씩 처리한다. 처리 도중 남은 시간이 없다면 처리 중인 region까지만 작업을 마치고 다음 가비지 컬렉션을 중단한다(abort).
  mandatory part을 다시 구성할 때 까지, 위 과정이 반복되면서 optional part는 점점 작아지게 된다. 그러다 다시 collection set을 나누게 되면 mandatory part와 optional part를 새로 만든다.

Promptly Return Unused Committed Memory from G1
GC가 활성화된 상태일 때 Java의 힙 메모리를 운영체제에 반환하도록 하는 내용을 담고 있는 제안.
현재 G1은 Full GC가 일어나거나 Concurrent cycle이라는 상황에만 Java의 힙 메모리를 운영체제에 반환한다. 하지만, Full GC는 Java에서 최대한 피해야 할 상황이므로, Concurrent cycle만 해당 반환작업을 일으킬 수 있는데, 외부에서 강제하지 않는 한 대부분의 경우에는 힙 메모리를 반환하지 않는다.
이러한 동작은 하이퍼바이저의 자원을 공유해서 사용하는 컨테이너 환경에서 특히나 불리한데, VM이 비활성인 경우, 해당 VM에 할당된 메모리의 일부만 사용하는 단계에서도 G1은 모든 힙 메모리를 유지하게 되고, VM을 사용하는 사용자는 불필요한 메모리를, 자원을 제공하는 클라우드 공급자는 하이퍼바이저의 자원을 모두 활용하지 못하게 된다. 따라서, VM의 활동 상태를 감지하여 힙 사용량을 조절할 수 있게 한다.
위에서 설명한 Shenandoah와 OpenJ9의 GenCon collector에서는 이미 유사한 기능을 제공하고 있으며 OpenJDK에서 테스트한 결과, 야간에는 메모리의 약 85%까지 줄일 수 있었다고 한다.
기존 G1 GC에는 시간에 따른 JVM 메모리 릴리즈가 없었다. 기존 G1 GC가 OS에 메모리를 반환하는 때는 full GC or concurrent GC cycle 이다. 그런데 G1 GC는 full GC를 최대한 피하도록 설계되었고, concurrent GC cycle은 힙 메모리 사용량과 할당 작업에서만 작동하기 때문에, 결과적으로 기존 G1 GC는 OS에 메모리를 잘 반환하지 않는다. 그리고 이는 사용 자원에 따라 요금을 부과하는 클라우드 환경에서 불필요한 비용을 초래한다. 어플리케이션이 놀고 있는 때에도 메모리를 잡고 있으니, 사용하지 않는 물건에 대해 요금을 계속 지불하게 되는 것이다.
이 문제를 완화하기 위해 Java 12 의 G1 GC는 적절한 시간 안에서 어플리케이션이 사용되지 않는다고 판단되면 사용하지 않는 메모리 일부를 OS에 반환하여 JVM 힙 크기를 조정하도록 한다.

API 추가
Files.mismatch
java.nio.file 패키지에 두 개의 파일을 비교하기 위한 Files.mismatch(Path path, Path path2) 함수가 추가되었다. 인자로 받은 두 개 Path에 위치한 파일을 비교하여, 처음으로 다른 부분의 위치를 반환하고 동일한 경우, -1L을 반환한다.

NumberFormat.getCompactNumberInstace
Locale과 NumberFormat.Style에 따라서 다른 형태로 값을 반환해주는 함수가 추가되었다.

Collectors.teeing
Stream API에 추가되는&nbsp;Collectors.teeing&nbsp;함수 두 개의 Collector와 하나의 BiFunction 총 세 개의 인자를 받는다. 처음 받은 두 개의 Collector의 결과를 세 번째 BiFunction에서 받아서 계산할 수 있다.

String.indent
String.indent(int n) : 입력한 n 만큼 들여 쓰기 하는 함수. 음수를 넣을 경우, 앞으로 당긴다.

String.transform
String.transform(Function<? super String, ? extends R> f) : 제공된 함수에 특정 String 인스턴스를 입력으로 제공하고 해당 함수에서 반환된 출력을 반환한다.

<br>

# Java 13
https://openjdk.java.net/projects/jdk/13/
Dynamic CDS Archives
Dynamic CDS Archives는 jdk12에서 추가된 Default CDS Archives의 확장된 버전이다. 사용성을 개선하고 Default CDS Archives에는 없는, 로드된 애플리케이션의 클래스와 라이브러리 클래스를 포함하도록 개선되었다.
HotSpot에서 AppCDS(애플리케이션 클래스 데이터 공유)를 사용하여 애플리케이션 클래스를 저장하면, 추가적인 Startup 시간과 메모리의 이점을 볼 수 있지만, 여전히 세 가지 정도의 추가적인 절차가 필요하다.
* 클래스 리스트를 생성하기 위한 하나 이상의 trial run
* 생성된 클래스 리스트를 사용하여 아카이브를 덤프
* 아카이브와 함께 실행
  또한, 이 세 가지 절차는 기본 클래스 로더를 사용하는 애플리케이션에서만 동작하며 HotSpot에서 experimental로 지원하지만 사용하기가 쉽지가 않다.
  이 제안에서는 이러한 불편함을 해결하기 위해서 Java 애플리케이션 실행 시, 간단하게 커맨드라인에&nbsp;-XX:ArchiveClassesAtExit&nbsp;옵션을 주는 것으로 AppCDS를 활성화 할 수 있다. 이렇게 실행된 Java 애플리케이션은 종료 시에 jsa라는 시스템 아카이브 파일을 생성하는데, 해당 파일을 이용해 메타데이터를 공유하는 Java 애플리케이션을 향상된 성능으로 실행시킬 수 있다. 또한, 아래와 같이 옵션에 인수를 주어 생성될 아카이브 파일의 이름을 지정할 수 있다.
  $ bin/java -XX:ArchiveClassesAtExit=hello.
  이렇게 생성된 아카이브 파일은 아래와 같이 사용할 수 있다.
  이 방식은 위의 1.trial run  절차를 제거하여 AppCDS의 사용이 간편해지고, 기본 제공 클래스 로더와 사용자 정의 클래스 로더 효과를 모두 지원한다. 또한, JEP350의 개선기능은 애플리케이션의 첫 실행에서 자동 아카이브 생성을 수행할 수 있다. 그러면 2. 아카이브덤프 절차를 제거할 수 있게되고, 이는 CDS/AppCDS 의 사용을 자동화 할 수 있다.
*

JVM 옵션을 통해 CDS를 더 편하게 사용 가능:
```java
$ java -XX:ArchiveClassesAtExit=my_app_cds.jsa -cp my_app.jar
$ java -XX:SharedArchiveFile=my_app_cds.jsa -cp my_app.jar
```

ZGC: Uncommit Unused Memory
ZGC가 사용하지 않는 힙 메모리를 OS에 반환하도록 함.
초기 ZGC는 오랫동안 사용하지 않더라도 메모리를 OS에 반환하지 않는 문제가 있어 아래와 같은 몇몇 환경에서는 좋은 방식이 아니었다.
* 사용한 리소스만큼 비용을 지불하는 컨테이너 환경
* 오랫동안 유휴 상태로 있거나 다른 애플리케이션들과 리소스들을 공유하는 환경
* 시작상태와 실행상태의 메모리 사용량이 다른환경(실행시에는 많은 메모리를 사용하지만, 싫생 이후에는 일정한 메모리만을 사용하는 환경)
  따라서 ZGC는 ZPage라는, page cache 내의 사용되지 않는 메모리 집합을 정해진 정책에 따라 커밋 해제하여 OS로 반환하되, 최소 힙 크기(-Xms) 아래로는 줄어들지 않도록 지정하게 변경되었다. (-Xms와 -Xmx가 동일한 경우, 이 기능이 암시적으로 비활성화된다. 명시적으로 비활성화하기 위해서는 -XX:-ZUncommit을 사용) 일반적으로 page cache는 LRU(Least Recently Used) 방식을 사용하고 page 크기별로 구분하기 때문에 메모리를 해제하는 방법은 비교적 간단하지만, 문제는 캐시에서 ZPage를 제거할 시기를 결정하는 데 있었다.
  단순하게는 일정 시간이 지나면 제거되도록 설정할 수 있고, 실제로 이 방식은 Shenandoah GC에서, 기본값 5분으로 사용하고 있다. ZGC도 -XX:ZUncommitDelay=<seconds>(default 300초) 으로 간단한 시간 정책을 제공할 수 있다. 이러한 방식 외에도, GC가 일어나는 빈도에 기초하여 메모리 해제 주기를 설정할 수도 있다.

Reimplement the Legacy Socket API
java.net.Socket과 java.net.ServerSocket은 JDK 1.0에서 처음 등장하였는데 유지보수와 디버깅이 어려운 레거시 Java 및 C 코드의 혼합 형태로 구현되어 아래와 같은 몇 가지 문제가 있었다.
* 스레드 스택을 I/O 버퍼로 사용하여 디폴트 스레드 스택 크기를 몇번이고 늘려야하는 문제
* 네이티브 자료구조를 사용해 구현한 비동기 close에는 수년 동안 미묘한 안정성/이식성 문제가 존재.
* 구현에는 여러 가지의 동시성 문제가 있으며 이를 해결하기 위해서는 정밀 검사가 필요.
* 네이티브 메서드에서 스레드를 Blocking 하는 대신, park 하는 미래의 환경에서는 현재 구현이 목적에 맞지 않음.

따라서, java.net.Socket 및 java.net.ServerSocket API는 모든 조작을 SPI(Service Provider Interface) 메커니즘인 java.net.SocketImpl에 위임하였고, 내장 구현을 “plain” 구현이라고 하며, SocketInputStream 및 SocketOutputStream 클래스를 지원하는 비공개 PlainSocketImpl에 의해 구현되도록 하였다.
SPI(Service Provider Interface): 플러그인 형태로 제공하는 인터페이스. 인터페이스만 정의하고 각 구현은 가져다 사용하는 벤더에서 구현하도록 함.

- 신규 클래스 sun.nio.ch.NioSocketImpl 추가되어 java.net.PlainSocketImpl 을 대체함.
- NioSocketImpl 는 내부에서 synchronized 대신 java.util.concurrent 패키지의 locks를 사용함.
- 아래 아규먼트로 Socket API 사용 가능:
- Djdk.net.usePlainSocketImpl

Switch Expressions (Preview)
JDK12에서 나왔던&nbsp;break value;&nbsp;표현식이&nbsp;yield value;로 대체되었다.&nbsp;return value;가 제어권이 함수 호출자나 생성자에게 있었다면,&nbsp;yield value;는 스위치 표현식에게 제어권을 전달한다.
- yield 키워드:
```java
switch(mode) {
case "a", "b":
yield 1;
case "c", "d", "e":
yield 2;
default:
yield -1;
};
```
- yield 는 값을 반환하고 switch를 빠져나감 (break)
- 메서드도 실행하고 값도 반환하고 싶을 수 있다. 그럴 때는 yield 키워드를 사용하면 된다. 단순 return은 안된다. 메서드 자체가 종료된다.
```
int returnFrom = switch (type) {
case TYPE_1 -> 3;
default -> {
System.out.println("return default value");
yield 2; // `yield` 키워드를 사용한다.
}
}
```
- arrrow 도 여전히 사용 가능.

Text Blocks (Preview)
다른 언어에서도 등장하는 Text block이 Java에도 추가된다. 기존에 여러 줄의 텍스트를 사용할 때,&nbsp;+와 new line으로 연결해주었다면 이 제안에서는&nbsp;“””을 통해 multi line으로 텍스트를 입력할 수 있도록 제안하고 있다.
이 제안은 Java string의 가독성을 향상하고 이스케이프 문자열 사용을 피하고자 만들어졌다. 특수문자 이스케이프 방식은 기존의 문자열 사용법과 동일하고 필요한 경우, 아래 String 관련 함수들을 사용할 수 있다.
- String str = """ 문자열 """;
- 멀티라인 문자열을 더 보기 좋고 편하게 작성할 수 있음.
```java
string textblock = """
this
is
a
new
feature,
text
block!""";
system.out.println(textblock);
string inline = "\nthis\nis\anew\nfeature,\ntext\nblock!";
system.out.println(textblock.equals(inline));
output:

this
is
a
new
feature,
text
block!
true
줄바꿈 문자가 자동으로 포함된다. 자바 코드로 json, html 텍스트를 작성할 때 아주 편리할 듯.
```

<br>

# Java 14
Pattern Matching for instanceof (Preview)
보통 instanceof 연산자를 아래와 같이 사용했다.
그런데, 이제 아래와 같이도 사용 가능하다. instanceof 연산자가 적용되는 조건문 if 블록 내에서 지역 변수를 사용할 수 있다.
```java
// 기존 방식. 캐스팅이 들어가게 된다.
if (obj instanceof String) {
String text = (String) obj;
}

// 새로운 방식. 형 변환 과정이 없고, 그 변수를 담을 수 있다.
if (obj instanceof String s) {
System.out.println(s);
if (s.length() > 2) {
// ...
}
}

// 조건을 중첩해서 넣을 수도 있다.
if (obj instanceof String s && s.length() > 2) {
// okay!
}
```

Packaging Tool (Incubator)

NUMA-Aware(Non-Uniform Memory Access) Memory Allocation for G1
메모리 시스템에서의 G1 가비지 컬렉터의 성능이 향상되었다. 즉, 대형 머신에서의 성능 향상이다. NUMA는 불균형적인 메모리 접근을 의미하는데, 멀티 프로세서 환경에서의 메모리 접근 방식이다.

JFR Event Streaming
Non-Volatile Mapped Byte Buffers

Helpful NullPointerExceptions
NPE시 라인넘버만이 아닌 어느 객체에서 발생했는지 표시
NullPointerException이 발생하면 코드 라인 넘버를 보고 어디가 문제인지 유추했던 기억이 있을 것이다. 라인 넘버만 나오기 때문에, 정확한 이유를 유추해야 하는 불편함이 있었는데, 이 부분이 조금 강화되었다.
```
private void runJEP358Test() {
// 이런 메서드를 호출하는데, `b`의 리턴이 `null` 이면?
a().b().c();
}
```

위 메서드에서 만일 b 메서드의 리턴이 null이면 어떻게 될까? 기존 버전의 자바에서는 아래와 같은 메시지가 출력됐을 것이다.

```
# 이전 버전에서는 아래와 같이 출력되었다. 대충 이런 모습...
Exception in thread "main" java.lang.NullPointerException
at MadPlay.runJEP358Test(MadPlay.java:43)
at MadPlay.main(MadPlay.java:117)
Bash
```
그런데, 자바 14버전부터는 실행 옵션에 -XX:+ShowCodeDetailsInExceptionMessages 넣어주면, 아래와 같이 NPE 메시지가 바뀐다.
```
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "MadPlay.c()"
because the return value of "MadPlay.b()" is null
at MadPlay.runJEP358Test(MadPlay.java:43)
at MadPlay.main(MadPlay.java:1317)
Bash
```


Records (Preview)
record라는 것이 생겼다. 클래스는 아닌데 뭔가 롬복(lombok) 라이브러리와 유사하다. 원래는 보통 아래와 같은 형태로 클래스를 정의했다.
```
class Point {
private final int x;
private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // getter, setter 생략
    // 더 나아가 toString, hashCode, equals 메서드도 생략
}
```

그런데, 단순 데이터 용도라면 아래와 같이 record를 사용하면 된다.
```
record Point(int x, int y) {
// 상속은 불가하다. (마치 final 클래스처럼)

    // 초기화 필드는 `private final`이다. 즉, 수정 불가
    x = 5; // 에러

    // static 필드와 메서드를 가질 수 있다.
    static int LENGTH = 25;

    public static int getDefaultLength() {
        return LENGTH;
    }
}
```
특징을 살펴보자. 먼저, final 클래스처럼 상속할 수 없는 특성을 갖고 있다. 또한 초기화에 사용되는 내부 필드는 private final이다. 수정할 수 없다. 하지만 static 필드와 메서드를 가질 수 있다.

그럼 사용할 때는 어떻게 사용할까? 아래와 같이 클래스처럼 사용하면 된다.
```
Point point = new Point(2, 3);

// getter가 자동 생성
point.x();
```
클래스와 동일하게 new 연산자로 인스턴스를 생성하면 된다. 신기한 것은 getter가 자동으로 생성된다. 이러한 점에서 롬복 라이브러리와 비슷하다고 언급한 것이다.

Switch Expressions (Standard)
Java 12, 13에서 2번의 Preview를 통해 강화된 Switch 표현식이 Java 14에서 표준화되었다.
기존 switch 구문
public class SwitchExpressionsExample {
public static void main(String[] args) {
SwitchExpressionsExample example = new SwitchExpressionsExample();
example.printDay(Day.FRI);
example.printDay(Day.TUE);
example.printDay(Day.SUN);
}

    public void printDay(Day today) {
        switch (today) {
            case MON:
            case TUE:
            case WED:
            case THUR:
            case FRI:
                System.out.println(today.name() + " is Weekday");
                break;
            case SAT:
            case SUN:
                System.out.println(today.name() + " is Weekend");
                break;
        }
    }

    enum Day {
        MON, TUE, WED, THUR, FRI, SAT, SUN
    }
}
'break'의 위치에 따라 실행 결과가 달라질 수 있기 때문에 개발자가 의도적으로 'break'를 했는지 실수인지 파악하기 어렵다.
강화된 switch 표현식
```
public void printDay(Day today) {
switch (today) {
case MON, TUE, WED, THUR, FRI -> System.out.println(today.name() + " is Weekday");
case SAT, SUN -> System.out.println(today.name() + " is Weekend");
}
}
```
여러 조건에 따라 ','로 구분해서 한 번에 처리할 수 있다.
단일 실행으로 '->'만 추가했지만, '{}' 형태로 구문을 만들 수 있다.
'->' 대신 ':'을 사용해서 예전 방식으로 사용할 수도 있다.
반환값 받기
```
public String printDay(Day today) {
String result = switch (today) {
case MON, TUE, WED, THUR, FRI -> today.name() + " is Weekday";
case SAT, SUN -> today.name() + " is Weekend";
};
return result;
}
```
표현식으로 변경되었기 때문에 결과값을 반환 받는 형식으로 만들 수 있다.
반환값의 타입을 Object로 설정하게 되면 표현식마다 다른 자료형을 반환할 수도 있다.
반환값이 있을 때 표현식이 void를 반환하면 컴파일 에러가 발생한다.
반환값이 있는 표현식을 블록('{}') 구문으로 사용
```
public String printDay(Day today) {
String result = switch (today) {
case MON, TUE, WED, THUR, FRI -> today.name() + " is Weekday";
case SAT, SUN -> {
System.out.print("Holiday! ");
yield today.name() + " is Weekend";
}
};
return result;
}
```
'yield' 키워드를 사용해서 반환(java 13에서 변경된 내용으로 java 12를 사용한다면 'yield'대신 'break'를 사용하면 된다.)
'return' 키워드를 사용하면 컴파일 에러가 발생한다. (반환값이 없는 경우라면 return을 사용할 수도 있다.)

<br>

# Java 15
https://openjdk.java.net/projects/jdk/15/

Edwards-Curve Digital Signature Algorithm (EdDSA)
Edwards-Curve 디지털 서명 알고리즘(EdDsa)을 사용하여 암호화 서명을 구현하였다.
기존 암호화 알고리즘의 취약점과 성능을 개선한 새로운 알고리즘이 추가되었다고 보면 된다.

Sealed Classes (Preview)
https://marrrang.tistory.com/82
Sealed 클래스는 클래스와 인터페이스에서 모두 사용 가능하며, 상속할 수 있는 범위를 제한한다. 그 동안은 public, private, final 등으로 상속을 제한하는 방법, 생상자에 대한 접근을 제어하는 방법, 자바 모듈을 이용해서 오픈 범위를 제한하는 방법 등을 사용할 수 있었지만 Sealed 클래스를 이용하면 명확하게 상속 범위를 개발자가 정의할 수 있어서 원치 않는 상속으로 인한 악영향을 제거할 수 있다.

Sealed Class, Interface는 간단하게 상속하거나(extends), 구현(implements)할 클래스를 지정해두고 해당 클래스들만 상속 혹은 구현을 허용하는 키워드입니다.
개발자가 코드를 작성하면서 어떠한 Super Class의 Sub Class들을 명확히 인지할 수 있어야 한다는 것을 목표로 합니다.
```java
public sealed interface CarBrand permits Hyundai, Kia{}

public final class Hyundai implements CarBrand {}
public non-sealed class Kia implements CarBrand {}
```
위처럼 선언하고 사용할 수 있습니다. 예시 코드에서 Hyundai, Kia를 제외한 다른 클래스가 구현을 하려고 하면 에러를 발생시킵니다.
위의 예시 코드에서 특이한 점은 상속/구현하는 클래스들이 final 아니면 non-sealed로 선언되어 있다는 점입니다.
Sealed Class는 몇 가지 제약 사항을 두고 있습니다.
상속/구현하는 클래스는 final, non-sealed, sealed 중 하나로 선언되어야 한다.
Permitted Subclass들은 동일한 module에 속해야 하며 이름이 지정되지 않은 module에 선언 시에는 동일한 package 내에 속해야 한다.
Permitted Subclass는 Sealed Class를 직접 확장해야 한다.
```java
//AS-IS
String getBrandName(CarBrand brand) {
    if (brand instanceof Hyundai) {
    	return "Hyundai";
    }
    
    if (brand instanceof Kia) {
    	return "Kia";
    }
    
    // 예측할 수 없는 결과 발생
    return null;
}

//TO-BE : CarBrand 객체가 들어온다면 해당 객체가 Hyundai 아니면 Kia 중에 하나라는건 확실히 할 수 있습니다.
        String getBrandName(CarBrand brand) {
        if (brand instanceof Hyundai) {
        return "Hyundai";
        }

        if (brand instanceof Kia) {
        return "Kia";
        }

        // 예측할 수 없는 결과 발생
        return null;
        }
        
```



Hidden Classes
다른 class의 bytecode에서 직접 사용할 수 없는 class이다.
Hidden class는 runtime에 class를 생성하고 reflection을 통해 간접적으로 사용하는 framework에서 사용하기 위한 것이다.
hidden class는 access 제어 중첩의 member로 정의될 수 있으며 다른 class와 독립적으로 unload 될 수 있다.


Reimplement the Legacy DatagramSocket API

ZGC: A Scalable Low-Latency Garbage Collector
JDK 11에 experimental feature로 추가되었던 Z Garbage Collector가 product feature가 되었다. (JEP 333)

기존의 default GC인 G1을 변경하는 것은 아니다.

기존엔 Linux x64 만 지원하던 것이 이제 아래 플랫폼을 모두 지원하게 되었다.

Linux/x86_64
Linux/aarch64
Windows
macOS
이제 XX:+UnlockExperimentalVMOptions 옵션을 추가하지 않아도 사용 가능하며 기존엔 -XX:+UnlockExperimentalVMOptions -XX:+UseZGC 옵션을 통해 사용할 수 있었다.

JDK 11에 도입된 이후 여러 피드백을 받고 버그를 수정했으며 여러 기능과 개선 사항을 추가하였다.

그중 중요한 몇 가지는 아래와 같다.

Concurrent class unloading
Uncommitting unused memory
최대 heap size 4TB 에서 16TB로 변경
최소 heap size 8MB로 감소
-XX:SoftMaxHeapSize
JFR leak profiler 지원
class-data sharing 지원
제한적이고 불연속적인 address space
NVRAM heap 배치 지원
NUMA 인식 향상
multi threaded heap pre-touching

textblock
ext Block 기능은 JDK 13에 preview 기능으로 추가되었었다. (JEP 355)
당초 목표는 JDK 12였지만 철회되었고(관련 글 참고) JDK 13과 JDK 14에 단계적으로 추가되어 최종적으로 JDK 15에서 preview 딱지를 떼게 되었다.

Shenandoah: A Low-Pause-Time Garbage Collector
JDK 12에 experimental feature로 추가되었던 Shenandoah garbage collector도 product feature로 변경되었다. (JEP 189)
ZGC와 마찬가지로 default GC인 G1 GC를 대체하지는 않는다.
이제 XX:+UnlockExperimentalVMOptions 옵션을 추가하지 않아도 사용 가능하며 기존엔 -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC 옵션을 통해 사용할 수 있었다.
Shenandoah GC는 실행 중인 Java thread와 병렬로 gc 작업을 수행하여 GC 일시 중지 시간을 줄인다고 한다.

<br>

# Java 16
https://openjdk.java.net/projects/jdk/16/

ZGC: Concurrent Thread-Stack Processing
Move ZGC thread-stack processing from safepoints to a concurrent phase.
The ZGC garbage collector (GC) aims to make GC pauses and scalability issues in HotSpot a thing of the past. We have, so far, moved all GC operations that scale with the size of the heap and the size of metaspace out of safepoint operations and into concurrent phases. Those include marking, relocation, reference processing, class unloading, and most root processing.
The only activities still done in GC safepoints are a subset of root processing and a time-bounded marking termination operation. The roots include Java thread stacks and various other thread roots. These roots are problematic, since they scale with the number of threads. With many threads on large machine, root processing becomes a problem.
In order to move beyond what we have today, and to meet the expectation that time spent inside of GC safepoints does not exceed one millisecond, even on large machines, we must move this per-thread processing, including stack scanning, out to a concurrent phase.
After this work, essentially nothing of significance will be done inside ZGC safepoint operations.


Elastic Metaspace
사용하지 않는 HotSpot class-metadata (즉 metaspace) memory를 운영체제에 보다 신속하게 반환하고 metatspace footprint를 줄이고 metaspace code를 단순화하여 유지 관리 비용을 줄였다.
Return unused HotSpot class-metadata (i.e., metaspace) memory to the operating system more promptly, reduce metaspace footprint, and simplify the metaspace code in order to reduce maintenance costs.

Packaging Tool
jpackage is a command-line tool to create native installers and packages for Java applications.
It's standard practice while distributing software to deliver an installable package to the end-user. This package is compatible with the user's native platform and hides the internal dependencies and setup configurations.
This allows the distribution, installation, and uninstallation of the applications in a manner that's familiar to our end users.
jpackage allows developers to create such an installable package for their JAR files. The user doesn't have to explicitly copy the JAR file or even install Java to run the application. The installable package takes care of all of this.


Pattern Matching for instanceof
JDK 14에서 preview feature로 제안되었고 JDK 15를 거쳐 JDK 16에서 최종 확정 되었다.

Records
jdk 14,15 를 거쳐 jdk16 에서 최종 확정

<br>

# Java 17
https://blogs.oracle.com/javakr/post/java-17-webcast-brief

* jdk 11 다음 LTS 버전
Restore Always-Strict Floating-Point Semantics
  항상 엄격한 부동 소수점 체계를 복원

엄격한 부동 소수점 체계 (strict floating-point semantic)와 미묘하게 다른 기본 부동 소수점 체계 (default floating-point semantic)를 병행 사용하지 않고 일관되게 엄격한 부동 소수점 체계를 사용하도록 하였다.

Java SE 1.2에서 strict와 default가 도입되기 전의 체계로 language와 vm을 복원한다.

1990년대 후반에 default floating-point semantic을 변경하게 된 동기는 원래 java language와 JVM semantic 간 잘못된 상호 작용과 인기 있는 x86 아키텍처의 x87 floting-point co-processor instruction set의 일부 특성에서 비롯되었다.

비정상 피연산자 및 결과를 포함하여 모든 경우에 정확한 floating-point semantic을 일치시키려면 추가 명령어의 큰 overhead가 필요하였다.

overflow 또는 underflow가 없을 때 결과를 일치시키는 것은 더 적은 overhead로 수행될 수 있으며 이는 java SE 1.2에 도입된 default floating-point semantic에서 허용하는 것이다.

하지만 2001년경부터 펜티엄 4 이상 프로세서에 탑재된 SSE2(Streaming SIME Extension 2) 확장은 과도한 overhead 없이 간단한 방식으로 strict JVM floating-point 연산을 지원할 수 있게 되었다.

초기 x86 시스템에서 사용되는 Java 언어와 부동 소수점 보조 프로세서 명령어 사이에 불편한 상호 작용이 있었고, 이것은 자바가 부동 소수점 연산을 전달하기 위해 큰 오버헤드의 명령들을 관리해야 하도록 만들었습니다. 오버플로와 언더플로가 없이 결과를 훨씬 더 효율적으로 수행할 수 있었지만, Java 1.2에서 Java 부동 소수점 의미 체계를 완화해야 했습니다.

2001년 무렵 프로세서는 추가 오버헤드 없이 보다 엄격한(strict) 부동 소수점 의미 체계를 허용하는 새로운 확장을 제공하기 시작했습니다. 이제 유일한 옵션인 엄격한 부동 소수점 의미 체계로 돌아가겠습니다. 엄격한 실행이 항상 정해진 옵션이었기 때문에 이것은 기존 애플리케이션에 영향을 미치지 않을 것입니다.



Enhanced Pseudo-Random Number Generators
JDK 17은 의사 난수(Pseudo-Random Number)를 생성하는 데 사용되는 일부 코드의 구조를 바꿉니다.
이전 버전의 JDK에는 여러 클래스의 의사 난수 생성기(PRNG, Pseudo-Random Number Generator)가 포함되었습니다.
그러나 그들은 공통 인터페이스가 없어서, 한 클래스를 다른 클래스로 교체하기 어려웠습니다.
이 JEP는 기존의 모든 PRNG 클래스가 현재 구현하고 있는 난수 생성기와 더 전문화된 기능으로 난수 생성기를 확장하는 다른 클래스를 포함하는 몇 가지 새로운 인터페이스를 제공 합니다.


Strongly Encapsulate JDK Internals
sun.misc.Unsafe와 같은 중요한 내부 API를 제외하고 JDK의 모든 내부 요소를 강력하게 캡슐화한다.
이로 인해 reflection을 통한 private field 접근에 대해 더 이상 동작하지 않으므로 JDK 버전 변경 시 확인이 필요하다.
```java
var ks = java.security.KeyStore.getInstance("jceks"); var f = ks.getClass().getDeclaredField("keyStoreSpi"); f.setAccessible(true);
```
Java 플랫폼의 유지 관리자와 이를 사용하는 개발자 간의 약속 중 하나는 개발자가 사용할 수 있는 API 집합이 있고, 이 API 집합은 향후 버전의 JDK에서 계속 사용할 가능성이 있다는 것입니다. 이러한 API를 제공하기 위해, 플랫폼의 관리자는 플랫폼 내에서만 사용되는 내부 API를 사용하는 경우가 있습니다.
이러한 내부 API는 예고 없이 변경될 수 있으므로 이에 의존하는 모든 프로그램은 업데이트를 적용할 때 중단될 수 있습니다.
JDK 9까지 내부 API는 표준 API 목록에 문서화되지 않았지만 개발자는 소스 코드에 액세스할 수 있으므로 찾기 쉽고, 일단 발견되면 다른 API처럼 호출할 수 있었습니다.
이러한 API로 컴파일할 때 경고가 있지만 일부 개발자는 여전히 해당 API에 의존합니다.
직접 사용하지 않더라도 이를 사용하는 일반적인 도구와 프레임워크가 많이 있을 수 있습니다.
따라서 모든 중요하지 않은 응용 프로그램이 대부분은 아니더라도 수년 동안 이 응용 프로그램에 의존했습니다.
JDK 9 및 Jigsaw의 도입으로 JDK 내부 코드만 해당 내부 API에 액세스할 수 있도록 강제할 수 있게 되었습니다.
그러나 9에서 기본 옵션으로 만드는 것은, 작은 주의만 주고, 모든 라이브러리와 프레임워크가 작동을 멈추도록 할 수 있습니다.
대신 우리는 JDK 9에 대해 더 많은 명시적 경고만 추가하기로 선택했고 시행을 시작하기 전에 적절한 시간을 기다리겠다고 말했습니다.
캡슐화도 이제 거의 4년이 지났으므로 적절한 시간이 지났으므로 기본적으로 강력한 캡슐화를 활성화합니다.
JDK 16을 사용하면 여전히 액세스할 수 있지만 기본적으로 더 이상 액세스할 수 없다는 경고가 표시됩니다.
명령어 옵션으로 모든 패키지의 캡슐화를 완화할 수 있었습니다.
JDK 17 이상에서는 명령어 옵션이 없어졌으므로 내부 API를 계속 사용하려면 --add-opens 명령어 옵션을 사용하여 특정 패키지를 하나씩 열어야 합니다.


Sealed Classes
JDK 15에서 preview로 제안되었고 JDK 16에서 JEP 397에 의해 개선되어 다시 preview로 제안되었다.
JDK 17에서는 JDK 16에서 변경사항 없이 마무리되었다.

Context-Specific Deserialization Filters
신뢰할 수 있는 데이터를 역직렬화(Deserialization)하는 것은 본질적으로 위험한 행동입니다.
공격을 최소화하는 핵심은 임의의 클래스의 인스턴스가 역직렬화 되는 것을 방지하는 것입니다.
JDK 9은 개발자가 역직렬화 할 수 있는 클래스를 지정할 수 있도록 필터를 도입하였고, 허용 목록이나 거부 목록 또는 둘의 조합을 만들 수 있습니다.
이것은 기능이 JDK 8에 백포트 될 정도로 가치 있는 기능으로 간주되었습니다.
문제는 JDK 17까지는, 이러한 필터를 사용하여 모든 스트림 작성자가 참여해 필터를 설정하거나, 단일 정적 JVM 와이드 필터를 사용해야 한다는 것입니다.
이러한 제한으로 인해 타사 라이브러리를 너무 포괄적이거나, 너무 제한적으로 만들지 않고, 이 실현 필터를 사용하기가 어렵습니다.
JEP 415는 동적 및 컨텍스트 지정 버스트 스트림 필터(burst stream filter)를 생성할 수 있는, 구성 가능한 JVM-wide 필터 팩토리를 도입하여, 모든 스트림 작성자의 참여가 필요하지 않게 만듭니다.

***
> Reference
* https://www.oracle.com/java/technologies/javase/8-whats-new.html
* http://jmlim.github.io/java/2018/12/13/java8-datetime-example/
* https://www.hungrydiver.co.kr/bbs/detail/develop?id=2
* https://www.popit.kr/%EB%82%98%EB%A7%8C-%EB%AA%A8%EB%A5%B4%EA%B3%A0-%EC%9E%88%EB%8D%98-java9-%EB%B9%A0%EB%A5%B4%EA%B2%8C-%EB%B3%B4%EA%B8%B0/
* https://itstory.tk/entry/Java-10-%EC%8B%A0%EA%B7%9C-%EA%B8%B0%EB%8A%A5%ED%8A%B9%EC%A7%95-%EC%A0%95%EB%A6%AC
* https://dreamchaser3.tistory.com/4
* https://daddyprogrammer.org/post/10411/jdk-roadmap-change-jdk9-11/
* https://dev-kani.tistory.com/21
* https://luvstudy.tistory.com/125
* https://advancedweb.hu/new-language-features-since-java-8-to-17/
* https://velog.io/@riwonkim/Java-17%EB%A1%9C-%EC%A0%84%ED%99%98%EC%9D%84-%EA%B3%A0%EB%A0%A4%ED%95%B4%EC%95%BC-%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0
* https://blog.naver.com/gngh0101/222396980468
* https://marrrang.tistory.com/16