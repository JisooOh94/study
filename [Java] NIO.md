# OIO
* 하나의 클라이언트 Socket마다 하나의 스레드를 할당하여 소켓통신하는 네트워크 I/O 방식

<img src="https://user-images.githubusercontent.com/48702893/131709074-5bd400ed-3ac0-4499-a166-0dca9773f9ee.png" width="400" height="200"> 

### OIO 단점
#### stream 기반 I/O
* streaming 방식으로 데이터를 처리하여, 1, 또는 그 이상의 byte 단위로 데이터 I/O 수행
* 작은 크기의 데이터를 read/write 할때마다 Context 전환 및 데이터 복사, Disk I/O 가 수행되어 많은 부하 발생
* 따라서 보편적으로 버퍼링을 지원하는 IO stream(e.g. BufferedInputStream, BufferedOutputStream) 으로 IO 수행
  > 데이터를 미리 read/write 하여 버퍼에 저장해두고, 버퍼가 가득차거나 스트림이 닫혔을때 한번에 readl/write 수행

#### Kernel buffer 복사
* JVM 은 유저모드에서 커널 영역 버퍼에 접근하는 Direct Buffer Handling 이 불가능하여, 커널 버퍼 데이터를 JVM 프로세스 메모리로 불러온 후에야 작업 가능 
* 이로인해 I/O 작업시 불가피하게 여러번의 context 전환 및 데이터 복사 수행
> 파일에서 데이터를 read 하여 소켓에 write 하는 작업의 경우, 총 4번의 context 전환 및 데이터 복사 수행
> 1. 프로세스가 데이터 read 요청, 커널모드로 전환하여 read 시스템콜 호출 
> 2. 디스크 컨트롤러가 디스크로부터 데이터를 읽어 DMA(Direct Memory Access) 컨트롤러에게 전달, DMA 컨트롤러가 커널 버퍼에 데이터 저장
> 3. 요청된 양만큼 커널 버퍼에서 JVM 프로세스 버퍼로 데이터 복사, 유저모드로 전환하여 read 시스템콜 반환
> 4. send 시스템 콜 호출하여 커널모드로 전환, 전송할 데이터를 프로세스 버퍼에서 커널버퍼로 복사
> 5. DMA 컨트롤러가 커널 버퍼에서 프로토콜 엔진으로 데이터 복사, 유저모드로 전환하여 send 시스템콜 반환

![image](https://user-images.githubusercontent.com/48702893/160350695-2ca44b61-fc77-4d41-9bf8-e2af9777e162.png)

![image](https://user-images.githubusercontent.com/48702893/159923422-01afbef4-00df-4af2-8f2c-2fd1281f5f8e.png)

* 위 과정에서, 중복 복사 + 빈번한 context 전환으로 인한 불필요한 오버헤드 발생
* 디스크 > 커널 버퍼로의 데이터 저장은 CPU 가 아닌, DMA 컨트롤러가 수행하여 오버헤드가 거의 없는 반면에, 커널 버퍼 > JVM 프로세스 버퍼로의 데이터 복사는 CPU 가 수행하므로 큰 부하 발생
* 또한, 사용이 끝난 JVM 내부 버퍼 메모리 영역은 GC 대상이 되어 오버헤드 추가

#### 블로킹 I/O
* I/O 작업 과정에서 커널버퍼 > JVM 프로세스 버퍼로의 데이터 복사가 호출 스레드를 blocking 한채로 수행되기때문에 성능 저하 발생
* 특히나, 네트워크 I/O 인경우, 데이터 I/O 뿐만 아니라 socket establish 과정에서도 스레드가 block 되기떄문에 성능 저하는 더 심화됨
```java
Socket clientSocket = new ServerSocket(8080).accept();   //소켓 통신이 established 될 때까지 block
BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

String request = in.read();                         //IO 로 인한 blocking
out.println(processRequest(request);                //IO 로 인한 blocking
```

<br>

# NIO
* EventLoop 를 통해 여러 클라이언트 요청을 단일 스레드로 처리하는 방식

<img src="https://user-images.githubusercontent.com/48702893/131709571-af197f5c-b8b5-405c-bf75-7d4f78d63ab6.png" width="400" height="400">

* IO 작업을 Non blocking 으로 처리하여 리소스를 효율적으로 사용할 수 있기에 가능
* 적은 수의 스레드로 더 많은 연결을 처리할 수 있어 메모리 관리와 컨텍스트 스위치에 대한 오버헤드 적음
* 1개의 EventLoop 스레드와 다수의 Worker 스레드로 동작

### NIO 동작 방식
<img src="https://user-images.githubusercontent.com/48702893/131711099-4701c5ac-fe60-47b5-a206-d12231e8231d.png" width="500" height="200">

<img src="https://user-images.githubusercontent.com/48702893/129528788-c0374bff-8735-4d9e-8c69-021a62b0d642.gif" width="200" height="200" align="center">

* 메인 스레드는 IO 요청 전송 후 다른 작업 수행
* IO 요청에 대한 이벤트 발생(응답 수신, 에러 발생등) 시 이벤트 큐에 저장
* 이벤트 루프 스레드가 지속적으로 이벤트 큐를 확인하여 이벤트가 적재되어있을시, call stack 에 옮겨, 스레드 할당하여 처리

### NIO 장점
#### channel 기반 I/O
* buffer 기반으로 동작하는 channel 을 통해 I/O를 처리하므로 stream 기반으로 동작하는 OIO 에 비해 I/O 성능이 좋음
* 단뱡향인 stream 과는 달리 channle 은 양방향이어서 I/O 구분없이 하나로 사용 가능 

#### Direct Buffer Handling [[ref]](http://eincs.com/2009/08/java-nio-bytebuffer-channel/)
* 커널 버퍼에 직접 접근하여 read/write 할 수 있는 클래스(ByteBuffer)를 제공
  * 내부적으로 포인터 버퍼 형태로 커널버퍼 직접 참조
```java
// use
ByteBuffer directBuf = ByteBuffer.allocateDirect(10);

//ByteBuffer.class
public static ByteBuffer allocateDirect(int capacity) {
        return new DirectByteBuffer(capacity);
}
        
//DirectByteBuffer.class
DirectByteBuffer(int cap) {                   // package-private
        ...
        long base = 0;
        try {
            base = UNSAFE.allocateMemory(size);
        } catch (OutOfMemoryError x) {
            Bits.unreserveMemory(size, cap);
            throw x;
        }
        UNSAFE.setMemory(base, size, (byte) 0);
        ...
}


//Unsafe.class
/**
 * Provides the caller with the capability of performing unsafe
 * operations.
 *
 * <p>The returned {@code Unsafe} object should be carefully guarded
 * by the caller, since it can be used to read and write data at arbitrary
 * memory addresses.  It must never be passed to untrusted code.
 *
 * <p>Most methods in this class are very low-level, and correspond to a
 * small number of hardware instructions (on typical machines).  Compilers
 * are encouraged to optimize these methods accordingly.
 *
 * <p>Here is a suggested idiom for using unsafe operations:
 *
 * <pre> {@code
 * class MyTrustedClass {
 *   private static final Unsafe unsafe = Unsafe.getUnsafe();
 *   ...
 *   private long myCountAddress = ...;
 *   public int getCount() { return unsafe.getByte(myCountAddress); }
 * }}</pre>
 *
 * (It may assist compilers to make the local variable {@code final}.)
 */
public static Unsafe getUnsafe() {
    return theUnsafe;
}

/**
 * Allocates a new block of native memory, of the given size in bytes.  The
 * contents of the memory are uninitialized; they will generally be
 * garbage.  The resulting native pointer will never be zero, and will be
 * aligned for all value types.  Dispose of this memory by calling {@link
 * #freeMemory}, or resize it with {@link #reallocateMemory}.
 *
 * <em>Note:</em> It is the responsibility of the caller to make
 * sure arguments are checked before the methods are called. While
 * some rudimentary checks are performed on the input, the checks
 * are best effort and when performance is an overriding priority,
 * as when methods of this class are optimized by the runtime
 * compiler, some or all checks (if any) may be elided. Hence, the
 * caller must not rely on the checks and corresponding
 * exceptions!
 *
 * @throws RuntimeException if the size is negative or too large
 *         for the native size_t type
 *
 * @throws OutOfMemoryError if the allocation is refused by the system
 *
 * @see #getByte(long)
 * @see #putByte(long, byte)
 */
public long allocateMemory(long bytes) {...}
```

* 이를통해 JVM 프로세스 버퍼로의 복사로 인해 발생하던 여러가지 부하 해소
  * 중복 복사로 인한 부하
  * 복사 작업시 사용하던 CPU 리소스
  * 복사 작업으로 인한 호출 스레드 blcking



NIO는 블로킹과 넌블로킹 특징을 모두 가지고 있다.
IO 블로킹과의 차이점은 NIO 블로킹은 스레드를 인터럽트 함으로써 빠져나올 수가 있다는 것이다.
NIO의 넌블로킹은 입출력 작업 준비가 완료된 채널만 선택해서 작업 스레드가 처리하기 때문에 작업 스레드가 블로킹 되지 않는다.

NIO는 불특정 다수의 클라이언트 연결 or 멀티 파일들을 넌블로킹이나 비동기 처리할 수 있다.
=> 과도한 스레드 생성을 피하고 스래드를 효과적으로 재사용한다는 장점이 있다.
운영체제의 버퍼를 이용한 입출력이 가능하므로 입출력 성능 향상
NIO는 연결 클라이언트 수가 많고 하나의 입출력 처리 작업이 오래걸리지 않는 경우에 사용하는 것이 좋음,


Channel은 Non-blocking 방식도 가능하다. 다시 말하지만, Channel을 사용하는 I/O는 언제나 Non-blocking 방식으로 동작하는 것이 아니라, Non-blocking 방식도 가능하다는 것이다.

파일을 읽는데 사용되는 Files.newBufferedReader(), Files.newInputStream() 등은 모두 blocking이다.

파일을 쓰는데 사용되는 Files.newBufferedWriter(), Files.newOutputStream() 등도 모두 blocking이다.

진짜?

위 메서드들은 결국 Files.newByteChannel()을 통해 생성되는 SeekableByteChannel을 바탕으로 데이터를 읽거나 쓰게 되는데, 이 SeekableByteChannel은 ReadableByteChannel과 WritableByteChannel을 구현하고 있다.

그런데 ReadableByteChannel과 WritableByteChannel은 모두 blocking 모드로만 동작하는 Channel이다.

근거는? Java API에 다음과 같이 적혀 있다.

https://docs.oracle.com/javase/8/docs/api/java/nio/channels/ReadableByteChannel.html

Interface ReadableByteChannel

Only one read operation upon a readable channel may be in progress at any given time. If one thread initiates a read operation upon a channel then any other thread that attempts to initiate another read operation will block until the first operation is complete. Whether or not other kinds of I/O operations may proceed concurrently with a read operation depends upon the type of the channel.

결국 NIO 중에서 File I/O에 관련된 것들은 아쉽지만 모두 blocking인 것이다.

Files.new~~() 외에 Files.lines(), Files.readAllLines(), Files.readAllBytes(), Files.write()도 위에 설명한 것과 마찬가지 이유로 모두 blocking이다.

File I/O에 사용되는 Channel이 blocking 모드로 동작하기는 하지만, 데이터를 Buffer를 통해 이동시키므로써 기존의 Stream I/O에서 병목을 유발하는 몇가지 레이어를 건너뛸 수 있다고 한다.


### NIO 단점
* 순수 자바 NIO 라이브러리를 직접 사용해 네트워크 통신을 구축하는것은 매우 어려움
* 따라서, NIO를 이용한 네트워크 통신을 대신 해주는 Netty 같은 프레임워크를 사용하여 개발자는 비즈니스 로직 개발에만 집중

NIO는 다수의 연결이나 파일들을 넌블로킹이나 비동기 처리할 수 있어서 많은 스레드 생성을 피하고 스레드를 효과적으로 재사용한다는 장점이 있다.
그래서 NIO는 연결 수가 많고 하나의 입출력 처리 작업이 오래 걸리지 않는 경우에 사용하는 것이 좋을 것이다.
스레드에서 입출력 처리가 오래 걸린다면 대기하는 작업의 수가 늘어나게 되므로 장점이 사라진다. 많은 데이터 처리의 경우 IO가 좋을 수 있다.
NIO는 버퍼 할당 크기가 문제가 되고, 모든 입출력 작업에 버퍼를 무조건 사용해야 하므로 즉시 처리 하는 IO보다 성능 저하가 있을 수 있다.
연결 클라이언트 수가 적고 전송되는 데이터가 대용량이면서 순차적으로 처리될 필요성이 있는 경우 IO로 구현하는 것이 좋은 선택일 수 있다.
NIO는 기본적으로 Buffer을 사용하기에 Stream으로 데이터를 주고 받는 IO에 비해 사전작업(버퍼할당, 처리)에 소요되는 비용이 크다

스레드에서 입출력 처리가 오래 걸린다면 대기하는 작업의 수가 늘어나므로 제한된 스레드로 처리하는 것이 불편할 수 있음.
대용량의 데이터 처리의 경우 IO가 좋다.
NIO는 버퍼 할당 크기가 문제가 되고, 모든 입출력 작업에 버퍼를 무조건 사용해야 하므로 즉시 처리하는 IO보다 조금 더 복잡.
연결 클라이언트 수가 적고 전송되는 데이터가 대용량이면서 순차적으로 처리될 필요성이 있는 경우 IO로 서버를 구현하는 것이 좋음

# NIO 성능 개선 포인트 [[ref]](https://jeong-pro.tistory.com/145)
NIO의 키워드 3가지는 버퍼, 채널, 셀렉터다. (각각을 잘 이해하면 성능이 좋은 NIO를 이용할 수 있다.)

### 2. Scatter / Gather
자바안에서 여러 개의 버퍼를 만들어 사용하는데 만약 동시에 각각 버퍼에 데이터를 쓰거나 읽는다고 한다면 시스템 콜을 여러번 불러서 읽거나 쓰게 될 것이다.

시스템 콜을 호출하는 것은 가벼운 작업이 아니므로 이렇게하는 것은 비효율적이다.

Scatter와 Gather는 프로세스에서 사용하는 버퍼 목록을 한 번에 넘겨줌으로서, 운영체제에서는 최적화된 로직에 따라 버퍼들로부터 순차적으로 데이터를 읽고 쓸 수 있게 되는 기능이다.
Scatter/Gather :하나의 데이터 스트림에서 여러개의 버퍼로 한번의 시스템 콜로 읽거나 쓰는 방법

### 3. 가상메모리
가상 메모리는 프로그램이 사용할 수 있는 주소 공간을 늘리기 위해 운영체제에서 지원하는 기술이다.
여러 개의 가상 주소가 하나의 물리적 메모리를 참조함으로써 메모리를 효율적으로 사용할 수 있게 해준다는 점이다.
즉, 유저 영역의 버퍼(가상 주소)와 커널 영역의 버퍼(가상 주소)가 같은 물리 메모리를 참조하게 매핑시키면 커널 영역에서 유저 영역으로 데이터를 복사하지 않아도 되게 된다.

![image](https://user-images.githubusercontent.com/48702893/159925940-6dac67be-a57b-4d4b-b296-5e610aea526b.png)

### 4. 메모리 맵 파일
운영체제가 지원하는 Memory-mapped IO는 파일시스템의 페이지들과 유저 영역의 버퍼를 가상 메모리로 매핑시키는 방법이다.
즉, 유저 가상 메모리와 커널 가상메모리가 보는 물리메모리에 디스크(정확히는 디스크 블록)의 내용까지 일치시켜버리는 것이다.
그러면 별도의 입출력 과정을 거치지 않고 자동으로 디스크에 반영되게 해버리는 것이다.
여기서 매우 큰 장점은 큰 파일을 복사하기 위해 많은 양의 메모리를 소비하지 않아도 된다는 점이다.
파일 시스템의 페이지(단위)들을 메모리로서 바라보기 때문에 그때그때 필요한 부분만을 실제 메모리에 올려놓고 사용하면 되기 때문이다.

### 5. 파일 락(File Lock) --> Server2Server IO 엔 해당사항 없을듯
파일락은 쓰레드에서 공부한 동기화와 비슷한 개념으로 어떤 프로세스가 어떤 파일에 락(lock)을 획득했을 때 다른 프로세스가 그 파일을 동시에 접근하지 못하게 하거나 접근할 수 있게하는 제한을 두는 것이다.
이 때 파일 전체 혹은 일부분을 잠궈서 사용하는데 바이트 단위로 계산해서 파일의 잠금 부분을 계산한다.
이렇게 파일 일부분만 잠궈서 사용함으로써 락이 설정되지 않은 파일의 다른 위치에서 여러 프로세스들이 동시에 다른 작업을 할 수 있게 하는 것이다.

### 6. Non Blocking
NIO의 넌블로킹은 입출력 작업 준비가 완료된 채널만 선택해서 작업 스레드가 처리하기 때문에 작업 스레드가 블로킹되지 않는다.
NIO의 넌블로킹의 핵심 객체는 멀티플렉서(multiplexor)인 셀렉터(Selector)이다.  셀렉터는 복수 개의 채널 중에서 준비완료된 채널을 선택하는 방법을 제공해준다.


# NIO 구성요소
### Buffer
* 자바의 포인터 버퍼 (NIO에서 제공하는 Buffer클래스)
* 커널에 의해 관리되는 시스템 메모리를 직접 사용할 수 있는 Buffer 클래스

### Channel
* 읽기, 쓰기 하나씩 쓸 수 있는 스트림은 단방향식, 채널은 읽기 쓰기 둘다 가능한 양방향식 입출력 클래스
* 네이티브 IO , Scatter/Gather 구현으로 효율적인 IO처리 (시스템 콜 수 줄이기, 모아서 처리하기)

읽기, 쓰기 하나씩 쓸 수 있는 스트림은 단방향식, 채널은 읽기 쓰기 둘 다 가능한 양방향식 입출력 클래스이며 네이티브 IO , Scatter/Gather 구현으로 효율적인 IO 처리 (시스템 콜 수 줄이기, 모아서 처리하기


### Selector
NIO 넌블로킹의 핵심 객체는 멀티플렉서인 Selector이다.
셀렉터는 복수 개의 채널 중에서 이벤트가 준비 완료된 채널을 선택하는 방법을 제공해준다.

하나의 스레드로 여러 채널을 관리하고 처리할 수 있는 클래스 (Reactor 패턴 활용)
* Reactor 패턴 :이벤트에 반응하는 객체를 만들고 이벤트가 발생하면 해당 객체가 반응하여 해당 이벤트에 맞는 핸들러와 매핑시켜서 처리하는 구조
  리눅스환경에선 2.6 이상 부터는 SelectorProvider를 통해 epoll을 지원한다. 이전은 poll

* Channel에서 발생하는 이벤트를 EventLoop인 Selector 가 스레드에 위임하여 처리하는 구조로 동작
* Selector가 지속적으로 select 를 호출하여 등록된 Channel 에서 발생하는 이벤트 모니터링

* Non-blocking + Synchronous 로 동작, NIO2 부터 완전히 Non-blocking + Asynchronous 로 동작 (AIO)

* 네트워크 프로그래밍의 효율을 높이기 위한 것
* 클라이언트 하나당 쓰레드 하나를 생성해서 처리하기 때문에 쓰레드가 많이 생성될 수록 급격한 성능 저하를 가졌던 단점을 개선하는 Reactor패턴의 구현체

![image](https://user-images.githubusercontent.com/48702893/159921061-96cbd352-7a3c-4d80-9ac4-c52d67c91988.png)

![image](https://user-images.githubusercontent.com/48702893/159921076-b0723956-95e8-4d6a-90a2-f3fd000c1c0e.png)

<img src="https://user-images.githubusercontent.com/48702893/131709571-af197f5c-b8b5-405c-bf75-7d4f78d63ab6.png" width="400" height="400" align="center">

***
> Reference <br>
> https://jess-m.tistory.com/23 <br>
> https://coding-start.tistory.com/318 <br>
> https://brunch.co.kr/@myner/50