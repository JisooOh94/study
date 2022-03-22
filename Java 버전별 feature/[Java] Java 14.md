# Java 14
```java
* NUMA-Aware Memory Allocation for G1
* JFR Event Streaming
* Non-Volatile Mapped Byte Buffers
* Helpful NullPointerExceptions
* Switch Expressions (Standard)
* ZGC on macOS (Experimental)
* ZGC on Windows (Experimental)
```
### NUMA-Aware Memory Allocation for G1 [[ref]](https://sangheon.github.io/2020/11/03/kor-g1-numa.html) [[JEP 345]](https://openjdk.java.net/jeps/345)
* Non-Uniform Memory Access (NUMA)
  * 멀티프로세서 시스템에서 사용되고 있는 컴퓨터 메모리 설계 방법중의 하나로, 프로세서마다 개별적인 로컬메모리를 할당하여, 메모리 접근에 대한 경합 상황을 해소한 구조
  * 기존 Symmetric Multi Processor(SMP) 구조는 모든 프로세서가 공유 메모리와 공유 버스를 사용하기 때문에 한번에 하나의 프로세서만 메모리에 접근할 수 있어 전체적인 프로세서의 성능 저하 발생
    * 공용 BUS 로 사용되는 PCLE 방식의 BUS 는 이더넷 통신처럼 한 노드가 BUS 사용시, 신호 중첩을 방지하기 위해 다른 노드들은 BUS 사용할 수 없음
  * NUMA 는 프로세서마다 별도의 메모리 공간(로컬 메모리) 할당하여 사용하므로, 프로세서들이 동시에 로컬 메모리에 접근이 가능해져, 대기로 인한 지연이 없어 병목 해소

![image](https://user-images.githubusercontent.com/48702893/159422211-d7ef96a4-5890-4ac9-afd2-63a729616d6b.png)

* G1GC 에서 쓰레드에 heap region 할당시, 최대한 로컬 메모리를 쓰도록 최적화
  * Eden 영역 region 할당
    * NUM-aware 전에는 전체 프로세서가 하나의 공유 메모리를 사용하므로, 하나의 MutatorAllocRegion(할당 가능한 region list) 인스턴스만 보유
    * 따라서, 쓰레드가 JVM에 메모리 요청시, 단일 MutatorAllocRegion 인스턴스에서 메모리 할당
    * NUMA-aware 이후 NUMA 노드(프로세서 + 로컬메모리) 수 만큼의  보유
    * 쓰레드가 JVM에 메모리 요청시, G1 GC는 쓰레드의 NUMA 노드를 확인하여, 동일한 NUMA 노드를 가진 MutatorAllocRegion에서 메모리 할당
      ![image](https://user-images.githubusercontent.com/48702893/159226256-0c5051e6-2d57-484a-bedc-606911a04546.png)
  * Survivor 영역 evacuatio region 할당
    * Survivor region 도 마찬가지로 NUMA 노드 수 만큼의 SurvivorGCAllocRegion 보유
    * GC 과정에서, young live object 들의 survivor region 으로의 evacuation 시, 동일한 NUMA 노드를 가진 SurvivorGCAllocRegion에서 region 할당받아 evacuation 실행
    * evacuation 작업시 사용하는 PLAB(Promotion Local Allocation Buffer) 또한 동일한 NUMA 노드의 region 에 저장
    * Old GC
      * Numa Node 사용량 불균형 문제 및 성능 향상 효과 미미로 NUMA 사용하지 않음
      
* Benchmark
![image](https://user-images.githubusercontent.com/48702893/159228605-33b19fb0-af80-45ae-97e4-f08400035984.png)

### JFR Event Streaming [[JEP 349]](https://openjdk.java.net/jeps/349)
* JavaFlight Recorder 에서 디스크에 기록하는 모니터링 데이터를 실시간으로 조회할 수 있는 jdk.jfr.consumer 패키지 제공
* .jfr 파일을 생성하여 저장하는것이 아닌, 디스크 repository 에 기록되는 데이터를 실시간으로 바로 조회하기때문에 오버헤드가 적음(파일에 쓰지 않고 조회한다는 의미로 추정)
```java
try (var rs = new RecordingStream()) {
  rs.enable("jdk.CPULoad").withPeriod(Duration.ofSeconds(1));
  rs.enable("jdk.JavaMonitorEnter").withThreshold(Duration.ofMillis(10));
  rs.onEvent("jdk.CPULoad", event -> {
    System.out.println(event.getFloat("machineTotal"));
  });
  rs.onEvent("jdk.JavaMonitorEnter", event -> {
    System.out.println(event.getClass("monitorClass"));
  });
  rs.start();
}
```
* 어플리케이션 모니터링 데이터를 별도의 모니터링 서비스(e.g. grafana, datadog) 에서 사용하고자 할 경우 유용하리라 추정

### Non-Volatile Mapped Byte Buffers [[JEP 352]](https://openjdk.java.net/jeps/352) [[ref]](https://www.linkedin.com/pulse/understanding-java-14-nvm-feature-vincent-vauban/)
* Disk 의 데이터를 Mapped Byte Buffer 로 로드 할 수 있는 FileChannel 기능 추가
* MappedByteBuffer 를 통해 direct 로 Disk 데이터에 접근할 수 있게 함으로서, Disk IO 성능 개선
  * MappedByteBuffer 는 메모리 공간에 direct 로 매칭
  * FileChannel 을 통해, MappedByteBuffer 에 로드된 메모리 공간 데이터를 직접 수정 가능

### Helpful NullPointerExceptions [[JEP 358]](https://openjdk.java.net/jeps/358) [[ref]](https://www.baeldung.com/java-14-nullpointerexception)
* NPE 예외 발생시, 예외 발생 원인 메시지를 기존보다 상세히 출력함으로서 디버깅 및 유지보수를 용이하게 함
* AS-IS
```java
Exception in thread "main" java.lang.NullPointerException
        at com.baeldung.java14.npe.HelpfulNullPointerException.main(HelpfulNullPointerException.java:10)
```
* TO-BE
```java
Exception in thread "main" java.lang.NullPointerException: 
  Cannot invoke "String.toLowerCase()" because the return value of 
"com.baeldung.java14.npe.HelpfulNullPointerException$PersonalDetails.getEmailAddress()" is null
  at com.baeldung.java14.npe.HelpfulNullPointerException.main(HelpfulNullPointerException.java:10)
```

### Switch Expressions [[JEP 361]](https://openjdk.java.net/jeps/361)
* Java 12, 13에서 2번의 Preview를 통해 강화된 Switch 표현식이 Java 14에서 표준화
* arrow, yield 를 새롭게 추가하여, switch 문에서 바로 값 반환이 가능하도록 수정
```java
//AS-IS
int foo;
switch(bar) {
    case 1 :
        foo = 1;
        break;
    case 2 :
        foo = 2;
        break;
}

//TO-BE
int foo = switch(bar) {
    case 1 :
        yield 1;
    case 2 :
        yield 2;
}
```
* 값 반환시, arrow(->) 를 사용하여 yield; 생략 가능
```java
int foo = switch(bar) {
    case 1 -> 1;
    case 2 -> 2;
    default -> 3;
}
```
* arrow 표현식에서 구문 표현시, '{}' 추가 후, yeild 로 반환
```java
int foo = switch(bar) {
    case 1 -> 1;
    case 2 -> 2;
    default -> {
        logger.info(3);
        yeild 3;
    };
}
```
* 조건이 여러개일 경우, ',' 로 구분하여 하나의 case 문에서 처리 가능
```java
int foo = switch(bar) {
    case 1,2 -> 1;
    case 3,4 -> 2;
    default -> {
        logger.info(3);
        yeild 3;
    };
}
```