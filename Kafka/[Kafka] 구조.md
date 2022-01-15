# 구조

![image](https://user-images.githubusercontent.com/48702893/149144910-d9f5199a-575c-4fe6-8187-c3a0cdc3f6f7.png)

* Producer - Broker - Consumer 로 구성
	* Producer 에서 메시지 생성하여 Broker 로 전송
	* Broker 에 메시지가 저장되어있으면, Consumer 가 메시지 읽어 처리

### zookeeper
* 연결되어있는 kafka 클러스터, 각 브로커 메타정보(권한, 컨트롤러 브로커 여부 등), Topic 및 partition offset 정보 저장 관리 
* 브로커는 zookeeper 를 통해 메세지 저장 및 관리 작업을 위해 필요한 공유정보 조회
* zookeeper 도 여러대의 서버를 묶은 클러스토 관리되며, 하나의 zookeeper 클러스터가 여러개의 kafka cluster 관리 가능
	* 각 kafka cluster 의 메타정보를 저장하는 디렉토리만 구분해주면 됨

### broker
* 카프카 서버, 메세지 큐 저장 및 관리 수행
* 확장성 및 고가용성을 위해 여러개의 broker 를 묶은 Kafka cluster 로 관리됨
* Producer 에서 메시지 전송시, round-robin 방식으로 클러스터내의 broker 들에게 공평하게 할당
* Kafka cluster 의 브로커들중 하나를 컨트롤러 브로커로 선정하여 클러스터 내 브로커 관리 수행
	* 브로커 상태 체크
	* 브로커 사망시, 브로커의 파티션을 리더 파티션으로 사용하던 토픽의 리더 파티션 재 선출
	* 각 브로커에 파티션 분배

### topic
* 메시지 구분을 위한 타이틀 개념
* topic 생성시, zookeeper 에 정보가 저장되며, 해당 topic 의 메시지를 처리할 kafka cluster 를 zookeeper 가 설정
* topic 단위로 kafka cluster, consumer group 이 묶임

### partition
* 메시지가 저장되는 메시지 큐
* 하나의 토픽당 여러개의 파티션이 생성될 수 있으며, 각 파티션은 kafka cluster 내의 브로커들에게 골고루 나뉘어 저장 및 관리됨
	* zookeeper 에서 설정한 controller 브로커가 파티션 분배 작업 수행
* 파티션에 저장되는 각 메시지는 offset 이라는 1씩 증가하는 index 값을 가지게 되며, 이 offset 으로 파티션 내 메시지 식별 가능
* producer 가 메시지 전송시, 메시지는 해당 토픽의 파티션들에 round-robin 방식으로 골고루 저장되게 되며, 이를 통해 broker 에도 부하가 골고루 분산되게됨
* 하나의 파티션 내의 메시지간에는 LIFO 를 보장하나, 여러 파티션간 메시지는 LIFO 를 보장하지 않음
	* 메시지가 프로듀스 된 시간 순서에따라 처리가 되어야한다면, 토픽의 메시지를 하나의 파티션에만 프로듀싱하도록 파티션 고정 가능

![image](https://user-images.githubusercontent.com/48702893/149141998-24c29f47-c66d-4534-810c-3aae65f65cae.png)

### consumer
* 파티션에 저장되어있는 메시지를 소비해가 처리를 수행하는 주체
* consumer 또한 마찬가지로 여러개의 consumer 를 묶은 consumer group 으로 관리되며, 토픽은 consumer group 단위로 구독됨
	* 하나의 토픽을 여러개의 consumer group 이 구독 가능
* 토픽내의 파티션과 consumer group 내 consumer 는 1:1, N:1 의 관계는 가능하나, 1:N 의 관계는 불가능
	* 하나의 파티션을 여러개의 consumer 가 소비해가는경우, LIFO 이 깨질수 있고 last offset 관리가 어려워짐
	* partition 수보다 consumer 수가 더 많을경우, 아무런 partition 도 할당받지 못한 잉여 consumer 가 발생하므로, topic 의 partition 수에 따라 consumer group 내 consumer 수 조절이 중요
* consumer group 이 처리할 topic 설정시, topic 의 파티션들을 consumer group 내 consumer 들에게 할당하는 리밸런싱 과정 수행됨
	* consumer group 에 consumer 가 추가되거나 삭제될시, 리밸런싱 재수행
* consumer group 은 각 consumer 가 할당된 파티션에서 마지막으로 컨슈밍한 offset 정보를 zookeeper 로부터 할당받아 저장 및 관리
	* 한 consumer 에 장애가 발생해 consumer group에서 이탈할시, 그 consumer 가 담당하던 파티션들의 last offset 정보를 가져와 다른 consumer 가 이어서 처리 가능
	* consumer 는 파티션에서 메세지 consume 하여 처리 후, zookeeper 에 offset 값을 증가시켜 저장하는 offset commit 수행
	
![image](https://user-images.githubusercontent.com/48702893/149145169-80291447-9b7e-45e0-a62a-b46fdd111892.png)	

***
> Reference
> * https://team-platform.tistory.com/13
> * https://yeon-kr.tistory.com/183
> * https://needjarvis.tistory.com/603
> * https://always-kimkim.tistory.com/entry/kafka101-broker