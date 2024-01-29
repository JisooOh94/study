# RabbitMq
* AMQP 프로토콜을 구현한 메세지 브로커 시스템
* Producer - Consumer간의 Point-to-Point 메세지 전달 보장에 초점
	* 그를 위해 신뢰있는 메시지 전달을 위한 다양한 장치 마련 (exchange type, 라우팅 기능 등)
	* 이로인해 프로듀서 - 브로커 - 컨슈머간 강결합을 유발하여 확장성이 떨어짐
* Kafka 보다 먼저 나와 레퍼런스 자료가 많고 성숙도가 높음
* 데이터 처리보단 관리도구나 다양한 기능 구현을 위한 서비스, 혹은 메시지별 라우팅이 필요한 경우에 주로 사용

### RabbitMq 라우팅 옵션
* Direct : unicast 방식처럼 라우팅 키가 정확히 일치하는 큐에 메시지 라우팅 
* Fanout : Broadcast 방식처럼 라우팅 키와 상관없이 모든 큐에 메시지 라우팅 
* Topic : 라우팅 키의 패턴(Regex)에 부합하는 큐에 메시지 라우팅 
* Headers : x-match 속성인 [any, all]을 통해서 메시지 헤더의 키와 값의 일치 정도에 따라 라우팅

### AMQP(Advanced Message Queuing Protocol)
* 클라이언트와 브로커간의 메시지를 주고받기 위한 프로토콜
* 3가지 방식의 메시지 전달 보장
	* At-Most-Once: 각 메시지는 한번만 전달되거나 전달되지 않음
	* At-Least-Once: 각 메시지는 최소 한번 이상 전달됨을 보장
	* Exactly-Once: 각 메시지는 딱 한번만 전달됨

### RabbitMq 구조

![image](https://user-images.githubusercontent.com/48702893/154788920-4904e138-dd7a-45fc-b3fe-a61775341131.png)

* Publisher : 메세지 생산자
* Exchange : 생산한 메세지를 어떤 Queue에 전달할지 판단 및 전송
* Queue : 메세지 저장 큐
* Consumer : 메세지 소비자

<br>

# Kafka vs RabbitMq 특징 비교
### 기능
* Kafka
	* 대용량의 실시간 메시지 트래픽 처리에 집중
* RabbitMq
	* 높은 처리량보다는 메시지의 신뢰성있는 전달에 집중
	* 신뢰성있는 메시지 전달으 위해 exchange type 이나 라우팅등의 기능 지원

### 메시지 리플레이
* Kafka 
	* 메시지를 메모리에 저장하는 기존 메시징 시스템과는 달리 파일(디스크)에 저장
		> 일반적으로 디스크보다 메모리의 IO 성능이 뛰어나나, 디스크의 순차스캔 성능은 메모리 스캔과 비슷하여, 카프카가 메시지를 디스크에 저장하여도 성능 저하 적음
	* 이를 통해 카프카를 재시작해도 메시지 유실되지 않고 영속성 보장
* RabbitMq
	* 메시지를 메모리에 저장
	* 컨슈머가 메시지를 컨슘해가거나, 장애등으로 메시지 큐 사망시 메시지 삭제됨

### 메시지 컨슈밍
* Kafka
	* 백프레셔 방식(컨슈머가 브로커로부터 메시지를 직접 가져가는 PULL 방식
	* 백프레셔의 장점을 통해 컨슈머가 최적의 성능을 낼 수 있어 메시지 대용량 처리 가능
* RabbitMq
	* 옵저버 패턴 방식(브로커가 컨슈머에게 메시지를 push해 주는 방식)

### 프로토콜
* Kafka
	* TCP / IP 위의 custom 프로토콜 사용
	* 표준화된 프로토콜을 사용하지 않기에 쉽게 대체하기 힘듦
* RabbitMq
	* AMQP, STOMP, MQTT 등과 같은 여러 표준 프로토콜 지원
	* 지원하는 표준 프로토콜이라면 대체 가능

### 라우팅
* Kafka
	* 라우팅 기능 지원하지 않음
	* 라우팅 기능이 필요할시, Kafka Streams를 활용하여 직접 구현 가능 
* RabbitMq
	* Direct, Fanout, Topic, Headers의 여러 라우팅 기능 제공하여 유연하게 선택 가능

### 메시지 우선 순위
* RabbitMq
	* priority queue를 지원하여 x-max-priority argument 값을 통해 메시지 처리 순서 설정 가능
* Kafka
	* 메시지가 프로듀스된 시간(동일 파티션 내에서만)에 따라서만 처리되고 그 외 처리 순서 설정 불가능 

### 응답
* RabbitMq
	* ack 응답을 통해 브로커에 메시지가 안전하게 프로듀스 되었는지 확인 가능
	* 마찬가지로 ack 응답을 통해 메시지가 안전하게 컨슈머에게 전달되었는지 확인 가능
	* 컨슈머에서 직접 ack 응답 전송 시점을 관리하여 메시지에 대한 완전한 처리도 보장 가능
* Kafka
	* RabbitMq와 마찬가지로 ack 응답을 통한 안전한 프로듀싱, 컨슈밍 보장
	* 컨슈머에서 offset을 통해 메시지의 처리 추적 가능 및 수동 제어 가능

### 스케일링
* RabbitMq
	* Smart Pipes, Dumb endpoints 모델로 Points-to-Points 방식으로 메시지를 교환하므로 프로듀서 - 브로커 - 컨슈머가 강결합되어 확장이 어려움
* Kafka
	* Smart endpoints, Dumb Pipes 모델로, endpoints 인 프로듀서, 컨슈머와 dumb pipes 인 브로커가 느슨하게 결합되어 확장이 쉬움
	* 브로커, 컨슈머를 모두 클러스터링하고, 이를 zookeeper 를 통해 관리하여, 확장에 필요한 작업을 모두 자동으로 수행하므로(리밸런싱, 메시지 복제등) 확장에 용이
	
<br>

# use-case
### Kafka
* 리얼타임 스트리밍에 적합
* 여러 프로듀서가 매우 큰 규모의 메시지를 생산하고 다양한 컨슈머에게 전송해야 되는 시스템에 적합
* MSA 환경에서, 유연한 확장성이 중요한 아키텍쳐에 적합

### RabbitMq
* 메시지의 수신 또는 메시지의 처리 순서가 보장되어야 하는 경우
* 끊임없이 발생되는 스트리밍 이벤트 처리보다는 두 Endpoints 간의 아토믹한 메시지 교환에 적합
* 작은 규모의 메시지 처리에 적합
	
***

> Reference
> * https://ellune.tistory.com/29
> * https://armful-log.tistory.com/61
> * https://yearnlune.github.io/general/RabbitMq-vs-kafka/#
> * https://gwonbookcase.tistory.com/49
> * https://yearnlune.github.io/general/RabbitMq-vs-kafka/#%EB%A9%94%EC%8B%9C%EC%A7%80-%EC%9A%B0%EC%84%A0-%EC%88%9C%EC%9C%84
> * https://blog.naver.com/PostView.nhn?isHttpsRedirect=true&blogId=kbh3983&logNo=221077410881&categoryNo=110&parentCategoryNo=0&viewDate=&currentPage=1&postListTopCurrentPage=1&from=postView