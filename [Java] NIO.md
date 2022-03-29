# OIO
* 하나의 클라이언트 Socket마다 하나의 스레드를 할당하여 소켓통신하는 네트워크 I/O 방식

<img src="https://user-images.githubusercontent.com/48702893/131709074-5bd400ed-3ac0-4499-a166-0dca9773f9ee.png" width="400" height="200"> 

## OIO 단점
### stream 기반 I/O
* streaming 방식으로 데이터를 처리하여, 1, 또는 그 이상의 byte 단위로 데이터 I/O 수행
* 작은 크기의 데이터를 read/write 할때마다 Context 전환 및 데이터 복사, Disk I/O 가 수행되어 많은 부하 발생
* 따라서 보편적으로 버퍼링을 지원하는 IO stream(e.g. BufferedInputStream, BufferedOutputStream) 으로 IO 수행
  > 데이터를 미리 read/write 하여 버퍼에 저장해두고, 버퍼가 가득차거나 스트림이 닫혔을때 한번에 readl/write 수행

### Kernel buffer 복사
* JVM 은 유저모드에서 커널 영역 버퍼에 접근하는 Direct Buffer Handling 이 불가능하여, 커널 버퍼 데이터를 JVM 프로세스 메모리로 불러온 후에야 작업 가능 
* 이로인해 I/O 작업시 불가피하게 여러번의 context 전환 및 데이터 복사 수행
  * 파일에서 데이터를 read 하여 소켓에 write 하는 작업의 경우, 총 4번의 context 전환 및 데이터 복사 수행
  1. 프로세스가 데이터 read 요청, 커널모드로 전환하여 read 시스템콜 호출 
  2. 디스크 컨트롤러가 디스크로부터 데이터를 읽어 DMA(Direct Memory Access) 컨트롤러에게 전달, DMA 컨트롤러가 커널 버퍼에 데이터 저장
  3. 요청된 양만큼 커널 버퍼에서 JVM 프로세스 버퍼로 데이터 복사, 유저모드로 전환하여 read 시스템콜 반환
  4. send 시스템 콜 호출하여 커널모드로 전환, 전송할 데이터를 프로세스 버퍼에서 커널버퍼로 복사
  5. DMA 컨트롤러가 커널 버퍼에서 프로토콜 엔진으로 데이터 복사, 유저모드로 전환하여 send 시스템콜 반환

  <img src="https://user-images.githubusercontent.com/48702893/160350695-2ca44b61-fc77-4d41-9bf8-e2af9777e162.png" width="300" height="300">

* 위 과정에서, 중복 복사 + 빈번한 context 전환으로 인한 불필요한 오버헤드 발생
* 디스크 > 커널 버퍼로의 데이터 저장은 CPU 가 아닌, DMA 컨트롤러가 수행하여 오버헤드가 거의 없는 반면에, 커널 버퍼 > JVM 프로세스 버퍼로의 데이터 복사는 CPU 가 수행하므로 큰 부하 발생
* 또한, 사용이 끝난 JVM 내부 버퍼 메모리 영역은 GC 대상이 되어 오버헤드 추가

### Blocking I/O
* Java IO 패키지는 Blocking 방식으로 동작하여 효율성 떨어짐
```java
Socket clientSocket = new ServerSocket(8080).accept();   //소켓 통신이 established 될 때까지 block
BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

String request = in.read();                         //IO 로 인한 blocking
out.println(processRequest(request);                //IO 로 인한 blocking
```

<br>

# NIO
* EventLoop(NioEventLoop) 및 IO 멀티플렉싱(Selector)을 통해 여러 클라이언트 요청을 단일 스레드로 처리하는 방식

<img src="https://user-images.githubusercontent.com/48702893/131709571-af197f5c-b8b5-405c-bf75-7d4f78d63ab6.png" width="300" height="300">

## NIO 동작 방식
* 1개의 EventLoop 스레드와 다수의 Worker 스레드로 동작
* 메인 스레드는 IO 요청 전송 후 다른 작업 수행
* IO 요청에 대한 이벤트 발생(응답 수신, 에러 발생등) 시 이벤트 큐에 저장
* 이벤트 루프 스레드(NioEventLoop)가 지속적으로 이벤트 큐를 확인(selector.select()하여 이벤트가 적재되어있을시(selector.selectedKeys()), call stack 에 옮겨, 스레드 할당하여 처리

<img src="https://user-images.githubusercontent.com/48702893/131711099-4701c5ac-fe60-47b5-a206-d12231e8231d.png" width="500" height="200">
<img src="https://user-images.githubusercontent.com/48702893/129528788-c0374bff-8735-4d9e-8c69-021a62b0d642.gif" width="200" height="200">

## Selector
* 등록된 복수 개의 채널에서 이벤트(데이터 수신)를 모니터링 하는 장치
* 하나의 스레드로 여러 채널을 관리하고 처리할 수 있는 장치 (Reactor 패턴 활용)
  * Reactor 패턴 :이벤트에 반응하는 객체를 만들고 이벤트가 발생하면 해당 객체가 반응하여 해당 이벤트에 맞는 핸들러와 매핑시켜서 처리하는 구조
    리눅스환경에선 2.6 이상 부터는 SelectorProvider를 통해 epoll을 지원한다. 이전은 poll
* NioEventLoop 스레드가 selector.select() 지속적으로 호출하며 Channel 에서 발생하는 이벤트 모니터링
* Non-blocking + Synchronous 로 동작(pooling 방식), NIO2 부터 완전히 Non-blocking + Asynchronous 로 동작 (AIO)

## NIO 장점
### channel 기반 I/O
* buffer 기반으로 동작하는 channel 을 통해 I/O를 처리하므로 stream 기반으로 동작하는 OIO 에 비해 I/O 성능이 좋음
* 단뱡향인 stream 과는 달리 channle 은 양방향이어서 I/O 구분없이 하나로 사용 가능 

### Direct Buffer Handling [[ref]](http://eincs.com/2009/08/java-nio-bytebuffer-channel/)
* 커널 버퍼에 직접 접근하여 read/write 할 수 있는 클래스(ByteBuffer)를 제공
  * JVM 의 힙영역이 아닌, 운영체제의 커널영역에 바이트 버퍼 생성
  * nio channel 을 이용한 I/O 시 buffer 로 사용
  * 힙버퍼에 비해 생성이 오래 걸리므로, Buffer Pool 을 이용하여 재사용 권장
  ```java
  // use
  ByteBuffer directBuf = ByteBuffer.allocateDirect(10);
  ```
* 이를통해 JVM 프로세스 버퍼로의 복사로 인해 발생하던 여러가지 부하 해소 가능
  * 중복 복사로 인한 부하
  * 복사 작업시 사용하던 CPU 리소스
  * 복사 작업으로 인한 호출 스레드 blocking

> cf) NIO Channel이 함상 Non-Blocking 으로 동작하는것은 아니다.
> * NIO 에서 제공하는 채널중 WritableByteChannel. ReadableByteChannel 을 구현한 Channel 은 Blocking 방식으로[[ref]](https://docs.oracle.com/javase/7/docs/api/java/nio/channels/ReadableByteChannel.html), AsynchronousChannel, AsynchronousByteChannel 을 구현한 Channel은 Non-Blocking 으로 동작
> * e.g File I/O
> ```   
> //Files.java
> public static BufferedReader newBufferedReader(Path path, Charset cs) throws IOException {
>     CharsetDecoder decoder = cs.newDecoder();
>     Reader reader = new InputStreamReader(newInputStream(path), decoder);
>     return new BufferedReader(reader);
> }
> 
> public InputStream newInputStream(Path path, OpenOption... options) throws IOException {
>     ...
>     return Channels.newInputStream(Files.newByteChannel(path, options));
> }
> 
> public static SeekableByteChannel newByteChannel(Path path, OpenOption... options) throws IOException { ... }
> ```

### Non-Blocking I/O
* 하나의 selector 가 여러개의 Channel I/O 작업을 Non-Blocking 으로 효율적으로 처리 가능
  * 입출력 작업 준비가 완료된 채널만 선택해서 워커 스레드가 처리
* 적은 수의 스레드로 더 많은 연결을 처리할 수 있어 스레드 생성 비용 절약 및 컨텍스트 스위치에 대한 오버헤드 감소

### NIO 단점
* 순수 자바 NIO 라이브러리를 직접 사용해 네트워크 통신을 구축하기엔 생산성이 떨어짐 > 주로 Netty 로 사용
* 비즈니스 로직의좋은 선택일 수 있다. 부하가 클 경우, 워커스레드의 작업 효율이 떨어져 IO 후처리 대기하는 데이터가 쌓이게 되고, OOM 발생 가능
* IO 작업 대상 데이터 크기가 큰 경우, 버퍼 allocation 부하 증가
  * 연결 클라이언트 수가 적고 전송되는 데이터가 대용량이면서 순차적으로 처리될 필요성이 있는 경우 IO로 구현하는 것이 더 효율적일 수 있음

<br> 

# IO 성능 개선을 위한 OS 장치
## Scatter / Gather
* 흩어져 있는 메모리 공간의 데이터들을 연속적인 공간으로, 연속적인 공간의 데이터들을 흩어져있는 메모리 공간으로 전송을 가능하게 해주는 장치
* 하나의 channel 에서 여러개의 버퍼에 데이터를 read/write 할때, SystemCall 횟수를 줄이기 위해 주로 사용
  * 각각의 버퍼별로 개별적으로 read/write 수행시, SystemCall 이 여러번 호출되어 부하 증가
  * read/write 작업시, 작업 대상 버퍼목록을 한번에 channel 에 넘겨주어, 한번의 SystemCall 로 여러 버퍼에 read/write (Scatter Read, Gather Write)수행
    * OS 에서 최적화된 로직에 따라 버퍼들을 순차적으로 read/write
    ```java
    //http 메시지 헤더와 바디를 별도의 버퍼에 구분하여 저장하고자 할 경우, Scatter Read 사용
    ByteBuffer header = ByteBuffer.allocate(128);
    ByteBuffer body = ByteBuffer.allocate(1024);
    
    ByteBuffer[] httpMessageBuffers = new ByteBuffer[]{header, body};
    
    channel.read(httpMessageBuffers);
    ```

## 가상메모리
* OS 에서 제공하는 전통적인 가상메모리 시스템에서 오는 장점(실제 물리 메모리 보다 더 큰 가상 메모리 사용 가능) 외에 NIO 에서 제공하는 별도의 가상메모리 시스템 
* 여러 개의 가상 메모리 주소가 하나의 동일한 물리 메모리 공간을 참조하는 기능
* 이를 통해, 유저 영역 버퍼의 가상메모리가 커널 영역 버퍼에서 참조하는 물리메모리와 동일한 주소를 참조하게 함으로서 커널 영역 버퍼 > 유저 영역 버퍼로의 데이터 복사 생략 가능
* Java 에선 지원하지 않음

<img src="https://user-images.githubusercontent.com/48702893/159925940-6dac67be-a57b-4d4b-b296-5e610aea526b.png" width="400" height="200">

## zero-copy [[ref]](https://developer.ibm.com/articles/j-zerocopy/)
* 위의 가상메모리 기능은 Java 에선 지원하지 않으나 그와 비슷한 기능을 수행하는 zero-copy 를 java nio에서 제공
* 디스크 데이터를 네트워크로 전송시 수반되는 데이터 카피 최소화 기능
* Input channel 에서 읽은 디스크 데이터를 유저 영역 버퍼로의 복사 없이, read 버퍼에서 바로 소켓 버퍼로 복사하여 Context 전환 비용 및 복사 비용 절약

<img src="https://user-images.githubusercontent.com/48702893/160544864-bc65e5ef-e328-4f0f-befd-1b332b00dd3d.png" width="400" height="200">

* linux kernel 2.4[[changelog]](https://mirrors.edge.kernel.org/pub/linux/kernel/v2.4/ChangeLog-2.4.4) 이후 네트워크 카드가 gather operation 을 지원하면서, 소켓 버퍼에 데이터 복사 대신 read 버퍼 descriptor 를 저장함으로서 데이터 복사 과정 생략

## 메모리 맵 파일
* 파일 데이터가 저장되어있는 디스크 공간을 메모리 공간으로 사상 시키는 기능
* 파일이 메모리 공간에 매핑되면, 실제 파일 데이터가 메모리에 로드 된것처럼 read/write 가능
* 전통적인 File I/O 를 통한 디스크 read/write 보다 성능이 더 좋음
  * 전통적인 File I/O 는 디스크 조회하기 위해 여러번 Context 전환이 수행되어 부하 발생
  * 메모리 맵 파일은 4KiB 단위로 디스크 데이터를 미리 메모리로 불러올 때 발생하는 부하 외의 모든 작업이 실제 메모리상에서 이루어져 Context 전환 불필요
* 대용량 데이터 처리시에도, 작업에 필요한 부분만 메모리에 로드하여 수행하므로 효율적
* File I/O 시 메모리 맵 파일을 통해 디스크 - 커널 영역 버퍼 - 유저 영역 버퍼를 하나의 가상 메모리 공간으로 매핑시켜 커널 영역 버퍼로의 복사도 생략 가능
* Java 에선 nio 의 FileChannel.map 을 통해 MMF 사용 가능 [[ref]](https://docs.oracle.com/javase/7/docs/api/java/nio/channels/FileChannel.html#map(java.nio.channels.FileChannel.MapMode,%20long,%20long))
  * 작은 크기의 데이터일경우, MMF 를 사요하기보다 Disk I/O 를 수행하는게 더 효율적일 수 있음
  * ByteBuffer 의 GC 외에는 명시적으로 MMF 을 unmap 할 수 있는 방법이 없기때문에 실제 서비스에서 사용하기엔 부적절해보임 [[ref]](https://mapdb.org/blog/mmap_files_alloc_and_jvm_crash/)
```java
public abstract MappedByteBuffer map(MapMode mode, long position, long size) throws IOException;
```

***
> Reference <br>
> * https://jess-m.tistory.com/23 <br>
> * https://coding-start.tistory.com/318 <br>
> * https://brunch.co.kr/@myner/50
> * https://velog.io/@maketheworldwise/IO%EA%B0%80-%EB%8A%90%EB%A6%B0-%EC%9D%B4%EC%9C%A0%EA%B0%80-%EB%AD%98%EA%B9%8C-NIO%EB%8A%94-%EB%AD%98%EA%B9%8C
> * https://homoefficio.github.io/2016/08/06/Java-NIO%EB%8A%94-%EC%83%9D%EA%B0%81%EB%A7%8C%ED%81%BC-non-blocking-%ED%95%98%EC%A7%80-%EC%95%8A%EB%8B%A4/#about
> * https://hbase.tistory.com/37
> * https://aibees.github.io/Java-NIO/
> * https://hamait.tistory.com/421
> * https://jongmin92.github.io/2019/02/28/Java/java-with-non-blocking-io/
> * https://jongmin92.github.io/2019/03/03/Java/java-nio/