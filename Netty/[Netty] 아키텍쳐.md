# Netty
* Java의 NIO 를 이용한 비동기 Event-driven 방식의 네트워크 프레임워크

### 배경
* 순수 NIO만을 이용해서 네트워크 비즈니스 로직 작성은 매우 어려워 생산성도 떨어지고 버그 유발 가능성도 높아짐
* 그에따라, 어려운 NIO API를 내부에 숨겨 놓고, 추상화한 간단한 네트워크 통신 API를 제공함으로서 개발자가 비즈니스로직 개발에만 집중가능하게 함
  > TCP 및 UDP 소켓 서버와 같은 네트워크 프로그래밍을 크게 단순화하고 간소화

### Netty 핵심 기능
#### Event Model based on the Interceptor Chain Pattern
* Java NIO 를 이용한 타 프레임워크들은 사용자정의 이벤트 타입 추가시, 기존 비즈니스 로직에 영향이 가거나 혹은 아예 추가를 막음
* Netty 는 I/O 에 초점을 맞춘 잘 정의된 이벤트 모델을 제공하여, 개발자가 Netty 내부 코드나 코어 수정, 또는 기존 비즈니스 로직 수정 없이 사용자정의 이벤트 타입 쉽게 추가 가능
  * Netty 의 event 처리 pipeline 인 ChannelPipeline 은 Intercepting Filter 패턴을 구현하고, Pipeline 에 추가되는 EventHandler 인 ChannelHandler 에서 ChannelEvent 처리   
  * 이를 통해, 개발자가 이벤트 처리 방법 및 파이프라인 핸들러들의 상호작용 방식에 대해 완전한 제어 가능
```java
public class MyReadHandler implements SimpleChannelHandler {
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent evt) {
        Object message = evt.getMessage();
        // Do something with the received message.
        ...

        // And forward the event to the next handler.
        ctx.sendUpstream(evt);
    }
}
```

#### Universal Asynchoronous IO API
* 자바의 기존 I/O API는 수행하는 기능과 API 명세가 비슷함에도 서로간의 타입 호환이 전혀 되지 않음   
  * 예를 들어 java.net.Socket과 java.net.DatagramSocket은 어떠한 공통 부모 타입도 가지고 있지 않고, 완전히 다른 방식으로 소켓 I/O를 수행 
  * 또한, 자바 NIO API는 기존 OIO API와 호환되지 않으며, NIO 2와도 마찬가지
  * 이러한 I/O 구현체간 이식성 결여는 애플리케이션이 I/O 구현체에 의존성을 가지게 하며, 추후 I/O 구현체 변경도 어렵게 만듦
```
예를 들어, 서비스 대상 클라이언트의 수가 매우 적으며 OIO를 사용하여 소켓 서버를 작성하는 것이 NIO를 사용하는 것보다 훨씬 쉽기 때문에, 여러분은 OIO를 가지고 구현하고 싶어할 지도 모릅니다. 
하지만, 비즈니스가 기하 급수적으로 성장하여 서버가 동시에 수 만개의 클라이언트에게 서비스하기 시작했을때 여러분은 곤란한 상황에 처하게 될 것입니다. 
처음부터 NIO를 사용했을 수도 있었겠지만, 빠른 개발을 저해하는 NIO Selector API의 복잡성으로 인해 구현하는데 훨씬 더 많은 시간이 소요될 수도 있습니다.
```
* Netty는 Channel이라는 일관된 비동기 I/O 인터페이스를 가지고 I/O 구현체가 확장되기 때문에, 구현체간 유연한 변경이 가능
* Channel 을 통해 개발자는 I/O 구현체를 신경쓰지 않고 비즈니스 로직 개발에만 집중 가능 

#### ChannelBuffer
* NIO 의 ByteBuffer 는 flip 호출로 인해 사용성이 떨어지고, 빈번한 GC 도 유발하여 성능을 떨어뜨리는등 여러가지 문제점 내포
* Netty 는 NIO 의 ByteBuffer 대신 새로운 ChannelBuffer 를 이용하여 ByteBuffer 의 문제점들을 해결하고, 여러가지 추가적인 기능을 제공하여 성능 및 사용성 향상
  * flip 호출 없이 buffer 조회
  * Zero Copy
  * Dynamic Buffer Type(StringBuffer와 같이 필요시 용량이 증가하는 버퍼) 

#### 그 외 생산성 향상 장치
* 미리 잘 구현되어있는 다수의 코덱을 제공하여 개발자가 별도의 코덱을 정의해야하는 비용 절약
* 기존 NIO 에서 개발자가 SSLEngine 을 이용해 직접 구현해야했던 SSL 암호화를 SSLHandler 를 통해 간편하게 적용 가능
  * SSLHandler 는 SSLEngine 이 가지고있던 복잡성, 동기화 이슈등을 해결하여 세부적인 SSL 로직을 은닉하고 간편하게 사용가능한 API 제공
* HTTP 서버 개발을 위해 코어 로직(스레드 모델, 커넥션 생명 주기, 인코딩 방식 등) 까지 커스터마이징 할 수 있는 많은 클래스 및 기능을 제공하여, 비교적 쉽게 나만의 HTTP 서버 개발 가능 
  * 전체 미디어가 스트리밍 될 때까지 커넥션을 지속적으로 열어두어야 하는 미디어 스트리밍 서버 (예: 2시간짜리 영화)
  * 메모리 압박 없이 덩치 큰 파일 업로드를 허용하는 파일 서버 (예: 요청당 1GB 업로드)
  * 수많은 제3업체 웹서비스에 비동기로 연결하는 확장성좋은 클라이언트
* GRPC 코덱 모듈(ProtobufEncoder, ProtobufDecoder) 제공하여 손쉽게 Netty에 GRPC 연동 가능

***
> Reference
* https://12bme.tistory.com/172
* https://hbase.tistory.com/116