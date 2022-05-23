# EventLoop
* 이벤트 큐를 모니터링하는 무한루프 큐
* I/O 작업에 따른 이벤트가 발생하여 이벤트 큐에 저장시, EventLoopThread 가 큐에서 해당 이벤트 컨슘하여 후처리 로직을 callstack 에 등록, worker thread 가 처리
* 스레드 종류에 따라 단일 스레드 이벤트 루프, 다중 스레드 이벤트 루프로 구분

### 단일 스레드 이벤트 루프
* 이벤트를 처리하는 무한 루프 스레드가 하나인 이벤트 루프
* 이벤트 루프 구현이 단순하고, 하나의 이벤트 루프 스레드만 사용하므로 이벤트 발생 순서에 따른 처리 보장

### 다중 스레드 이벤트 루프
* 이벤트를 처리하는 무한 루프 스레드가 여러개인 이벤트 루프
* 멀티 코어 CPU 일때, 이벤트 루프 스레드가 병렬로 동작하므로 효율성 증대
* 구현이 복잡하고, 하나의 이벤트 큐를 여러개의 이벤트 루프 스레드가 사용하므로, 경합상황 및 동기화 이슈 발생에 따른 성능저하 뿐만 아니라 이벤트 발생 순서와 처리 순서가 달라질 수 있음

### Netty 이벤트 루프 스레드
* Netty 는 다중 스레드 이벤트 루프를 사용하면서 그 문제점을 이벤트 큐로 해결
* Netty 의 Channel 은 하나의 이벤트 루프에 등록되고, 각각의 이벤트 루프 스레드는 개별적으로 이벤트 큐를 가지고 있음
* 따라서 하나의 channel 에서 발생하는 이벤트는 모두 동일한 이벤트 큐에 저장되고, 동일한 이벤트 루프 스레드가 처리하므로 경합상황 해소 및 발생 순서에 따른 처리 순서 보장

<br>

# EventLoopGroup
* 스레드의 그룹으로서, channel 에 I/O 이벤트 발생시 컨슘하여 자신에 속해있는 워커 스레드에게 처리 위임(selector 내장)

![image](https://user-images.githubusercontent.com/48702893/163296653-f01ee5b1-89b7-4596-94bf-2fcc78ead2f7.png)

```
DefaultEventLoopGroup, 
EpollEventLoopGroup Epoll 방식으로 event 발생을 감지하는 EventLoopGroup. epoll 을 지원하는 os 에서만 사용 가능
KQueueEventLoopGroup, 
LocalEventLoopGroup, 
MultithreadEventLoopGroup, 
OioEventLoopGroup, 
ThreadPerChannelEventLoopGroup,
NioEventLoopGroup
```

### NioEventLoopGroup
* Java NIO 기반으로 event 를 pooling 하여 처리하는 EventLoopGroup 
* 단일 스레드 모델 / 멀티 스레드 모델 / 마스터 - 슬레이브 스레드 모델 로 구성할 수 있으며, 멀티코어일경우 마스터 - 슬레이브 스레드 모델로 구성하는것이 유리
  * 단일 스레드 모델의 경우, 1개의 스레드가 Acceptor, Worker 작업 모두 수행
```java
// 단일 스레드 모델
EventLoopGroup bossGroup = new NioEventLoopGroup(1);

bootstrap.group(bossGroup);

//ServerBootstreap.class
public ServerBootstrap group(EventLoopGroup group) {
    return this.group(group, group);
}

// 멀티 스레드 모델
EventLoopGroup bossGroup = new NioEventLoopGroup(128);

bootstrap.group(bossGroup);

// 마스터 - 슬레이브 스레드 모델
EventLoopGroup bossGroup = new NioEventLoopGroup(1);
EventLoopGroup workerGroup = new NioEventLoopGroup();

bootstrap.group(bossGroup, workerGroup);
```
  
* NioEventLoopGroup 인스턴스 생성시, 생성자 파라미터로 스레드 풀 크기를 설정하지 않으면, core 개수 * 2 로 설정됨

![image](https://user-images.githubusercontent.com/48702893/163350838-27fbb433-c37d-44aa-a6cb-43451f0c1bcc.png)

#### API
* setIoRatio(int ioRatio)
  * channel event triggered가 아닌, NioEventLoopGroup 의 execute 나 submit 을 직접 호출하여 Worker 스레드에 외부 작업을 요청할때, 리소스를 각각에 얼마나 할당할지 비율 설정 
  * channel event triggered task 와 external requested task 에 EventExecutor 를 100을 기준으로 설정된 비율에 따라 할당
  * default : 50

<br>

# Channel
* IO 작업 수행 대상인 엔티티(파일, 네트워크소켓 등)에 대한 Open 된 Connection
* Socket 의 추상화 객체로서 엔티티와 주고받는 데이터의 운송수단, 통로
* 연결(connect), 바인드(bind), 읽기, 쓰기 등의 I/O 작업을 수행하는 API 제공
* ChannelConfig 필드를 통해 BootStrap 에서 생성한 Cahnnel 의 속성 조회 및 변경 가능

### ChannelConfig[[doc]](https://netty.io/4.0/api/io/netty/channel/ChannelConfig.html#getMaxMessagesPerRead--)
* connectionTimeout
  * target 엔티티와의 connetion 수립 timeout 시간
  * default : 30000
* MaxMessagesPerRead
  * eventLoop 스레드가 한번의 channel 조회에서 최대로 읽을 메세지(데이터) 수
  * default : 1
* writeSpinCount
  * buffer 의 데이터를 channel 에 write 할때, worker 스레드가 한번의 write task에서 수행할 수 있는 최대 write 작업 횟수
  * writeSpinCount 만큼의 데이터를 write 하였음에도 버퍼의 데이터를 모두 쓰지 못했다면, 추가적인 write task 가 submitted 되며, 다음 scheduling 에서 실행됨
  * 메모리 효율 성 및 wirte throughput 성능 증대
  * default 16
* allocator
  * channel 에서 사용할 buffer 를 allocate 해주는 byteBufAllocator 설정
* recvByteBufAllocator
  * channel 에 수신 buffer 를 allocation 해주는 byteBufAllocator 설정
* autoRead
  * ChannelHandlerContext.read() 가 자동으로 호출되어, 어플리케이션에서 직접 read 를 호출할 필요가 없음
    * [[ChannelHandlerContext.read()]](https://netty.io/4.0/api/io/netty/channel/ChannelHandlerContext.html#read--) : channel 에서 data read 를 요청, channel에 데이터가 있어 read가 되었다면, channelRead 이벤트. channelReadComplete 이벤트 trigger
  * 자동으로 read 가 호출되므로, channel 에 데이터가 수신되면 자동으로 읽음
  * default : true
* writeBufferHighWaterMark
  * channel 의 쓰기 버퍼 최대 크기
  * 쓰기 버퍼에 버퍼링된 데이터 크기가 이 값을 초과할 경우, Channel.isWritable() 이 false 를 반환
* writeBufferLowWaterMark
  * channel 의 쓰기 버퍼가 가득차, 더이상 데이터를 버퍼링할 수 없을때, 다시 버퍼링을 수행할 데이터 크기
  * 쓰기 버퍼의 데이터크기가 이 값 이하로 떨어져야 Channel.isWritable() 에서 다시 ture 반환
* MessageSizeEstimator
  * channel 에서 메세지(byteBuf) 크기를 측정해주는 장치

### API
* alloc() : 설정된 ByteBufAllocator 조회
* bytesBeforeUnwritable() : isWritable 이 false 를 반환할때까지, 얼마나 많은 byte 를 버퍼에 쓸수 있는지 조회. 즉 쓰기 버퍼에 여유공간이 얼마나 있는지 조회
* bytesBeforeWritable() :  쓰기 버퍼가 가득차서 더이상 쓸 수 없을때, 앞으로 얼마나 더 버퍼의 데이터가 처리되어야 다시 쓰기 버퍼에 저장할 수 있는지 조회
* closeFuture() : channel 이 close 되었을때, 해당 이벤트를 조회할 수 있는 ChannelFuture 객체 조회
* eventLoop() : 이 channel이 등록된 EventLoop 스레드 조회
* flush() : 이 channel의 ChannelOutboundInvoker 에 pending 되고 있는 메시지 모두 flush 요청
* isOpen() : channel 이 port 에 바인딩되어 열려있는지 여부(target에 연결되어있는지는 상관없음)
* isRegistered() : channel 이 EventLoopGroup 에 등록되어 group 내 eventLoopThread 를 할당받았는지 여부
* isActive() : channel이 열려있고, connection 도 연결되었으며, eventLoopThread 도 등록되어 입출력을 수행할 상태가 되었는지 여부
* isWritable() : channel 에 등록되어있는 eventLoopThread 가 쓰기 작업 요청시, 즉시 수행할 수 있는 상태인지 여부
* localAddress() : channel 이 bind되어있는 로컬 host, port 정보
* remoteAddress() : channel 이 connected 되어있는 target entity 주소 정보
* pipeline() : channel 이 가지고있는 ChannelPipline 조회
* read() : channel 의 데이터 read 요청. 데이터 read에 성공하면 channelRead 이벤트 발생 
* unsafe() : unsafe operation 을 제공하는 internal-use-only 객체 조회
  * unsafe operation : java 의 previlleged opertaion 처럼, 사용자가 무분별하게 호출하면 위험한, java socket 의 core operation 을 캡슐화한 operation

### Channel 생명 주기
1. ChannelRegistered : 채널이 EventLoop에 등록됨
2. ChannelActive : 채널이 활성화(원격 노드에 연결되어 데이터 송수신 가능)
3. ChannelInactive : 채널이 원격 노드에 연결되어있지 않음
4. ChannelUnregistered : 채널이 생성되었지만 EventLoop에 등록되지 않음

<br>

# ChannelHandler
* I/O 이벤트를 처리 하는 인터페이스로서 I/O 이벤트 발생시 수행할 callback 로직 정의 객체 
* 인바운드 이벤트 핸들러, 아웃바운드 이벤트 핸들러로 구성

### Inboud Event Handler
* 소켓을 통해 연결되어있는 상대방측에서 어떠한 I/O 이벤트(channel 활성화, 데이터 수신 등)가 발생했을때 수행되는 Handler
* Bottom-Up 방식으로 동작하기 때문에 가장 먼저 등록한 Handler 에서부터 마지막에 등록한 Handler 순서로 동작

#### Inbound Event 발생 순서
1. channelRegistered : 채널이 이벤트루프에 등록되었을 때
2. channelActive : 채널이 활성화되어 데이터 송수신 준비 완료
3. channelRead : 데이터 수신
4. channelReadComplete : 데이터 수신 완료, channel 에 더이상 수신할 데이터 없음
5. channelInactive : 채널 비활성화
6. channelUnregistered : 채널이 이벤트 루프에서 제거

```java
2022-04-15 08:55:00.215  INFO 2068 --- [     worker-3-1] c.j.m.bo.netty.handler.MyServerHandler   : MyServerHandler channelRegistered called
2022-04-15 08:55:00.216  INFO 2068 --- [     worker-3-1] c.j.m.bo.netty.handler.MyServerHandler   : MyServerHandler channelActive called
2022-04-15 08:55:00.228  INFO 2068 --- [     worker-3-1] c.j.m.bo.netty.handler.MyServerHandler   : MyServerHandler channelRead called
...
2022-04-15 08:55:00.232  INFO 2068 --- [     worker-3-1] c.j.m.bo.netty.handler.MyServerHandler   : MyServerHandler channelReadComplete called
...
2022-04-15 09:01:44.879  INFO 2068 --- [     worker-3-1] c.j.m.bo.netty.handler.MyServerHandler   : MyServerHandler channelInactive called
2022-04-15 09:01:44.879  INFO 2068 --- [     worker-3-1] c.j.m.bo.netty.handler.MyServerHandler   : MyServerHandler channelUnregistered called
```

### Outbound Event Handler
* 클라이언트 자신이 어떠한 작업(연결 요청, 데이터 요청, 소켓 닫기 등)을 수행하여 I/O 이벤트가 발생했을떄 수행되는 Handler 
* Top-Down 방식으로 동작하기 때문에 가장 마지막에 등록한 Handler부터 가장 먼저 등록한 Handler 순서로 동작
* 객체 직렬화, 데이터 압축등의 전송할 데이터 가공작업 주로 수행

#### Outbound Event 발생 순서
1. bind
2. connect
3. write
4. flush
5. disconnect : target peer 와 커넥션 해제
6. close : channel close() 호출
7. deregister : EventExecutor에서 channel 등록 해지

### Inbound Outboud Handler
* Inbound Event, Outbound Event 모두에 수행되는 handler

![image](https://user-images.githubusercontent.com/48702893/163183553-974b97e3-ca62-4d5a-9313-37ec26d1e89e.png)

### ChannelHandler 생명주기
* handlerAdded	: Channelhandler가 ChannelPipeline에 추가됨
* handlerRemoved : Channelhandler가 ChannelPipeline에서 제거됨
* exceptionCaught : ChannelPipeline에서 처리 중에 오류 발생

<br>

# ChannelHandlerContext
* ChannelHandler 가 자신의 ChannelPipeline 이나 다른 ChannelHandler 와 상호작용할 수 있도록 해주는 클래스
* ChannelHandler 가 속해있는 ChannelPipeline 객체를 얻어 Handler 추가, 수정, 삭제등의 조작을 하거나 다음 ChannelHandeler 에게 event 전파 가능
* ChannelHandler 의 event 를 trigger 하여 handler 로직 수행 가능
```java
public class NettySocketServerHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    String readMessage = ((ByteBuf) msg).toString(Charset.forName("UTF8"));
    ctx.write(msg);
    System.out.println("message from received: " + readMessage);
  }
  
  public void removeHandler(ChannelHandlerContext ctx) {
      ctx.pipeline().remove(this);
  }
  ...
}
```

![image](https://user-images.githubusercontent.com/48702893/163421705-c0cadee1-85cf-4c55-ba2e-801bb6aa8b61.png)


### Event 전파 API
* ChannelPipeline 내의 다음 ChannelHandlerContext 에게 해당 event 전파

#### Inbound
* fireChannelRegistered() 
* fireChannelUnregistered() 
* fireChannelActive()
* fireChannelInactive()
* fireChannelReadComplete()
* fireChannelRead()
* fireChannelWritabilityChanged()
* fireUserEventTriggered(Object)
* fireExceptionCaught(Throwable)

```java
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public void channelRead(ChannelHandlerContext context, Object data) {
    logger.info("EchoServerHandler channelRead called. Call userEventTriggered");
    context.fireUserEventTriggered("");
  }
  ...
}

public class MyServerHandler extends ChannelInboundHandlerAdapter {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    logger.info("MyServerHandler userEventTriggered called");
  }
  ...
}

pipeline.addLast(new StringDecoder())
        .addLast(new StringEncoder())
        .addLast(new EchoServerHandler())
        .addLast(new MyServerHandler());

//result
2022-04-15 09:10:43.634  INFO 17152 --- [     worker-3-1] c.j.m.b.netty.handler.EchoServerHandler  : EchoServerHandler channelRead called. Call userEventTriggered
2022-04-15 09:10:43.635  INFO 17152 --- [     worker-3-1] c.j.m.bo.netty.handler.MyServerHandler   : MyServerHandler userEventTriggered called
2022-04-15 09:10:43.635  INFO 17152 --- [     worker-3-1] c.j.m.b.netty.handler.EchoServerHandler  : EchoServerHandler channelRead called. Call userEventTriggered
2022-04-15 09:10:43.635  INFO 17152 --- [     worker-3-1] c.j.m.bo.netty.handler.MyServerHandler   : MyServerHandler userEventTriggered called
...
```

#### Outbound
* bind(ChannelHandlerContext, SocketAddress, ChannelPromise)
* connect(ChannelHandlerContext, SocketAddress, SocketAddress, ChannelPromise)
* disconnect(ChannelHandlerContext, ChannelPromise)
* close(ChannelhandlerContext, ChannelPromise)
* deregister(ChannelHandlerContext, ChannelPromise)
* read(ChannelHandlerContext)
* flush(ChannelHandlerContext)
* write(ChannelHandlerContext, Object, ChannelPromise)

<br>

# ChannelHandlerAdapter
* ChannelHandler 인터페이스의 기본적인 기능들을 구현해놓은 Adapter 클래스로서
* ChannelHandler를 모두 처음부터 구현하는것이 아닌, HandlerAdapter 클래스를 상속받아 필요한 부분만 구현하여 개발 리소스 절약 가능
```java
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext context, Object data) {
        Channel channel = context.channel();
        String msg = (String)data;
        logger.info(msg);
    }
}
```

<br>

# ChannelInitializer
* 생성된 Channel 의 설정을 손쉽게 할 수 있도록 초기화 메서드(initChannel) 제공하며 주로 Channel Pipeline 구성시 사용
* Bootstrap.childHandler 를 통해 적용할 수 있으며, Bootstrap.init() 내부에서 자동으로 ChannelPipline 에 추가되어 channel 생성시 자동으로 수행됨  
```java
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast(new StringDecoder())
                .addLast(new StringEncoder())
                .addLast(new MyServerHandler());
    }
}

bootstrap.childHandler(new EchoServerInitializer());
```

<br>

# ChannelPipeLine
* ChannelHandler 체인 컨테이너로서, channel 에 이벤트 발생시 Handler 체인의 Handler 를 순차적으로 수행(Event 종류에 따라 시작위치와 방향이 달라짐)
* 다양한 핸들러를 자유롭게 결합하여 사용 가능

### API
* addAfter, addBefore, addFirst, addLast : Handler 추가
* first, firstContext, last, lastContext, get : Handler 조회
* remove, removeFirst, removeLast : Handler 제거
* replace : 특정 Handler를 다른 Handler 로 교체
* toMap : handler이름 - handler 의 key-value인 Map 으로 변환  
* deregister : EventExecutor 에 등록된 channel 을 해제 요청(아웃바운드 요청이 완료되었거나, 에러가 발생하여 실패했을때 호출)
* read, write, flush, writeAndFlush : ChannelPipeline 의 channel 에 데이터 read/write 요청 
* fireChannelActive, fireChannelInactive... : event 전파... 이나 테스트시 아무런 작업이 수행되지 않음 

![image](https://user-images.githubusercontent.com/48702893/163182381-074f779d-ccc6-4488-8eb2-9da53c87a15d.png)

<br>

# ChannelFuture
* 비동기 IO 작업의 결과를 담는 PlaceHolder
* IO 작업이 완료되었는지 확인할 수 있으며 작업 완료시 그 결과에 접근가능

<br>

# Bootstrap
* 스레드를 생성하고 소켓을 오픈하며, 생성되는 Channel 의 속성값을 설정하는 등 Netty Component 부트스트래팽 수행
* 클라이언트용(Bootstrap)과 서버용(ServerBootstrap)으로 구성

### Bootstrap(Client)


### ServerBootstrap(Server)
#### group
* ServerChannel의 EventLoopGroup을 설정
* 단일/다중 스레드 EvenLoopGroup 을 설정할 수도 있고, 마스터 - 슬레이브(핸들러 - 워커) 스레드 EventLoopGroup 으로도 설정 가능
  * public ServerBootstrap group(EventLoopGroup group)
  * public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup)
```java
EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("eventLoop"));
EventLoopGroup workerGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("worker"));

ServerBootstrap bootstrap = new ServerBootstrap();
bootstrap.group(eventLoopGroup, workerGroup);
```

#### channel
* 소켓 입출력 통로로 사용할 channel 종류 설정
  * LocalServerChannel : 하나의 자바 가상 머신에서 가상 통신을 위한 서버 소켓 channel
  * OioServerSocketChannel : 블로킹 방식의 OIO 서버 소켓 channel
  * NioServerSocketChannel : 논블로킹 방식의 NIO 서버 소켓 channel
  * EpollServerSocketChannel : 리눅스 커널의 epoll 입출력 모드를 사용하는 서버 소켓 channel
  * OioSctpServerChannel : SCTP 전송 계층을 사용하는 블로킹 모드의 서버 소켓 channel
    > SCTP : 스트림 제어 전송 프로토콜. TCP와 UDP의 특성을 같이 가지고 있고 TCP의 보안 문제를 해결한 프로토콜
  * NioSctpServerChannel : SCTP 전송 계층을 사용하는 논블로킹 모드의 서버 소켓 channel
  * NioUdtByteAcceptorChannel : UDT를 지원하는 논블로킹 모드의 서버 소켓 channel
    > UDT : UDP-based Data Transfer protocol이라는 뜻으로 UDP를 기반으로 하지만 데이터 전송의 신뢰성을 보장할 수 있는 대용량 데이터 전송 프로토콜
  * NioUdtMessageAcceptorChannel : UDT 프로토콜을 지원하는 블로킹 모드의 서버 소켓 channel

```java
bootstrap.channel(NioServerSocketChannel.class);
```

#### childHandler
* ServerChannel ChannelPipeline에  ChannelHandler 추가
* 주로 ChannelPipeline 을 초기화하는 ChannelInitializer 로 설정
```java
bootstrap.childHandler(new EchoServerInitializer());
```

#### option/childOption
* 서버 소켓(option), 서버에 접속한 클라이언트 소켓(childOption) 속성 설정
* ChannelOption 에 설정가능한 속성 이름이 미리 정의되어있음 [[ref]](https://blog.krybot.com/a?ID=01200-8f5d0935-3772-4947-a5af-106034fe0ca4) 
  * ALLOCATOR
    * ByteBuf allocator 설정
    * default : ByteBufAllocator.DEFAULT (v 4.0 : UnpooledByteBufAllocator, v 4.1 : PooledByteBufAllocator)
  * RCVBUF_ALLOCATOR
    * 수신 버퍼 allocator 설정
    * default : AdaptiveRecvByteBufAllocator.DEFAULT
  * MESSAGE_SIZE_ESTIMATOR
    * channel 에서 메세지(데이터) 크기를 측정해주는 장치 설정
    * ByteBuf, ByteBufHolder 의 크기를 측정하여 buffer 여유 공간 계산에 사용
  * CONNECT_TIMEOUT_MILLIS
    * target 엔티티와의 connetion 수립 timeout 시간 설정
    * default : 30000
  * MAX_MESSAGES_PER_READ
    * eventLoop 스레드가 한번의 channel 조회에서 최대로 읽을 메세지(데이터) 수
    * 다른 channel 들의 default value 는 1 이나, ServerChannel, NioByteChannel 은 default value 가 16 (쓰루풋 향상과 systemcall 호출 횟수를 줄여 성능 향상을 위해) 
  * WRITE_SPIN_COUNT
    * buffer 의 데이터를 channel 에 write 할때, worker 스레드가 한번의 write task에서 수행할 수 있는 최대 write 작업 횟수
    * default : 16
  * WRITE_BUFFER_HIGH_WATER_MARK
    * channel 의 쓰기 버퍼 최대 크기
  * WRITE_BUFFER_LOW_WATER_MARK
    * channel 의 쓰기 버퍼가 가득차, 더이상 데이터를 버퍼링할 수 없을때, 다시 버퍼링을 수행할 데이터 크기
  * ALLOW_HALF_CLOSURE
  * AUTO_READ
    * ChannelHandlerContext.read() 가 자동으로 호출 설정
    * default : true
  * SO_BROADCAST
  * SO_KEEPALIVE
    * TCP connection keepalive 설정
    * true 로 설정시 netty 가 자동으로 idle tcp connection 의 validity 검사 및 connection 유지
    * default : false
  * SO_SNDBUF : 송신 버퍼의 크기 설정
  * SO_RCVBUF : 수신 버퍼의 크기 설정
  * SO_REUSEADDR
    * 서버 localhost 주소 및 포트 공유 여부 설정
    * true로 설정시, 프로세스간 port 공유가 가능
    * 특정 포트를 할당받은 프로세스가 비정상 종료되었을떄, 포트가 바로 release 되지 않아 다른 프로세스가 해당 port 를 사용하지 못하는데, 이 option 설정시 타 프로세스가 바로 사용 가능 
  * SO_LINGER
    * 소켓이 close될 때 미처 전송되지 못한 데이터를 폐기할 것인지 데이터를 전송할 것인지 설정
    * -1 일경우 socket.close() 는 바로 return 하나, OS 에서 버퍼에 남아있는 데이터 전송 수행
    * 0 일경우, socket.close() 도 바로 return 하고, OS 도 버퍼에 남아있는 데이터 폐기
    * n 일경우, n 시간동안 socket.close() 는 blocking 되고, OS 에서 버퍼에 남아있는 데이터 전송 수행. n 시간 초과시 나머지 데이터 폐기
  * SO_BACKLOG
    * 동시에 수용 가능한 소켓 연결 요청수
    * default : 50
  * SO_TIMEOUT
  * IP_TOS
  * IP_MULTICAST_ADDR
  * IP_MULTICAST_IF
  * IP_MULTICAST_TTL
  * IP_MULTICAST_LOOP_DISABLED
  * TCP_NODELAY
    * 데이터를 송수신할 때 네이글 알고리즘 비활성화 여부 설정

```java
bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1024));
bootstrap.childOption(ChannelOption.MESSAGE_SIZE_ESTIMATOR, new DefaultMessageSizeEstimator(200))
```

<br>

# Codec
* ChannelHandler 의 일종으로서 전송할 데이터를 byte로 변환해주는 encoder 와 수신한 byte 를 원하는 포맷으로 파싱해주는 decoder 로 구성
* encoder 와 decoder 는 각각 ChannelInboundHandler 와 ChannelOutBoundHandler 구현
* 미리 잘 구현되어있는 다수의 코덱을 제공하여 개발자가 별도의 코덱을 정의해야하는 비용 절약

### 내장 코덱(netty 4.1 기준)
* base64 : Base64 인코딩 데이터 송수신 지원
* bytes : 바이트 배열 데이터 송수신 지원
* compression
  * 송수신 데이터의 압축을 지원하는 코덱
  * 여러가지 압축 알고리즘 지원
    * v4.0 : zlib, jzlib, gzip, snappy, v4.1 : bzip2, castle, l24, lzf, faseLz, bzip2
* marshalling : JBoss 마샬링 송수신 지원
* ProtoBuf : 구글의 포로토콜 버퍼를 사용한 데이터를 송수신 지원
* rtsp : 오디오 및 비디오 같은 실시간 데이터의 전달을 위해 만들어진 애플리케이션 레벨의 프로토콜 (real time streaming protocol) 지원
* sctp 
  * TCP가 아닌 sctp 전송 계층을 사용하는 코덱
  * bootstrap 에서 Channel type 에 NioSctpChannel 혹은 NioSctpServerChannel 로 설정해야 사용가능
* http : http 프로로콜을 지원
* spdy : Google Spdy프로토콜 지원
* HTTP/2 Codec
  * HTTP/2 Protocol을 지원하는 코덱
* String : 문자열 송수신을 지원하는 코덱
* Serialization : 객체를 네트워크로 직렬화 / 역직렬화를 지원하는 코덱
* MQTT Codec : MQTT Protocol을 지원하는 코덱
* HaProxy : Load Balance와 Proxy기능 을 제공하는 오픈 솔루션을 지원하는 코덱
* STOMP : STOMP(Streaming Text Oriented Messaging Protocol) Protocol을 지원하는 코덱

### Encoder
* 아웃바운드 데이터를 byte 형식으로 변환
* 내부적으로 템플릿 메서드 패턴을 사용하여 MessageToByteEncoder 를 상속받아 encode 메서드를 구현한 구현체에서 인코딩 수행
* encode(ChannelHandlerContext ctx, T msg, ByteBuf out)
  * msg : 인코딩 수행할 객체
  * out : 인코딩한 byte 데이터를 저장할 buffer
```java
public class ObjectEncoder extends MessageToByteEncoder<Serializable> {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    public ObjectEncoder() {
    }

    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        int startIdx = out.writerIndex();
        ByteBufOutputStream bout = new ByteBufOutputStream(out);
        bout.write(LENGTH_PLACEHOLDER);
        ObjectOutputStream oout = new CompactObjectOutputStream(bout);
        oout.writeObject(msg);
        oout.flush();
        oout.close();
        int endIdx = out.writerIndex();
        out.setInt(startIdx, endIdx - startIdx - 4);
    }
}
```

### Decoder
* 인바운드 데이터을 원하는 메세지 포맷으로 파싱
* 내부적으로 템플릿 메서드 패턴을 사용하여 ByteToMessageDecoder 를 상속받아 decode 메서드를 구현한 구현체에서 디코딩을 수행
* ByteBuf 에 디코딩할 수 있는 만큼(메세지 포맷 크기) 데이터가 쌓인후 디코딩 수행 
* decodeLast(ChannelHandlerContext ctx, ByteBuf in, List out)
  * ByteBuf in : 디코딩할 바이트가 저장되는 buffer
  * List out : 파싱된 객체가 저장장되는 list
* decodeLast(ChannelHandlerContext ctx, ByteBuf in, List out)
  * decodeLast 와 동일하나, channel 이 비활성화 된 후에 도착하는 패킷들을 파싱하기 위한 api
```java
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    ...

    protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = this.decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return in.readableBytes() < this.frameLength ? null : in.readSlice(this.frameLength).retain();
    }
}
```

<br>

# ByteBuf
* NIO 의 ByteBuffer 가 가지고있던 여러가지 문제점들을 개선하여 나온 Buffer

### ByteBuffer 의 문제점
* 데이터 read/write 인덱스가 분리되어있지 않음
  * 버퍼에 read/write 할 위치 인덱스를 하나로 관리하여 read/write 작업 전환시 인덱스를 수동으로 변경해주어야함
```java

```
* 버퍼의 사이즈가 고정적

* 버퍼풀 지원하지 않음

### ByteBuf 의 개선
* read/write 인덱스 분리
* 가변 크기 버퍼
* 버퍼풀 지원
 

* NIO 의 ByteBuffer 는 flip 호출로 인해 사용성이 떨어지고, 빈번한 GC 도 유발하여 성능을 떨어뜨리는등 여러가지 문제점 내포
* Netty 는 NIO 의 ByteBuffer 대신 새로운 ChannelBuffer 를 이용하여 ByteBuffer 의 문제점들을 해결하고, 여러가지 추가적인 기능을 제공하여 성능 및 사용성 향상
  * flip 호출 없이 buffer 조회
  * Zero Copy
  * Dynamic Buffer Type(StringBuffer와 같이 필요시 용량이 증가하는 버퍼)

***
> Reference
* https://sina-bro.tistory.com/15
* https://brunch.co.kr/@myner/49
* https://programmer.ink/think/netty-source-code-analysis-4-nioeventloopgroup.html
* https://wizardee.tistory.com/160
* https://brunch.co.kr/@myner/46
* https://blog.krybot.com/a?ID=01200-8f5d0935-3772-4947-a5af-106034fe0ca4