# 구조

![image](https://user-images.githubusercontent.com/48702893/149144910-d9f5199a-575c-4fe6-8187-c3a0cdc3f6f7.png)

* Producer - kafka Cluster(Broker) - Consumer 로 구성
	* Producer 에서 메시지 생성하여 Broker 로 전송
	* Broker 에 메시지가 저장되어있으면, Consumer 가 메시지 읽어 처리
* pub-sub 구조 메시지 큐
	* publisher 가 메시지 발행시, 그를 구독한 subscriber 가 메시지 소비(정해진 subscriber 에게 메시지를 전송 X)
	* publisher 는 subscriber 에 대한 정보 없이 메시지만 발행하고, subscriber 도 publisher에 대한 정보 없이 메시지 소비 가능
	* 발행자와 구독자의 디커플링을 통해 가용성 및 확장성 증대
	
### 1. producer
* 크게 Accumulator, Sender로 구성
* Accumulator
	* 전송 요청되는 메시지들을 모아두는 버퍼, 저장공간
	* buffer.memory(default : 32mb) 설정값으로 accumulator 크기 설정 가능
	* accumulator 에 메시지를 축적해두었다가, 전송이 trigger 되면, sender 에서 batch.size(default: 16kb) 만큼 메시지를 bulk 로 읽어가 토픽으로 전송
		* 네트워크 트래픽 비용 감소 및, 브로커 서버 부하 감소
	* sender 에서 메시지 전송하는 속도보다, accumulator 에 축적되는 속도가 더 빨라 버퍼가 가득찰시, accumulator 는 신규 메시지 전송 요청을 max.block.ms(default: 60000) 값 만큼 block하게되며, 그 시간 이후에도 버퍼에 여유공간이 생기지 않는다면 예외 throw
	* 버퍼 full 로 인한 메시지 전송 요청 실패가 빈번히 발생한다면, accumulator 의 버퍼크기(buffer.memory) 증대 필요
* Sender
	* 브로커에 메시지를 전송하는 백그라운드 스레드
	* Accumulator 에 batch.size 만큼 메시지가 쌓여있으면 브로커로 전송
	* 쌓여있는 메시지 크기가 batch.size 보다 작아도 마지막 메시지 전송 이후 linger.ms(default 0) 만큼의 시간이 지나면 브로커로 전송
	* 브로커로 메시지 전송 실패시(브로커로부터 ack 응답 미수신) retries 로 설정한 횟수만큼 메시지 재전송 시도
	* retries 와는 별도로, 최초 메시지 전송 시도 이후 delivery.timeout.ms 까지 전송 성공하지 못할시, 재전송 중단 및 예외 throw 

![image](https://user-images.githubusercontent.com/48702893/153410414-f6ceec63-e151-4da7-b8ce-9bda37657bf0.png)

### 2. broker
* 카프카 서버로서 확장성 및 고가용성을 위해 여러개의 broker(최소 3개 이상 권장) 를 묶은 Kafka cluster 로 구성됨
* Producer 로부터 수신한 메세지 저장 및 관리 수행
* Producer 에서 메시지 전송시, round-robin 방식으로 Kafka cluster 내 broker 들에게 공평하게 할당
* zookeeper 가 Kafka cluster 의 브로커들중 하나를 컨트롤러 브로커로 선정하여 클러스터 내 브로커 관리 수행 위임

#### Controller broker
* 토픽 생성시, 설정된 파티션 수에 맞춰 각 브로커에 파티션 분배
* Kafka cluster 내 브로커들의 h/c 수행. h/c 에 실패한 브로커 발견시 failover 수행
  * 해당 브로커의 파티션을 리더 파티션으로 사용하던 토픽들의 리더 파티션 재 선출
  * 재 선출된 리더 파티션들의 정보를 zookeeper 에 저장
  * ![image](https://github.com/JisooOh94/study/assets/48702893/133d684a-b565-4d9a-bf7b-bf8f38f03a5b)
* Controller broker 의 h/c 는 zookeeper 가 수행. h/c 실패시 zookeeper 에서 controller broker 재선출 

#### topic
* 메시지 구분을 위한 타이틀 개념
* topic 생성시, 설정한 파티션 개수등의 정보가 zookeeper 에 저장되며, 해당 topic 의 메시지를 처리할 kafka cluster 또한 zookeeper 가 설정
* topic 단위로 kafka cluster, consumer group 이 묶임

#### partition
* 메시지가 저장되는 메시지 큐
* 하나의 토픽당 여러개의 파티션이 생성될 수 있으며, 각 파티션은 kafka cluster 내의 브로커들에게 골고루 나뉘어 저장 및 관리됨
    * zookeeper 에서 설정한 컨트롤러 브로커가 파티션 분배 작업 수행
* 각 브로커에 분배된 파티션들은 리더 파티션이되고, 브로커의 replication factor 설정값에 따라 각 리더 파티션들의 replica 파티션도 함께 생성된다.
* <img width="752" alt="image" src="https://github.com/JisooOh94/study/assets/48702893/978150ce-857a-4f0a-ac56-849cde1d9f6c">
* 파티션에 저장되는 각 메시지는 offset 이라는 1씩 증가하는 index 값을 가지게 되며, 이 offset 으로 파티션 내 메시지 식별 가능
* producer 가 메시지 전송시, 메시지는 해당 토픽의 파티션들에 round-robin 방식으로 골고루 저장되게 되며, 이를 통해 broker 에도 부하가 골고루 분산되게됨
* 하나의 파티션 내의 메시지간에는 LIFO 를 보장하나, 여러 파티션간 메시지는 LIFO 를 보장하지 않음
    * 메시지가 프로듀스 된 시간 순서에따라 처리가 되어야한다면, 토픽의 메시지를 하나의 파티션에만 프로듀싱하도록 파티션 고정 가능
* 토픽의 파티션 추가는 런타임시점에도 자유롭게 가능하나, 파티션 제외는 불가능(토픽 삭제 후 재생성으로만 가능)

![image](https://user-images.githubusercontent.com/48702893/149141998-24c29f47-c66d-4534-810c-3aae65f65cae.png)

### 3. consumer
* 파티션에 저장되어있는 메시지를 소비해가 처리를 수행하는 주체
* consumer 또한 마찬가지로 여러개의 consumer 를 묶은 consumer group 으로 관리되며, 토픽은 consumer group 단위로 구독됨
    * 하나의 토픽을 여러개의 consumer group 이 구독 가능
        * 하나의 메시지 데이터를 여러 용도로 사용하고자 하는 요구사항 충족
        * 각 consumer group이 개별적으로 파티션의 offset 을 관리함으로서 가능
* 토픽내의 파티션과 consumer group 내 consumer 는 1:1, N:1 의 관계는 가능하나, 1:N 의 관계는 불가능
    * 하나의 파티션을 여러개의 consumer 가 소비해가는경우, LIFO 이 깨질수 있고 last offset 관리가 어려워짐
    * partition 수보다 consumer 수가 더 많을경우, 아무런 partition 도 할당받지 못한 잉여 consumer 가 발생하므로, topic 의 partition 수에 따라 consumer group 내 consumer 수 조절이 중요
* consumer group 이 구독하고있는 topic 의 브로커중 하나가 Consumer Group Coordinator 로 선정되어 Consumer group 내 consumer 관리 수행
    * 각 consumer 는 CGC 로 hbm 전송, CGC 는 timeout 시간(session.timeout.ms) 내에 hbm 을 전송하지 않은 consumer 를 비정상 상태로 간주하고 Consumer group 에서 제외, 리밸런싱 수행
    * 리더 컨슈머(가장 첫번째로 Join Group 요청을 전송한 컨슈머) 선정 및 리더 컨슈머에게 파티션 분배 위임 (리더 컨슈머가 분배한 파티션 정보대로 각 컨슈머에게 할당된 파티션 정보 전달)
* consumer group 이 처리할 topic 설정시, topic 의 파티션들을 consumer group 내 consumer 들에게 할당하는 리밸런싱 과정 수행됨
    * consumer group 에 consumer 가 추가되거나 삭제될시, 리밸런싱 재수행
* consumer group 의 각 consumer 는 파티션에서 마지막으로 컨슈밍한 offset 정보를 저장 및 관리
    * 0.9 이전버전은 zookeeper, 이후 버전은 별도의 토픽(_consumer_offsets)에 저장
    * consumer 는 파티션에서 메세지 consume 하여 처리 후, zookeeper 에 offset 값을 증가시켜 저장하는 offset commit 수행
    * 한 consumer 에 장애가 발생해 consumer group에서 이탈할시, 그 consumer 가 담당하던 파티션들의 가장 최근에 commit 된 offset 정보를 가져와 다른 consumer 가 이어서 처리 가능
    * commit 된 offset 이 이전 consumer 가 실제 마지막으로 처리한 offset 보다 
        * 작을경우 : 메시지 중복으로 처리 됨
        * 클경우 : 처리된 offset과 commit 된 offset 사이의 모든 메시지 누락      
	
![image](https://user-images.githubusercontent.com/48702893/149145169-80291447-9b7e-45e0-a62a-b46fdd111892.png)	

### 4. zookeeper
* 연결되어있는 kafka 클러스터의 식별정보, 각 브로커 메타정보(권한, 컨트롤러 브로커 여부 등), Topic 및 partition offset 정보 저장 관리
* 브로커는 zookeeper 를 통해 메세지 저장 및 관리 작업을 위해 필요한 공유정보 조회
* zookeeper 도 가용성을 위해 여러대의 zookeepr 서버를 묶은 클러스터로 구성이 가능
  * 하나의 leader zookeeper - 나머지 follower zookeeper 로 구성
  * 변경사항이 leader zookeeper 에 먼저 반영되면 나머지 follower zookeeper 들이 동기하는 방식으로 동작 
* 하나의 zookeeper 서버로 여러개의 kafka cluster 관리 가능
    * 각 kafka cluster 의 메타정보는 kafka cluster 구성시 브로커의 zookeeper.connect 에 설정한 경로 하위에 저장됨. 따라서 해당 디렉토리만 다르게 해주면 여러개의 kafka cluster 를 하나의 zookeeper 에 연결가능

***
> Reference
> * https://team-platform.tistory.com/13
> * https://yeon-kr.tistory.com/183
> * https://needjarvis.tistory.com/603
> * https://always-kimkim.tistory.com/entry/kafka101-broker
> * https://allg.tistory.com/66?category=692384