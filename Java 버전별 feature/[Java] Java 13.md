# Java 13
```java
* Dynamic CDS Archives
* ZGC: Uncommit Unused Memory (Experimental)
* Reimplement the Legacy Socket API
* Switch Expressions (Preview)
* Text Blocks (Preview)
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