# Java 12
```java
Shenandoah: A Low-Pause-Time Garbage Collector (Experimental)
Microbenchmark Suite
Switch Expressions (Preview)
JVM Constants API
One AArch64 Port, Not Two
Default CDS Archives
Abortable Mixed Collections for G1
Promptly Return Unused Committed Memory from G1
```

### Microbenchmark Suite [[JEP 230]](https://openjdk.java.net/jeps/230) [[ref]](https://blog.idrsolutions.com/2019/03/how-java-12-has-made-microbenchmarking-easier/)
* JDK 개발자들을 위한 JVM 소스코드 마이크로 벤치마크 모음
* JMH(Java Microbenchmark Harness) 를 기반으로 작성됨

> Add a basic suite of microbenchmarks to the JDK source code, and make it easy for developers to run existing microbenchmarks and create new ones.

### JVM Constants API [[JEP 334]](https://openjdk.java.net/jeps/334)
* class constant pool 조작을 용이하게 해주는 새로운 패키지(java.lang.invoke.constant) 추가
* 클래스 파일 및 바이트 코드를 직접 조작하는 써드파티 라이브러리(e.g. lombok) 개발시 용이

> Introduce an API to model nominal descriptions of key class-file and run-time artifacts, in particular constants that are loadable from the constant pool.

### One AArch64 Port, Not Two [[JEP 340]](https://openjdk.java.net/jeps/340)
* 64 비트 ARM 아키텍처 빌드를 위해 중복으로 존재하던 2개의 포트(arm, aarch64)중 arm 포트 제거, aarch64를 64비트 ARM 아키텍처의 default 빌드 포트로 사용
* arm, aarch64 두가지 아키텍쳐에 중복으로 소모되던 유지보수 및 개발 리소스 절약 

### Default CDS Archives [[JEP 341]](https://openjdk.java.net/jeps/341) [[ref]](https://dzone.com/articles/39-new-features-and-apis-in-jdk-12)
* Oracle jdk 11 에서 사용 가능하던 개선된 CDS 기능이 openJdk 12 에 포함
* 기존 CDS 에서 공유를 지원하던 클래스 외에, --module-path 로 설정한 디렉토리 하위 사용자 정의 클래스 공유 기능 추가

### Abortable Mixed Collections for G1 [[JEP 344]](https://openjdk.java.net/jeps/344)
* G1GC 의 Mixed GC 단계 성능 개선을 위한 장치
* Mixed GC 수행시간이 최대 STW 시간 제한(-XX:MaxGCPauseMillis) 을 초과활경우, Mixed GC 중단 기능
  * Mixed GC 딘계에서 evacuate region 선택시, old gen region이 너무 많이 선택되면 evacuation 시간이 길어져 STW 시간 제한 초과 가능
* GC 대상을 80%의 필수 대상(Mandatory)과 20%의 선택적 대상(Optional)으로 구분
* 우선적으로 Mandatory evacution 수행, 완료 후 Optional evacuation 수행하되, STW 시간 제한에 도달할시, 중단

### Promptly Return Unused Committed Memory from G1 [[JEP 346]](https://openjdk.java.net/jeps/346)
* G1GC 는 메모리를 region 단위로 나누어 사용, GC 과정에서 region 이 free 되어도 OS 로 잘 반환하지 않음
* Full GC, Concurrent GC 과정에서 os 로 free region 반환하나, 두 GC 모두 잘 수행되지 않아 결과적으로 Free region 의 os 반환이 이루어지지 않음  
* 이는 사용 자원에 따라 요금을 부과하는 클라우드 환경에서 불필요한 비용 초래 
* 따라서, 설정한 시간동안 어플리케이션이 사용되지 않고 있다면, 사용하지 않는 free region 일부를 OS에 반환 및 JVM 힙 크기 조정

### API 추가
* Files.mismatch
  * 인자로 받은 두 개 Path에 위치한 파일을 비교하여, 처음으로 다른 부분의 위치를 반환하고 동일한 경우, -1L을 반환한다.
* NumberFormat.getCompactNumberInstace
  * Locale과 NumberFormat.Style에 따라서 다른 형태로 값을 반환해주는 함수가 추가되었다.
* Collectors.teeing
  * 두 개의 Collector와 하나의 BiFunction 총 세 개의 인자를 받는다. 처음 받은 두 개의 Collector의 결과를 세 번째 BiFunction에서 받아서 계산할 수 있다.
* String.indent
  * 입력한 n 만큼 들여 쓰기 하는 함수. 음수를 넣을 경우, 앞으로 당긴다.
* String.transform
  * 제공된 함수에 특정 String 인스턴스를 입력으로 제공하고 해당 함수에서 반환된 출력을 반환한다.

<br>

# Java 13
```java
Dynamic CDS Archives
ZGC: Uncommit Unused Memory (Experimental)
Reimplement the Legacy Socket API
Switch Expressions (Preview)
Text Blocks (Preview)
```

### Dynamic CDS Archives[[JEP 350]](https://openjdk.java.net/jeps/350)
* java 12에서 추가된 Default CDS Archives의 확장된 버전
* 시스템 클래스 로더, 사용자 정의 클래스로더까지 클래스 공유 확장
* 사용성 개선 및 CDS 적용 방법 간소화
  * AS-IS : 아카이브 덤프 명령어(java -Xshare:dump) 로 어플리케이션의 classlist 파일 및 classes.jsa 아카이브 파일 수동 생성 필요
  * TO-BE : 아카이브 덤프 필요 없이, 어플리케이션 실행 인수에 -XX:ArchiveClassesAtExit 추가하여 활성화

### Reimplement the Legacy Socket API[[JEP 353]](https://openjdk.java.net/jeps/353)
* JDK 1.0에 추가되었던 java.net.Socket, java.net.ServerSocket 의 성능 개선
* 레거시 Java 코드 및 네이티브 코드로 구현되어있던 기존 ServerSocket 구현체(PlainSocketImpl)는 유지보수 및 디거빙이 어렵고 여러가지 문제 내포
  * 스레드 스택을 I/O 버퍼로 사용하여 디폴트 스레드 스택 크기 증대 필요
  * 네이티브 코드로 작성된 Socket async close 메서드를 포함하여 여러가지 동시성 및 이식성 문제 존재
* PlainSocketImpl 을 NioSocketImpl 로 대체
  * 유지 보수 및 디버깅에 용이
  * NIO 라이브러리 구현체와 동일한 JDK 내부 인프라스트럭쳐를 사용하므로, 별도의 native 코드 불필요
  * 스레드 스택에 저장하여 관리하던 I/O 버퍼를 java 에서 제공하는 버퍼 캐시 메커니즘으로 대체
  * synchronized 메서드를 통한 동기화 처리를  java.util.concurrent locks 를 이용한 동기화처리로 대체하여 동시성 개선
* 기존 PlainSocketImpl 를 이용한 코드와의 하위호환이 가능하도록 개발되었으나, 몇가지 코너케이스가 존재하므로 적용전 확인 필요