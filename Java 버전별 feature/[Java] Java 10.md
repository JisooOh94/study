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
* 기존 부트스트랩 클래스 로더만 지원하던 CDS를 플랫폼 클래스 로더(익스텐션 클래스 로더), 시스템 클래스 로더까지 확장

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