# Java 12
```java
* Shenandoah: A Low-Pause-Time Garbage Collector (Experimental)
* Microbenchmark Suite
* Switch Expressions (Preview)
* JVM Constants API
* One AArch64 Port, Not Two
* Default CDS Archives
* Abortable Mixed Collections for G1
* Promptly Return Unused Committed Memory from G1
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