# Java 10
* 라이선스없이 상업적으로 사용할 수있는 마지막 무료 Oracle JDK

### Local-Variable Type Inference
* R-Value 타입 추론 기능을 통한 var 타입
* 지역변수에만 사용 가능
```java
var numbers = List.of(1, 2, 3, 4, 5); 

for (var number : numbers) {
    System.out.println(number); 
} 

for (var i = 0; i < numbers.size(); i++) { 
    System.out.println(numbers.get(i)); 
}
```

### Parallel Full GC for G1
* G1GC 의 Full GC 성능 개선
* 기존 single thread 기반 full gc 를 parallel thread 로 개선

### Application Class-Data Sharing
* 자바 프로세스간 클래스 공유 시스템
* 클래스를 공유 아카이브에 저장, 여러 자바 프로세스에서 클래스 사용
* 어플리케이션 stratup 시간 단축 및 메모리 사용량 최적화
* 기존 부트스트랩 클래스 로더만 사용 가능하던(CDS) 클래스 공유 아카이브를 시스템 클래스 로더까지 확장

### Thread-Local handshakes
* 개별 스레드단위로 stop 처리 및 콜밸 실행 할 수 있는 기술
* 기존에 VM Safepoint 가 선행되어야 했던 작업에서 Safepoint 없이 작업 수행 가능해짐 (작업 수행시 stop 시켜야할 스레드만 개별적으로 stop)
    * VM Safepoint : Stop the world, 모든 쓰레드 일시정지
    * Safepoint 선행 작업 : GC, Code deoptimization, Flushing code cache 등

### Heap Allocation on Alternative Memory Devices
* JVM Head 영역을 사용자 지정 메모리 장치에 할당 가능
* 비휘봘성 메모리 영역(SSD)에 할당하여 비정상 종료시에도 heap 데이터 유지하는것도 가능

### Root Certificates
* HTTPS 암호화 통신에 사용되는 인증서의 검증을 위해 Java 에 내장되어있는 인증 기관 목록

### Etc
* Time-Based Release Versioning: 자바 릴리즈 주기 변경으로 versioning format 변경
* Additional Unicode Language-Tag Extensions

<br>

# Java 11
```
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
Deprecate the Nashorn JavaScript Engine
Deprecate the Pack200 Tools and API
```

### Epsilon GC
* 작동하지 않는 GC
* 메모리 할당을 처리하지만 메모리 회수를 수행하지 않아 GC 로 인한 오버헤드가 없음
* 사용 가능한 Java 힙이 모두 소모되면 OOM 에러와 함께 JVM 종료
* 주로 성능 테스트시 사용 (GC 로 인한 변인 제거)

### HTTP 클라이언트 표준화
* java.net.http 패키지 추가
* Async, Http 2.0 지원하는 Http Client 사용 가능

### Nest-based access controls
* 중첩클래스에서 reflection 으로 외부 private 변수 접근 가능

### Flight Recorder
* Java Application 의 실시간 상태 정보를 지속적으로 수집, .jfr 파일에 저장
* 트러블 슈팅시 활용 가능
    * 어플리케이션 상태 정보를 지속적으로 저장하였다가, 에러(사고) 발생 후 원인 분석에 사용하는것이 항공기 블랙박스(Flight Recorder)와 유사
* 런타임중에 활성화 / 바활성화 가능
* 기존 모니터링 툴에 비해 low overhead

![image](https://user-images.githubusercontent.com/48702893/157260990-a85ccb5d-facb-4e05-86b4-597b8685da5e.png)

***

> Reference
> * https://itstory.tk/entry/Java-10-%EC%8B%A0%EA%B7%9C-%EA%B8%B0%EB%8A%A5%ED%8A%B9%EC%A7%95-%EC%A0%95%EB%A6%AC
> * https://dreamchaser3.tistory.com/4
> * https://daddyprogrammer.org/post/10411/jdk-roadmap-change-jdk9-11/
> * https://recordsoflife.tistory.com/277
> * https://blogs.oracle.com/javakr/post/jfr-v3