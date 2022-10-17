# Netty based framework comparison
* Netty 기반 Non-blocking framework 비교
* 가장 최근까지 유지보수 되고있는 framework 들만 선정
    * [Vert.x](https://github.com/vert-x3)
    * [Akka](https://github.com/akka)
    * [Ratpack](https://github.com/ratpack)

# [Vert.x](https://vertx.io/)
* Verticle 로 구성되는 비동기 웹애플리케이션 개발 프레임워크

### Verticle
* 독립적으로 실행가능한 하나의 작은 서비스
  * Java로 생각하면 독립적으로 실행 가능한 Class 또는 .jar 파일 (main 메서드가 있는 클래스), Servlet
* 애플리케이션은 하나의 Verticle로 이루어질 수도 있고, 여러 개의 Verticle로 이루어질 수도 있다 
* 애플리케이션 실행시 하나의 vertx 인스턴스가 만들어지고, 각각의 Verticle 들이 인스턴스 안에서 개별적인 스레드로 생성된다.
  * 일반적인 스레드와 다르게, 각각의 Verticle 들은 고유의 ClassLoader 를 가져 공유 데이터 영역이 없다. Event Bus 를 통해 상호간에 통신한다
      * 그러나 Verticle 간 공유되어야 더 효율적인 데이터들(e.g. 캐시)도 존재한다. 이를 위해 별도의 굥유 데이터를 위한 영역인 Shared Map 을 지원하며 immutable 데이터만 shared map 에 저장할 수 있다
  * 또한 여러호스트에서 구동중인 Vertx 인스턴스들을 Event Bus 를 통해 간편하게 클러스터링 할 수 있다.
  
  ![image](https://user-images.githubusercontent.com/48702893/194755863-9632477a-46bd-4e58-84e9-126ae690b88b.png)

### Event Bus
* Verticle 간, (클러스터링 되어있는) vertx 인스턴스간 데이터 통신 통로
* 내부적으로 Hazelcast 데이터 그리드 솔루션 사용하여 신뢰성 및 안정성 보장
* Point to Point나 Pub/Sub 같은 MQ 기능 사용 가능

### 장점
1. Event Bus 를 이용한 간편한 Vert.x 인스턴스 Clustering 지원

2. Polyglot
   * Java나 kotlin, groovy 처럼 jdk 기반 언어 뿐만아니라 Ruby나 Python, JS 등 다양한 언어에 vert.x integration 가능
   * Verticle 간에는 event bus 를 통해 json 포맷 메시지 기반으로 통신하므로, 하나의 vert.x 인스턴스 내에서 여러 언어로 개발된 verticle 들이 함께 동작 가능

3. Simple Concurrency model
  * 서로 데이터 교환 없이 경량 프로세스처럼 동작하는 Verticle 덕분에, blocking 로직 작성하듯((synchronized나 volatile 같은 동기화를 위한 locking 처리 불필요) non-blocking 애플리케이션 개발 가능

4. 높은 동시성
* Vertx 의 Verticle 들은 서로 공유 데이터 영역 없이 개별적인 경량 프로세스처럼 동작한다. 
* 이덕분에 하나의 vertx 인스턴스에서 여러개의(보통 core 수 만큼) ELP 스레드를 띄워 동시에 더 많은 요청을 처리할 수 있다. 
  * Node.js와 같은 기존의 비동기 프레임워크의 경우 Single Thread기반의 ELP를 하나만 띄울 수 있기 때문에, Core수가 많아도, 이를 사용할 Thread가 없어 대량의 멀티코어 CPU 에서 성능이 크게 늘어나지 않음
  * 물론 Node.js도 여러개의 Node.js Process를 동시에 띄워서 여러개의 Core를 동시에 사용할 수 는 있지만 Process의 Context Switching 비용이 Thread의 Context Switching 비용보다 크기 때문에, 여러개의 Thread 기반으로 동작하는 Vert.x가 성능면에서 더 유리

![image](https://user-images.githubusercontent.com/48702893/194757382-916c2de6-96b5-4fe8-99ae-75db62b3b329.png)

![image](https://user-images.githubusercontent.com/48702893/194757388-92b3f92c-e687-499f-96c3-1fe76d610da1.png)

5. Embedded Vertx
* Vertx는 자체가 서버로써 독립적으로 동작할 수 있을 뿐만 아니라 라이브러리 형태로도 사용이 가능
* 그렇기때문에 Tomcat 같은 WAS와 함께 기동이 될 수도 있고, 하나의 JVM에서 Tomcat 서비스와 Vert.x 서비스를 같이 수행하는 형태도 가능
  > e.g. 일반적인 HTTP Request는 Tomcat으로 처리하고, SocketIO나 WebSocket과 같은 Concurrent connection이 많이 필요한 Request는 Vert.x 모듈을 이용해서 처리하게 구성 가능

6. Vert.x is a full solution, TCP, HTTP server, routing, even WebSocket

> Ref
> * https://d2.naver.com/helloworld/163784
> * https://bcho.tistory.com/860
> * https://www.baeldung.com/vertx
> * https://invisible-blog.tistory.com/42
> * https://www.baeldung.com/spring-vertx

<br>

# [Akka](https://akka.io/)
* Actor 를 통한 비동기 어플리케이션, Akka cluster 을 통한 분산 환경 어플리케이션 개발을 단순화 해주는 오픈소스 툴킷

### Actor
* 비즈니스 로직을 수행하는 일종의 경량 프로세스(기존의 클래스, 객체와 동일한 역할 수행)
* 비동기
  * 액터에 메세지 전송하여 비즈니스 로직 수행 요청후, 완료를 기다리지 않고 바로 다른작업 수행하는 비동기적으로 동작
  * 각 액터는 전달받은 메시지를 큐에 보관하며, 메시지를 순차적으로 처리
* 강력한 격리 원칙
  * 일반 클래스 객체와 달리, actor 에는 상태를 조회할 수 있는 어떠한 public api 가 없어 actor 간 상태 공유가 불가능하다.
  * 상태 공유 없이, 메시지를 통해 상호작용하므로 서로 완벽하게 독립적이며, 코드의 응집성(coherenece), 느슨한 결합(loosely coupled), 캡슐화(encapsulation) 보장
  * 위와 같은 특징은 데드락이나 락에 대한 고민을 없애주기 때문에 병렬 처리 코드를 보다 쉽게 구현할 수 있도록 도와준다.
* 경량
  * 각 엑터 인스턴스는 수백 바이트만 소비하므로 단일 응용 프로그램에 수백개의 액터를 동시 생성 가능
  
#### Actor architecture
![image](https://user-images.githubusercontent.com/48702893/193443881-87256067-efe4-429c-bae4-6724d7f97964.png)
* Actor Ref
  * actor 의 주소를 담고 있는 컨테이너
  * 다른 actor 와 메시지를 송수신 하기 위해 사용됨
  > ActorRef 를 통해 해당 actor 인스턴스로 직접 접근하여 public method를 쓰는 구조가 아니라 해당 actor의 주소로 서로의 메세지를 던져서 함수를 호출하는 방식
* Dispatcher 
  * 들어오는 Message를 받아 Actor에 전달, 스케줄링 역할 및 Actor의 처리량 조율(Actor의 상태 확인하여 메시지 처리 여부를 판단)
* Mail Box
  * FIFO 순서를 따라 메시지를 선택하고 처리
* State
  * 액터 객체의 상태를 나타내는 변수
  * counter, setoflisteners, pendingrequests
* Behavior
  * 메시지 처리에 따른 액터의 상태 변화 제어

### Cluster
* Akka 는 분산환경에 적용하기 용이하도록 설계되었다. 대표적으로 Actor model 또한 메세지를 통해 상호작용하게 함으로서 분산환경에 적용하기 용이하다.
* 또한 Akka.cluster package 도 제공하여, 다양한 분산환경 어플리케이션 개발을 위한 기능들을 상요할 수 있다.
  * 어떠한 설정 작업 없이, 자동으로 hc 수행하여 cluster 내 node 등록, 삭제 수행
  * 사용자 정의 클래스에서 cluster 의 가용 node 변화를 subscribe 할 수 있는 기능 제공
  * Akka 에서 제공하는 기본 router 를 확장하여 사용자 정의 router 를 개발할 수 있도록 제공
  * Introduces the concept of "roles" to distinguish different Akka.NET applications within a cluster
* 이와같은 Cluster 를 통해 여러가지 이점을 얻을 수 있다
* Fault tolerant
  * 클러스터는 에러나 실패(특히, 네트워크 파티션쪽) 로부터 우아하게 스스로 복구된다.
* Elastic
  * 클러스터는 본질적으로 유연하며, 필요에 따라 간편하게 scale up/down 할 수 있다.
* Decentralized
  * 클러스터내에서 주어진 마이크로 서비스 또는 애플리케이션 상태의 동일한 복제본을 여러 개 동시에 실행할 수 있어 하나의 노드에 어플리케이션이 의존성이 생기는걸 방지한다. 
* Peer-to-peer
  * 새로운 노드 추가시, 어떠한 설정작업 없이 노드는 자동으로 cluster의 peer들에 연결되며, peer 들 또한 자동으로 새로운 node가 추가되었음을 공유받아 새로운 노드가 클러스터 네트워크에 통합된다.
* No single point of failure/bottleneck
  * 어플리케이션 기능을 여러개의 노드가 수행할 수 있으므로, fault tolerance가 좋아지고 throughput이 높아진다.
* 낮은 진입장벽
  * 액터 모델을 통해서 리모트 노드에 존재하는 액터를 마치 로컬에 존재하는 액터처럼 사용할 수 있도록 지원해주기 때문에 개발자는 통신 프로토콜에 대해 고민할 필요 없이 리모트 액터를 사용할 수 있어 분산 처리 코드를 손쉽게 작성할 수 있다.

### 장점
* 직관성
  * Actor model이라는 개념 자체가 꽤 직관적인 부분이 있어 내가 원하는 Actor를 정의해서 Actor 간의 통신을 구성하는 것이 상대적으로 쉽다.
* 확장성
  * Akka framework는 클러스터 형식으로 운영할 수 있기 때문에 물리적인 확장성에서도 대응이 된다.
* 격리성
  * Actor에 직접적인 제어권을 갖거나 접근하기 위한 API가 존재하는 게 아니라 모든 것이 메시지로 인해서 비동기적으로 동작하기 때문에 직접적으로 다른 Actor에 영향을 줄 가능성이 거의 없다.
* 위와같은 특징들 덕분에 highly performant, highly scalable, highly maintainable, and highly available 어플리케이션 개발에 용이
* vert.x, ratpack 에 비해 상대적으로 사용자 수나 레퍼런스가 더 많다.

> Ref
> * https://javacan.tistory.com/entry/akka-1-start
> * https://medium.com/spoontech/akka-framework-%EC%9E%85%EB%AC%B8%EA%B8%B0-8fdc3d7c878d
> * https://changun516.tistory.com/196
> * https://azderica.github.io/00-akka-starter/
> * https://getakka.net/articles/clustering/cluster-overview.html
> * https://wiki.webnori.com/display/AKKA/Spring+Boot+With+AKKA
> * https://brunch.co.kr/@springboot/172
> * https://www.linkedin.com/pulse/spring-boot-akka-part-1-aliaksandr-liakh/
> * https://www.baeldung.com/akka-with-spring

<br>

# Comparison
### [Benchmark](https://www.techempower.com/benchmarks/#section=data-r21&f=zijunz-zik0zj-zik0zj-v2qiv3-zik0zj-zijbpb-zik0zj-zik0zj-hra0hr-zik0zj-zik0zj-zik073-zik0zj-35r)
<img width="1190" alt="image" src="https://user-images.githubusercontent.com/48702893/188261517-b1122170-33ad-4ffc-b331-2c93339d94b6.png">

### [Popularity](https://stackshare.io/stackups/ratpack-vs-vert-x-vs-akka)
<img width="858" alt="image" src="https://user-images.githubusercontent.com/48702893/188261415-948989c4-de13-4748-86d6-de92a48724c1.png">

### [Trends](https://trends.google.com/trends/explore?q=akka,vertx)
<img width="1172" alt="image" src="https://user-images.githubusercontent.com/48702893/196204495-acbd5059-88db-4947-9c82-48f41a927460.png">


### Spring Webflux vs Vert.x(Akka)
#### Microservices
* MSA 환경에서, vert.x 는 verticle 및 vert.x 인스턴스들이 독립적으로 동작하며 event-bus 로만 상호 작용하므로, Component 간 loosely coupled 되어있다 할 수 있다
* 따라서 vert.x 는 event-bus 를 사용하는 어떠한 JVM 기반 언어의 컴포넌트와도 함께 동작 할 수 있다.
* 반면에, Spring webflux 는 컴포넌트간 강결합 되기 떄문에, Java 나 Kotlin 으로 개발된 컴포넌트와만 함께 동작 할 수 있다. 

#### Other Protocols
* Webflux 와 Vert.x 는 모두 Netty 기반으로 동작하나 지원하는 애플리케이션 개발 범위가 다르다.
* Webflux는 HTTP 프로토콜 기반으로 동작하는 웹어플리케이션 개발을 위한 언어이다.
* 반면에 vert.x는 HTTP 웹 애플리케이션 개발 뿐만 아니라, TCP/UDP 프로토콜 기반 어플리케이션, Unix domain socket 어플리케이션 개발도 지원하다.  

#### Blocking and Relational Databases
* Blocking 로직은 비동기 어플리케이션 개발할떄 항상 문제가 되어왔다. 
* 스레드수를 적게 생성하는 비동기 어플리케이션 특성상 blocking 로직이 어플리케이션에 다수 존재하면 throughput 이 급격히 떨어진다.
* 또한 관계형 db 와 통신하는 비동기 어플리케이션도 문제가 많았는데, 현재까진 관계형 db와의 Non-blocking 통신을 지원하는 공식적인 데이터베이스 driver가 존재하지 않기 때문이다.
* 이에따라, Spring 에선 이렇게 블로킹 로직이 많은 애플리케이션이나 관계형 DB 를 사용해야하는 애플리케이션엔 webflux보단 Spring MVC 를 사용하도록 가이드 하고 있다.
<img width="962" alt="image" src="https://user-images.githubusercontent.com/48702893/194759716-0285714b-694c-4a47-9946-88d01801724a.png">

* 하지만 Vert.x 는 위와 동일한 상황에 대해 몇가지 workaround 를 제공한다
  1. The Vert.x JDBC Module, which is an asynchronous API wrapper around JDBC.
  2. The executeBlocking call in Vert.x Core.
  3. Using a completely separate Verticle called a Worker Verticle which runs in a defined set of thread pools.

#### Polyglot
* vert.x 는 polyglot 언어로서, 다양한 프로그래밍 언어와 함께 사용할 수 있을 뿐만 아니라, 라이브러리 형태로도 사용가능해, 어떠한 웹서버와도 함께 사용될 수 있다.
* 그에반해, Spring webflux 는 JVM 기반의 언어(java, kotlin) 에만 사용 가능하고, 구동되는 웹서버에도 제약이 존재한다(Netty, Tomcat, Jetty, Undertow, and Servlet 3.1+ containers) 

#### Performance
* 그리고 무엇보다, benchmark 에서도 알 수 있듯이, Vert.x 가 Webflux 보다 성능이 좋다
* 하지만 Webflux 는 성능은 다소 떨어진다 해도, 거대한 커뮤니티를 형성하고 있고 레퍼런스또한 방대하다. 게다가 지원하는 써드파티모듈 또한 다양하다.
* 따라서, 라이브러리 형태로도 integration 할 수 있는 vert.x 특성을 이용하여 vert.x 와 spring 을 함께 사용하는것도 하나의 솔루션이 될 수 있다.
    > e.g. 인증 처리는 spring 에서 제공하는 OAuth 2.0 을 이용하고, 비즈니스 로직은 verticle 을 통해 수행되도록 구성

> Ref
> * https://blog.rcode3.com/blog/vertx-vs-webflux/
> * https://stackoverflow.com/questions/47711528/spring-webflux-vs-vert-x

### Akka vs Vert.x
* Akka, Vert.x 모두 리액티브를 지향하고 있는 점이나, 이벤트(메시징) 루프식의 스택도 그렇고, verticle 이나 actor와 같이 공유 데이터 없는 독립적인 개체로 동작하는 등 여러 공통점이 많음
* 그럼에도 몇가지 중요한 차이가 존재하며 이들을 스택 선택시 판단기준으로 사용

#### Elastic
* Vert.x 는 위에서도 설명했듯, polyglot language이다. 다양한 언어로 개발될 수 있고, Event bus 를 통해 서로 다른 언어로 개발된 verticle 들을 하나의 애플리케이션처럼 동작할 수도 있다.
* 또한, 라이브러리 형태로 필요한 부분에만 선택적으로 사용할 수도 있기에, 다른 WAS 프레임워크나 비동기 프레임워크, 심지어 Akka 와도 함께 쓰일 수 있다.
* 이와같은 특징들덕에 다순힌 유연해지는것 뿐만 아니라, 서비스를 구성하는 모듈간 의존성을 해소하고 decoupling 시킬 수 있다.
* 반면에, Akka 는 scala나 java object 포맷의 메시지로 actor 간 통신하므로 java 또는 Scala 로만 개발 될 수 있다.

#### Fault tolerant & Delivery Guarantees
* Akka actor 하나의 actor 에서 자식 actor 를 가지며 supervisor 역할을 함으로서 트리형 계층 구조를 형성한다. [[ref]](https://getakka.net/articles/concepts/actor-systems.html#hierarchical-structure)
* 이를통해, actor 계층의 level 별로 actor 가 Handling 할 error를 따로 가져갈 수 있으며, 자식 actor 는 자신이 handling 할 수 없는 error 인경우 parent actor 로 전파하여 처리를 위임한다.
* 이를통해 에러 handling 을 계층별로 구조적이면서도 유연하게 가져갈 수 있다.

![image](https://user-images.githubusercontent.com/48702893/196196243-f82562ae-6782-4a8a-b26c-ff68336018f0.png)

* 또한 Akka 는 [akka persistence](https://doc.akka.io/docs/akka/current/typed/persistence.html#introduction) 를 통해, actor 가 예기치 못한 상황으로 shutdown 되었다가 다시 restart 되었을떄 shutdown 되기 전 actor 의 상태로 자동으로 recover 해준다.
* 이를 통해, 메시지의 At-most-once 전달 뿐만 아니라 At-lease-once 전달 또한 보장한다.
* 반면에 Vert.x 는 verticle 들이 단일 계층만을 형성할 수 있어 하나의 Verticle 에서 발생가능한 모든 예외상황을 handling 해야한다.
* 또한 Vert.x 는 Akka persistence 같은 자동 recover 기능을 지원하지 않아, 메시지의 At-lease-once 전달을 보장하지 못한다.

#### Support
* Akka 는 Lightbend 의 Reactive Platform 으로서 개발 및 유지보수 되고있다. 기업내에서 체계적으로 관리되기때문에 지원도 많고 기술적으로도 더 성숙하다 할 수 있겠다.
* 지원하는 기능도 매우 다양한데 kafka, cassandra, grpc 등의 integration을 손쉽게 도와주는 alpakka 라이브러리를 포함하여 다양한 dbms, mq, 프로토콜등을 지원한다.
* 이렇게 다양한 서드파티 지원을 포함하여 fault-tolerance, resilience, actors 구조, event-sourcing 을 통해 대용량 트래픽을 처리하는 대규모 분산 비동기 시스템에 적용하기에 적합

#### Learning curve
* Akka 는 다양한 서드파티 라이브러리 및 기능을 지원하기때문에 강력하나 러닝커브가 높음
* 그에반해 Vert.x 는 더 가볍고 구조가 심플하며 유연하여 러닝커브가 낮고 다양한 용도로 빠르게 적용 가능해 생산성이 높다.

> Ref
> * https://glqdlt.tistory.com/357
> * https://hamait.tistory.com/218
> * https://qimia.io/en/blog/Akka-Actors-vs-Vert-x-Core
> * https://www.quora.com/Among-Vert-x-and-Akka-which-toolkit-do-you-prefer-in-development-and-why

<br>

# RatPack[[Link]](https://ratpack.io/)
* Java 기반 비동기 웹애플리케이션 개발을 위한 Java 라이브러리

### 장점
* 매우 경량이면서 빠르고 확장성이 높다
* 다른 프레임워크들(e.g. dropwizard)에 비해 리소스(e.g. cpu, memory)를 적게 사용한다
* 다른 Java 라이브러리나 프레임워크와의 연동이 쉽다
* Netty 기반으로 비동기 처리 로직을 개발하여 신뢰성을 보장한다.
* Junit 에 연동되는 자체 테스트 라이브러리를 별도로 제공하여 테스트 작성이 쉽다

### 단점
* Java 8로 개발되어있음
* 사용자가 적고 커뮤니티가 작음

> Ref
> * https://www.moreagile.net/2016/01/ratpack.html
> * https://www.baeldung.com/ratpack