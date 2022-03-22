# Java 9
### Java Platform Module System(JPMS)
* 기존에 서비스 하위 도메인 단위로 분리하던 컴포넌트를 수행하는 기능별로 한번 더 분리하여 모듈화
* 동일 컴포넌트 내에서 기존 웹애플리케이션 소스만 여러개의 모듈(프로젝트)로 분리되어 관리
* 분리된 모듈간 기능 공유시, imports, exports 명령어를 통해 공유하고자 하는 기능이 개발되어있는 패키지 명시

* 장점
1. 웹애플리케이션 경량화
    * 사용자 정의 로직 뿐만 아니라, Java Platform API(SE,EE), 써드파티 라이브러리 또한 여러개의 모듈들로 분리
    * 이를 통해, 웹어플리케이션에 필요한 Java 모듈들만 명시하여 빌드에 포함
      > 기존에는 애플리케이션이 사용하지 않더라도 XML, SQL, Swing 같은 패키지는 항상 같이 배포

2. 패키지 캡슐화
    * 모듈에서 타 모듈에 공개할 패키지만 module descriptor 에 exports 명령어로 별도 명시하고, 그 외의 패키지는 은닉
    * 불필요하고 비인가된 모듈 간 결합을 방지하여 모듈의 올바른 사용을 유도

* 단점
1. 복잡도 증가 및 마이그레이션 비용
    * 기존 웹애플리케이션 소스의 패키지를 관심사별로 분리하고, 그들간의 의존관계 정리 필요
      > 로직에서 다른 패키지의 소스 사용한 부분 모두 확인 필요
    * 정리한 의존관계에 따라, 각 모듈마다 moduel descriptor에 export 할 패키지와 import 받을 패키지를 명시

2. 발생하는 비용에 비해 얻을 수 있는 이점이 애매함
    * 모듈로 분리하여도, 동일 컴포넌트에서 동작하므로 부하 분산등의 효과 없음
    * 웹애플리케이션 크기가 축소된다는 장점이 있으나, MSA 구조로 이미 분리된 웹애플리케이션의 크기가 서버 장비에 부담이 되는 경우는 드뭄

### jshell
* 테스트 프로젝트나 main 메소드없이 code를 신속하게 테스트 할 수 있는 대화식 REPL(Read-Eval-Print-Loop) 도구

* ![image](https://user-images.githubusercontent.com/48702893/156917635-f6b5d4b4-47e1-4548-8845-e251896900d5.png)

### unified jvm logging
* JVM 의 모든 이벤트를 로깅
* 기존에는 GC 를 비롯한 JVM 의 일부 이벤트만 로깅하였고, 그마저도 개별적인 로거에서 수행하였음
* JVM 의 모든 이벤트를 하나의 로거에서 로깅 수행

### etc
* interface에 private 메서드 추가
    * 코드 재사용률을 높이고,개발자의 의도를 명확히 전달하기 위해 Private method 를 제공
* 간결한 immutable collecion 생성자 추가
    * AS-IS : Collections.unmodifiable..()
    * TO-BE : List.of(), Map.of()
* Arrays.compare(a, b) static 메서드 추가
* Streams.takeWhile(), dropWhile() 메서드 추가
* CompletableFuture 개선 - 타임아웃과 지연 기능 추가
* HTTP/2 프로토콜과 WebSocket 기능을 지원하기 위해 HTTP 2 Client 도입

***

> Reference
> * https://lts0606.tistory.com/457
> * https://live-everyday.tistory.com/208
> * https://greatkim91.tistory.com/197