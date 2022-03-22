# Java 11
```
* Nest-Based Access Control
* Dynamic Class-File Constants
* Improve Aarch64 Intrinsics
* Epsilon: A No-Op Garbage Collector
* Remove the Java EE and CORBA Modules
* HTTP Client (Standard)
* Local-Variable Syntax for Lambda Parameters
* Key Agreement with Curve25519 and Curve448
* Unicode 10
* Flight Recorder
* ChaCha20 and Poly1305 Cryptographic Algorithms
* Launch Single-File Source-Code Programs
* Low-Overhead Heap Profiling
* Transport Layer Security (TLS) 1.3
* Deprecate the Nashorn JavaScript Engine
* Deprecate the Pack200 Tools and API
```

### Epsilon GC [[JEP 318]](https://openjdk.java.net/jeps/318)
* 작동하지 않는 GC
* 메모리 할당을 처리하지만 메모리 회수를 수행하지 않아 GC 로 인한 오버헤드가 없음
* 사용 가능한 Java 힙이 모두 소모되면 OOM 에러와 함께 JVM 종료
* 주로 성능 테스트시 사용 (GC 로 인한 변인 제거)

### HTTP 클라이언트 표준화 [[JEP 321]](https://openjdk.java.net/jeps/321)
* java.net.http 패키지 추가
* Async, Http 2.0 지원하는 Http Client 사용 가능

### Nest-based access controls [[JEP 181]](https://openjdk.java.net/jeps/181)
* 중첩클래스에서 reflection 으로 외부 private 변수 접근 가능

### Flight Recorder [[JEP]](328)(https://openjdk.java.net/jeps/328)
* Java Application 의 실시간 상태 정보를 지속적으로 수집, .jfr 파일에 저장
    * Java Mission Control 에 로드하여 확인 가능
      ![image](https://user-images.githubusercontent.com/48702893/159233223-6b13d022-89b3-48be-ba2f-3fd1109e131c.png)
* 트러블 슈팅시 활용 가능
    * 어플리케이션 상태 정보를 지속적으로 저장하였다가, 에러(사고) 발생 후 원인 분석에 사용하는것이 항공기 블랙박스(Flight Recorder)와 유사
* 런타임중에 활성화 / 바활성화 가능
* 기존 모니터링 툴에 비해 low overhead

***

> Reference
> * https://itstory.tk/entry/Java-10-%EC%8B%A0%EA%B7%9C-%EA%B8%B0%EB%8A%A5%ED%8A%B9%EC%A7%95-%EC%A0%95%EB%A6%AC
> * https://dreamchaser3.tistory.com/4
> * https://daddyprogrammer.org/post/10411/jdk-roadmap-change-jdk9-11/
> * https://recordsoflife.tistory.com/277
> * https://blogs.oracle.com/javakr/post/jfr-v3