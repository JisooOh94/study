# Netty based framework comparison
* Netty 기반 Non-blocking framework 비교
* 가장 최근까지 유지보수 되고있는 framework 들만 선정
    * [Vert.x](https://github.com/vert-x3)
    * [Akka](https://github.com/akka)
    * [Ratpack](https://github.com/ratpack)

### [Benchmark](https://www.techempower.com/benchmarks/#section=data-r21&f=zijunz-zik0zj-zik0zj-v2qiv3-zik0zj-zijbpb-zik0zj-zik0zj-hra0hr-zik0zj-zik0zj-zik073-zik0zj-35r)
<img width="1190" alt="image" src="https://user-images.githubusercontent.com/48702893/188261517-b1122170-33ad-4ffc-b331-2c93339d94b6.png">

### [Popularity](https://stackshare.io/stackups/ratpack-vs-vert-x-vs-akka)
<img width="858" alt="image" src="https://user-images.githubusercontent.com/48702893/188261415-948989c4-de13-4748-86d6-de92a48724c1.png">

# [Vert.x](https://vertx.io/)
* Verticle 로 구성되는 비동기 웹애플리케이션 개발 프레임워크

### Verticle
* vert.x에서 배치(deploy)의 기본 단위
  * Java로 생각하면 독립적으로 실행 가능한 Class 또는 .jar 파일 (main 메서드가 있는 클래스)
* 애플리케이션은 하나의 Verticle로 이루어질 수도 있고, event bus를 통해 서로 통신하는 여러 개의 Verticle로 이루어질 수도 있음
  * verticle 은 보통 Netty 와 비슷하게 클라이언트의 연결 요청을 수락하는 하나의 Main verticle 과 IO 작업 및 처리를 담당할 n개의 worker verticle 로 구성
  * 멀티코어환경일경우, 동시에 여러개의 ELP 스레드를 구동하며 이와 함께 동작하는 여러개의 Main verticle 및 worker verticle을 함께 구성 가능
  
    ![image](https://user-images.githubusercontent.com/48702893/189471986-8790906a-c54e-4fa6-a419-0f18104195fd.png)

### Event Bus
* Verticle 간 데이터 교환이나, 클러스터링 되어있는 vertx 인스턴스간 통신 통로
* Point to Point나 Pub/Sub 같은 MQ 기능을 사용 가능

### 장점
1. Clustering
* 여러 호스트에 동시에 여러개의 vert.x 인스턴스를 실행하고 이들간의 event bus를 형성해서 클러스터링 가능

2. Polyglot
* vert.x 자체는 Java로 작성되었지만, vert.x를 사용하기 위해 반드시 Java를 사용할 필요는 없음
* Java나 Groovy 같이 JVM 동작을 전제로 한 언어 뿐만 아니라 Ruby나 Python, JS, Kotlin 으로도 vert.x 프레임워크 사용 가능
* Spring framework 에도 Integration 가능

3. Simple Concurrency model
* 싱글 스레드 애플리케이션을 작성하듯 멀티 스레드 애플리케이션 개발 가능 (synchronized나 volatile 같은 동기화를 위한 locking 처리 불필요)

4. 낮은 진입장벽
* EventHandler만 구현하면 되기 때문에 코딩양도 적고, 컴파일 없이(???) 바로 실행해볼 수 있기 때문에 개발 및 테스트가 편함
* Tutorial 도 정리가 잘되어있다. (https://vertx.io/docs/)

5. 높은 격리성
* 각각의 Verticle은 고유의 클래스 로더를 가져 Verticle 간에 스태틱 멤버, 글로벌 변수 등을 통한 직접적인 상호작용을 방지
* 이덕분에, WAS의 Multi threading 모델과는 다르게, 여러개의 Thread를 띄우더라도 각 Thread가 객체나 자원의 공유 없이 마치 프로세스처럼 독립적으로 동작

6. 멀티코어 환경에서의 높은 성능
* 높은 격리성 덕분에 싱글스레드로 동작하는 ELP를 동시에 여러개 띄울수있어, 멀티코어환경에서 더 높은 동시성과 성능 보장
* Node.js와 같은 기존의 비동기 프레임워크의 경우 Single Thread기반의 ELP를 하나만 띄울 수 있기 때문에, Core수가 많아도, 이를 사용할 Thread가 없어 대량의 멀티코어 CPU 에서 성능이 크게 늘어나지 않음
* 물론 Node.js도 여러개의 Process를 동시에 띄워서 여러개의 Core를 동시에 사용할 수 는 있지만 Process의 Context Switching 비용이 Thread의 Context Switching 비용보다 크기 때문에, 여러개의 Thread 기반으로 동작하는 Vert.x가 성능면에서 더 유리

7. Embedded Vertx
* Vertx는 자체가 서버로써 독립적으로 동작할 수 있을 뿐만 아니라 라이브러리 형태로도 사용이 가능
* 그렇기때문에 Tomcat 같은 WAS와 함께 기동이 될 수 있어, 하나의 JVM에서 Tomcat 서비스와 Vert.x 서비스를 같이 수행하는 것이 가능
  > e.g. 일반적인 HTTP Request는 Tomcat으로 처리하고, SocketIO나 WebSocket과 같은 Concurrent connection이 많이 필요한 Request는 Vert.x 모듈을 이용해서 처리하게 구성 가능
   
8. 성능이 이미 입증된 모듈을 도입하여 성능에 대한 신뢰성을 보장
* 보통, 새로나온 프레임워크들은 성능에 대한 의심을 피할 수 없는데, Vert.x는 성능을 이미 입증받은 기존 도구들을 가져와 자신들의 방식에 맞게 녹여내어 안정성 보장
  > Netty를 서버 엔진으로 사용하고 있고, EventBus에 HazelCast 사용

> Ref
> * https://d2.naver.com/helloworld/163784
> * https://bcho.tistory.com/860
> * https://www.baeldung.com/vertx
> * https://invisible-blog.tistory.com/42

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
  * 위와 같은 특징은 데드락이나 락에 대한 고민을 없애주기 때문에 병행 처리 코드를 보다 쉽게 구현할 수 있도록 도와준다.
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
* Java 8로 개발되어있음...
* Netty의 사용성 정도만 개선한걸로 보이며 따로 Ratpack 만의 기술이 있어보이진 않음.
* 그에따라 기본 아키텍쳐도 Netty 나 Node.js 와 거의 동일하며 이들에 비해 성능적으로 뛰어난부분이 안보임

> Ref
> * https://www.moreagile.net/2016/01/ratpack.html
> * https://www.baeldung.com/ratpack