# Consumer 성능 관련 feature

### Pull 방식 Consumer
* 기존 메시징 시스템(e.g. RabbitMq)의 경우 브로커가 컨슈머에게 메시지를 전송하는 Push 방식 사용(옵저버 패턴)
  * 컨슈머의 처리율을 초과하는 양의 데이터 전송시, 컨슈머 버퍼에 적체되다 버퍼 용량 초과하면서 장애 발생
  * 따라서 브로커는 메시지 전송시, 컨슈머의 상태 고려 필요
* 카프카는 컨슈머가 브로커에게서 메시지를 가져오는 Pull 방식(일종의 Reactive BackPressure) [Reactive BackPressure](https://github.com/JisooOh94/study/blob/master/Reactive/2.%20%EB%B0%B1%ED%94%84%EB%A0%88%EC%85%94.md)
  * 컨슈머가 자신이 처리 가능한 양만큼만 메시지 가져오기때문에 Push 방식의 문제 해결

### 브로커의 메시지 배치 처리
* 메시지를 디스크에 저장하므로, 메시지를 버퍼링했다가 한번에 컨슘하여 처리하는 배치 처리 가능
  * Record Batch : Kafka에서는 쓰기 처리량을 높이기 위해 여러 개의 메시지(record)를 묶어 한번에 저장한다. 이 메시지 묶음을 Record Batch라고 하며, Record Batch에는 1개 이상의 메시지가 포함되어 있다. 또한 하나의 세그먼트에는 1개 이상의 Record Batch가 포함될 수 있다.
* 네트워크 트래픽 비용 절약 가능, 버퍼링을 통해 연속적인 디스크 공간에 메시지 저장하므로 I/O 성능 향상

# Consumer 성능 관련 설정
* group.id
  * 컨슈머가 속하는 컨슈머 그룹 id
* session.timeout.ms
  * 컨슈머와 Consumer Group Coordinator 사이의 세션 타임 아웃 시간(컨슈머가 CGC 에게 본 설정값을 전송, CGC 는 이 값으로 컨슈머와의 session timeout 판단)
  * 컨슈머는 heartbeat.interval.ms 시간 간격으로 Consumer Group Coordinator에게 hbm 전송
  * 컨슈머가 session.timeout.ms 시간 내에 hbm 을 Consumer Group Coordinator으로 날리지 않으면, Consumer Group Coordinator는 해당 컨슈머에 장애가 발생한것으로 인지하고 리밸런싱 수행
  * 값이 작을수록 더 빠르게 컨슈머의 장애를 감지할 수 있다는 장점이 있으나, 실제 장애가 아닌, 일시적인 지연까지 장애로 감지하여 빈번한 리밸런싱([빈번한 리밸런싱이 좋지 않은 이유]())을 유발하는 단점 존재
  * default : 300000 (5 min)
* heartbeat.interval.ms
  * kafka consumer 스레드의 health 를 체크하기 위한 설정값이며, 컨슈머가 Consumer Group Coordinator에게 hbm 을 보내는 시간 주기
  * session.timeout.ms 보다 작게 설정해야한다. 일반적으로 session.timeout.ms 의 1/3 값으로 설정한다. 
  * heartbeat.interval.ms 가 작을수록 빈번하게 hbm 을 전송하여 불필요한 리밸런싱을 방지 할 수 있으나, 그만큼 Consumer Group Coordinator에게 부하가 증가된다는 단점 존재
  * default : 3000
* max.poll.interval.ms
  * 메시지를 처리하는 어플리케이션 프로세스 스레드의 health 를 체크하기 위한 설정값이며 컨슈머에서 poll() 을 호출한후, 다음 poll() 을 호출할때까지 최대 대기시간
  * 이 시간내애 poll() 을 호출하지 않으면 Consumer Group Coordinator 는 해당 컨슈머가 장애 상황으로 판단하고 리밸런싱을 수행한다.
      * 엄밀히 말하자면, 브로커가 장애상황으로 판단하는것이 아닌, 컨슈머가 스스로 더이상 hbm 을 브로커로 전송하지 않아 장애상황 인것처럼 연기하는것이다.
  * 어플리케이션이 정상 상황일때 poll 로 읽어온 메시지들의 평균 처리 시간에 맞춰 본 설정값을 튜닝해야한다.
  * default : 300000
* max.poll.records
  * poll() 로 가져올 수 있는 최대 레코드 배치 크기(최대 메시지 개수)
  * 한번에 너무 많은 메시지를 가져오면, 처리에 오래걸려 max.poll.interval.ms 시간 내에 다음 poll() 을 수행하지 못할수도 있다. max.poll.interval.ms 에 맞춰 튜닝 필요
  * default : 500
* fetch.min.bytes
  * 컨슈머가 브로커로 fetch 요청을 전송했을때([poll 과 fetch 의 차이]()), 브로커가 내려줄 수 있는 레코드 배치 최소 크기 (default : 1 byte)
  * 브로커의 파티션에 쌓여있는 레코드가 fetch.min.bytes 이하일경우, fetch.min.bytes 이상 쌓일때까지 기다렸다가 응답한다.
  * 너무 빈번하게 featch 요청이 전송되는것을 방지하기때문에 네트워크 트래픽도 절약할 수 있고, 브로커에 가해지는 부하도 감소한다.
  * default : 1
* fetch.max.wait.ms
  * 브로커는 컨슈머로부터 fetch 요청을 수신했을때, fetch.min.bytes 만큼의 레코드가 파티션에 쌓여있지 않을경우 응답하지 않고 기다린다.
  * 이때, 무한히 응답을 미룰 수 없으므로, 대기시간이 fetch.max.wait.ms 이 넘어가면 fetch.min.bytes 만큼 레코드가 쌓이지 않았어도 응답한다. (default : 500ms)
  * default : 500
* fetch.max.bytes
  * 컨슈머가 한번의 fetch 요청으로 브로커로부터 가져올 수 있는 최대 레코드 배치 크기
  * 하지만 절대적인 값은 아니다. fetch.max.bytes 보다 큰 크기의 레코드 배치를 가져올 수 있다.
    * 하나의 컨슈머는 여러개의 파티션을 구독할 수 있고, 각 파티션은 여러개의 브로커 서버에 분산되어 있을 수 있다.
    * 따라서 컨슈머의 fetch 요청도 여러개의 브로커 서버로 갈 수 있으며 이때, 컨슈머가 수신할 수 있는 전체 메시지의 최대크기는 fetch.max.bytes * fetch 요청한 브로커 서버수 이다.
  * 일반적으로, 브로커가 저장할 수 있는 최대 메시지 크기 설정값(message.max.bytes) 보다 큰 값으로 설정하는것이 좋다.
  * default : 57671680 (55 mebibytes)
* max.partition.fetch.bytes
  * 컨슈머가 한번에 fetch 요청으로 하나의 파티션으로부터 가져올 수 있는 최대 레코드 배치 크기
  * fetch.max.bytes 와 마찬가지로, 컨슈머는 여러개의 파티션을 구독할 수 있기때문에, 컨슈머가 수신할 수 있는 전체 레코드 배치 최대 크기는 이 설정값보다 커질 수 있다.
  * 일반적으로, 브로커가 저장할 수 있는 최대 메시지 크기 설정값(message.max.bytes) 보다 큰 값으로 설정하는것이 좋다.
  * default : 1048576 (1 mebibytes)
* auto.offset.reset
  * 토픽에 대한 offset 정보가 없을때, 파티션의 몇번째 메시지부터 컨슘하기 시작해야하는지에 대한 정책
      * 생성되고 난후 아직 컨슈머 그룹 할당이 되지 않은 토픽이거나, offsets.retention.minutes 에 의해 offset 정보가 삭제된 토픽일경우 offset 정보가 없을 수 있다.
  * latest, earliest, none 중에 선택 가능하며 default 는 latest.
      * latest : 파티션의 가장 마지막 메시지의 offset 사용
      * earliest : 파티션의 가장 첫번째 메시지의 offset 사용
      * none : 토픽의 offset 정보가 없을경우, 컨슈머에게 에러 응답
  * default : latest

### 컨슈머 그룹 및 컨슈머 개수
* group.id 설정값으로 컨슈머들을 그룹화 가능
* 토픽을 구독하는 컨슈머 그룹의 컨슈머 개수는 토픽의 파티션 개수의 약수로 설정하는것이 바람직
  * 파티션 개수보다 컨슈머의 개수가 많을경우, 파티션을 할당받지 못한 컨슈머는 무한히 대기하게 되므로 컨슈머 낭비 발생
  * 컨슈머 개수가 파티션 개수의 약수가 아니면, 각 컨슈머에 파티션이 비 균일하게 할당되어 특정 파티션에 부하가 몰리는 현상 발생가능
* 컨슈머 그룹내 컨슈머가 많아질경우, CGC 브로커 서버에 가해지는 부하도 함께 커짐. 따라서 쓰루풋을 위해 컨슈머 증대시, CGC 브로커 서버 가용량도 확인해야함
  * 컨슈머가 많아질수록, CGC 브로커로 hbm 을 전송하는 클라이언트(==컨슈머)가 많아지는것과 같다.

### 메시지 배칭 사이즈 조절을 통한 throughput 증대
* fetch.max.wait.ms, fetch.min.bytes 값을 증대 시키면, 컨슈머가 브로커로부터 한번에 읽어오는 메시지 배치 크기를 늘릴 수 있다.
* 이를 통해 같은 양의 메시지를 더 적은 요청 횟수로 가져올 수 있게되어, 네트워크 대역폭 절약 및 메모리 절약, cpu 리소스 절약이 가능하다.
  * 또한, 컨슈머 <-> 브로커 서버간 네트워크 latency 가 높은경우에도 효과가 있다. (tcp socket 버퍼 크기 증대와 같은 효과)
* 하지만, 일부 메시지(버퍼에 가장 처음 enqueue 된 메시지)는 처리 latency 가 높아지므로 요구사항에 맞춰 적절히 증대해야 한다.
  * 반대로, 빠른 메시지 처리가 중요하고 broker, consumer 리소스에 여유가 있다면 fetch.max.wait.ms, fetch.min.bytes 값을 낮춰 latency 를 줄일 수 있다.  
  > 애초에 mq 자체가 비동기로 처리하는것이므로 latency 가 딱히 중요한 성능 지표는 아닌듯하다.

### 최대 fetch 사이즈 증대를 통한 latency 개선
* fetch.max.bytes, max.partition.fetch.bytes 값을 높혀 브로커로부터 한번에 읽어올 수 있는 최대 메시지(레코드 배치) 크기를 증가시킨다.
* 이를 통해, 더 적은 수의 요청으로 더 많은 양의 메시지를 읽어와 처리할 수 있기때문에 latency 가 개선된다.
* 하지만, 두 설정값 수정을 통해 최대 메시지 크기가 증가되면, 최대 소모될 수 있는 컨슈머 메모리 사용량 또한 증가되므로 먼저 컨슈머 메모리 스펙 확인후 그에 맞춰 설정해야한다.  
  * 최대 소모될 수 있는 컨슈머 메모리 사용량은 아래와 같은 공식으로 계산
  ```
  min(NUMBER-OF-BROKERS * fetch.max.bytes, NUMBER-OF-PARTITIONS * max.partition.fetch.bytes)
  ```

### offset auto-commit 비활성화
* kafka 의 auto-commit 은 브로커로부터 레코드 배치 수신후, auto.commit.interval.ms 로 설정한 시간만큼 지나면 자동으로 수신한 메시지들의 offset 을 commit 해주는 기능이다.
* 문제는, 수신한 메시지들의 처리 완료 여부와는 상관없이 auto.commit.interval.ms 시간만 지나면 commit 한다는점인데, 이로인해 아래와 같이 메시지 유실 / 중복 처리등의 문제가 발생가능하다.
  * 메시지 유실
    1. 브로커로부터 수신한 메시지들을 아직 처리중에 auto.commit.interval.ms 시간이 흐름. 메시지 처리가 완료되지 않았음에도 offset commit 됨
    2. 이때, 컨슈머 서버의 장애로 메시지 처리를 완료하지 못한 상태로 컨슈머 사망. 파티션 리밸런싱 수행됨
    3. 새로 파티션을 할당받은 컨슈머는 이전 메시지가 아직 정상적으로 처리가 되지 않았음에도 이미 offset commit 이 되었기에, 다음 메시지부터 컨슘 시작
    4. 이로인해 처리가 되지 않은 메시지가 발생하는, 메시지 유실 발생
  * 메시지 중복
    1. 브로커로부터 메시지 수신하여 처리중. 아직 auto.commit.interval.ms시간도 지나지 않아 offset 도 commit 되지 않은 상태
    2. 메시지의 일부 처리(e.g 수신한 메시지의 내용을 db의 a,b,c table 에 반영해야하나 a,b 테이블에만 반영)만 진행된 상태에서 컨슈머 서버에 장배 발생하여 컨슈머 사망. 파티션 리밸런싱 수행됨
    3. 새로 파티션을 할당받은 컨슈머는 이전 메시지를 다시 컨슘.
    4. 해당 메시지에 대해 이전에 이미 수행되었던 일부 처리가 중복으로 다시 수행됨. 메시지 중복 발생
* 위와같은 문제로 인해, at-least-once 혹은 exactly-once 처리를 보장해야하는 서비스의 경우엔 auto-commit 을 비활성화하고, 직접 offset commit 을 컨트롤해야한다.

#### spring manual offset commit
* spring kafka 는 manual offset commit 으로 syncCommit 방식, asyncCommit 방식 2가지를 제공한다.
* syncCommit (consumer.commitSync())
  * broker 로부터 commitSync 에 대한 응답이 올때까지 스레드가 blocking 되어 기다리는 방식. 쓰루풋이 다소 떨어진다.
  * commitSync 호출후, 브로커로부터 에러 응답이 오는등의 retryable 한 에러 발생시, 자동으로(?) retry 수행함으로서 메시지의 exactly-once 처리를 보장한다.
  * syncCommitTimeout 을 설정하여, broker 로부터 일정시간 내에 응답이 없을경우 timeoutException 발생하도록 처리 필요하다.

  ```java
  while (true) {
    ConsumerRecords<Integer, AvroMessage> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<Integer, AvroMessage> record : records) {
        System.out.println("Received message: (" + record.key() + ", " + record.value().toString() + ") at offset " + record.offset());
    }
    try{
        consumer.commitSync();
    }catch (CommitFailedException e){
        System.out.println("Commit failed due to : "+ e);
        e.printStackTrace();
    }
  }
  ```

* asyncCommit (consumer.commitAsync())
  * broker 로 commitAsync 전송후, 스레드가 blocking 되지 않고 다음 처리를 이어나간다. 쓰루풋 저하가 없음
  * 하지만, 그로인해 commitAsync 에 실패해도 재시도를 할 수 없다. 따라서 offset commit에 실패하여 메시지가 중복으로 처리될 가능성이 있다.

  ```java
  while (true) {
    ConsumerRecords<Integer, AvroMessage> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<Integer, AvroMessage> record : records) {
        System.out.println("Received message: (" + record.key() + ", " + record.value().toString() + ") at offset " + record.offset());
    }
    consumer.commitAsync();
  }
  ```

#### spring KafkaListenerContainer 를 이용한 offset commit
* KafkaListenerContainer 를 이용하여 consumer 를 구성할경우, kafkaListener 에서 offset commit 을 대신 수행해준다.
* KafkaListenerContainer 에 설정한 offset commit 방식을 사용하고자 할경우, 마찬가지로 auto offset commit 을 비활성화 해야한다.

```java
Map<String, Object> consumerConfig = getDefaultKafkaConsumerConfig();
consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
return new DefaultKafkaConsumerFactory<>(consumerConfig);
```

* KafkaListenerContainerFactory 에 offset commit 방식을 명시해야하며 제공되는 방식은 아래와 같다.

| AckMode          | Desc |
|:------------------:|:--:|
| RECORD           | 각각의 메시지가 처리 완료될떄마다 commit 수행 |
| BATCH            | 메시지가 처리 완료될때마다 offset commit 하지 않고 기다렸다가, 일정 수준 이상 쌓이면 그때 마지막 처리한 메시지의 offset 을 commit (즉, 각각 commit 하지 않고, batch 로 묶어서 commit) |
| TIME             | 마지막 offset commit 으로부터 ackTime 시간 이후 offset commit 수행 |
| COUNT            | ackCount 수 만큼 메시지 처리 후 offset commit 수행 |
| COUNT_TIME       | TIME, COUNT 둘중 하나의 조건이라도 만족되면 offset commit 수행 |
| MANUAL           | 메시지 처리 완료후 ackowledgement.acknowledge() 호출시, offset commit 하지 않고 기다리다가 다음번 poll() 호출때 offset commit 을 수행한다. |
| MANUAL_IMMEDIATE | 메시지 처리 완료후 ackowledgement.acknowledge() 호출시, 즉시 offset commit 을 수행한다. |

```java
ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory();
kafkaListenerContainerFactory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
```

* KafkaListenerContainerFactory 를 통해 sync offset commit, async offset commit 또한 설정할 수 있다.

```java
kafkaListenerContainerFactory.getContainerProperties().setSyncCommits(false);
```

### auto.offset.reset 정책에 따른 메시지 유실
* auto.offset.reset 의 default 값인 latest 정책은 메시지가 유실 될 수 있는 문제를 가지고있다.
    * 파티션에 데이터가 10시부터 17시까지 쌓이고 있고 컨슈머그룹을 17시에 붙였다면, latest 정책의 경우 10시부터 17시까지의 데이터는 가져오지 않고 17시에 가장 마지막으로 프로듀싱된 메시지부터 컨슘하므로 메시지 유실이 발생한다.
* 따라서 earliest 로 설정하여 메시지 유실을 방지하는것이 좋다.

```java
메시지 중복 처리의 가능성이 있는것 아닌가? 토픽에 컨슈머 그룹이 할당되어 처리중이었다고 가정한다.
컨슈머 그룹에 문제가 생겨 토픽으로부터 제외되었고 그상태로 offsets.retention.minutes 만큼 시간이 지나 이전 컨슈머그룹이 commit 했던 offset 정보도 사라졌다.
그 상태로 새로운 컨슈머 그룹을 할당하면, auto.offset.reset=earliest 설정에 의해 각 파티션의 첫번째 메시지부터 컨슘하기 시작하고, 그러면 이전 컨슈머그룹이 처리했던 메시지들도 다시 처리되는것이다.
[seek(), assign()](https://www.conduktor.io/kafka/java-consumer-seek-and-assign/)  raw api 를 이용해 직접 offset 을 설정하는 방법도 있는것같다. 이를 통해 위와같은 케이스 대처 가능할듯
```

### Static membership 을 통한 rolling-restart 시 리밸런싱 회피
* 일반적인 경우에, 컨슈머가 컨슈머 그룹에 참여시 자동으로 생성한 unique id 를 가지고 Consumer Group Coordinator에게 JoinGroupRequest 요청을 보낸다.
* Consumer Group Coordinator는 unique id 를 가지고 신규 컨슈머를 인지, 파티션 리밸런싱을 수행한다. 이같은 방식이 dynamic membership 이다.
* 이때, 배포등을 위해 컨슈머 서버들을 rolling restart 하는경우 한대씩 restart 할때마다 매번 리밸런싱이 발생한다. 
* 이로인해 restart 시간이 지연되고, 이미 restart 가 끝난 컨슈머들도 나머지 컨슈머들의 restart 가 모두 끝날때까지 메시지 처리를 하지 못하는 문제가 발생한다.
* 이를 위해, 컨슈머 마다 고정된 unique id 를 할당하여 restart 시에도 리밸런싱이 발생하지 않도록 하는 static membership 이 존재한다.
  1. group-instance-id 설정값을 통해 각 컨슈머마다 고정된 unique id 를 설정해준다.
  2. consumer restart 시간을 측정하여 해당 시간보다 더 긴 시간을 session.timeout.ms, max-poll-interval-ms 로 설정한다.
* 위와같이 설정할경우, restart 동안 session timeout 이나 poll timeout 발생으로 인한 리밸런싱도 일어나지 않고, restart 완료되었을때도 이전과 동일한 unique id 로 JoinGroupRequest 를 전송하므로 동일한 컨슈머로 인식되어 리밸런싱 없이 이전과 동일한 파티션을 할당받는다.


### 빈번한 리밸런싱이 좋지 않은 이유
* 리밸런싱은 기본적으로 Stop the world 로 수행된다.
    * 컨슈머 리밸런싱이 일어날 때, 모든 컨슈머에 할당된 파티션이 해제(revoke)되므로 새로 파티션이 할당되기 전까진 모든 데이터 처리가 일시 정지된다.
    * Kafka v2.4 부터 Eager Rebalance > [Incremental Cooperative Rebalance(증분 리밸런싱)](https://cwiki.apache.org/confluence/display/KAFKA/KIP-429%3A+Kafka+Consumer+Incremental+Rebalance+Protocol) 으로 리밸런싱 전략이 수정되면서 STW 없이(파티션 단위 STW 는 존재) 리밸런스가 수행된다.

<img width="913" alt="image" src="https://github.com/JisooOh94/study/assets/48702893/0e760fdf-b3d3-438f-b744-2a8484e7728d">

> 모든 컨슈머는 'Synchronization barrier'를 넘어가기 전에 메시지 처리를 중지하고 오프셋을 커밋해야 한다.

* 따라서 불필요한 리밸런싱을 최소화 하여 stop the world 시간을 최소화하는것이 중요
    * 불필요한 리밸런스를 줄이기 위해서는 max.poll.interval.ms와 max.poll.records를 적절히 조정하여 poll 메서드가 일정 간격으로 호출되도록 해야 한다.(프로세스 장애 오탐 방지) 
    * 필요한 경우에는 heartbeat.interval.ms와 session.timeout.ms를 조정한다. (컨슈머 장애 오탐 방지)

* 또는, Stop the world 없이 리밸런싱을 수행하는 [증분 리밸런싱(kafak 2.3.0 부터 추가)]()을 도입을 고려

### poll 과 fetch 의 차이
* poll : consumer 가 브로커로부터 메시지를 가져오기 위해 호출하는 메서드
* fetch : fetcher(consumer 가 호출한 poll 요청 처리 모듈) 에서 브로커에 데이터를 요청하는 request를 전송하는 메서드 
* 즉, consumer 는 poll 메서드로 메시지를 요청하고, poll 메서드 내부적으로 fetch 가 호출되며 fetch 메서드에서 브로커로부터 메시지를 받아와 consumer 에게 전달해주는 형식

<img width="882" alt="image" src="https://github.com/JisooOh94/study/assets/48702893/2cc0fda3-afda-43c0-be03-caa1ce35dae6">

### 멱등 컨슈머
* 컨슈머도 프로듀서와 마찬가지로 Exactly once 컨슈밍을 보장하는 멱등 컨슈머 구축이 가능하다.
* 다만 Exactly once 프로듀싱을 자동으로 보장해주는 idempotent producer 가 있는 프로듀서와는 다르게, 컨슈머는 At least once / At most once 컨슈밍 중 하나만 보장해준다.
* 따라서 At least once 컨슈밍 으로 설정 후, At most once 는 별도 비즈니스 로직을 통해 보장해주어야 한다.
  * At lease once 컨슈밍 설정 : offset auto-commit 비활성화 + 컨슈밍한 메시지 offset commit 을 메시지 처리 완료 후에 하도록 설정. 이를 통해 메시지가 정상적으로 처리되었을때에만 다음 메시지를 컨슈밍하게 함으로서 At lease once 컨슈밍 보장
* At most once 를 보장하기 위해선
  1. 메시지 전체 처리 과정을 하나의 트랜잭션으로 묶는다. 이를 통해, 일부 처리 로직이 실패하여 다시 메시지 컨슈밍 및 처리할때에도 멱등성을 보장한다.
  2. 메시지 처리 완료시, offset commit 전, 처리한 메시지의 식별자를 저장공간에 저장한다.
  3. 이후 메시지 컨슈밍하여 처리하기전, 저장공간을 조회하여 이미 처리되었던 메시지인지 먼저 판단후 처리하도록 한다.

### 트랜잭션 컨슈머
* Consumer 의 메시지 처리 로직을 트랜잭션으로 관리하는것
  * e.g. 메시지를 컨슘하여 처리하고, 결과를 데이터베이스에 저장하는 일련의 작업을 하나의 트랜잭션으로 처리

```java
@Transactional
public void processMessage(ConsumerRecord<String, String> record) {
    // 비즈니스 로직 처리
    // 데이터베이스 업데이트
}
```

* 메시지 처리 로직이 모두 성공했을때에만 offset commit 수행, 에러 발생시 수행했던 처리 롤백
  * 이를 통해, exactly-once-consuming 보장 (단, 처리 로직이 DB update 가 아닌경우, 별도 roll-back 로직 필요)
* offset commit 은 트랜잭션 내에서 수행되지 않는다. 따라서 offset commit 중, 에러 발생시에는 exactly-once-consuming 보장 못함

### Fetch-Offset-Request Fencing
* 더이상 유효하지 않은 Consumer 에서 메시지를 소비하는 것을 방지하는 기술
  * consumer 장애로 인한 consumer group 에서의 탈락, 또는 파티션 추가/삭제로 인한 리밸런싱 이후 이전에 할당되었던 파티션의 메시지를 컨슈밍하는 경우 등
  * `eosMode=V2` 와 같이 설정하여 활성화
* EOS(Exactly Once Semantics) 를 지원하기 위해 Kafka 2.5 부터 도입
  * Consumer의 장애나 네트워크 문제 발생 시에도 안전한 오프셋 관리를 가능하게 하여, 데이터 중복 또는 유실 없이 정확히 한 번의 메시지 처리를 보장
* 주로 Kafka Streams 또는 트랜잭션을 사용하는 Producer와 연동되는 경우에 사용
* 처리 과정
  1. CGC 연결: Consumer가 파티션을 할당받아 메시지를 소비하기 시작할 때, CGC는 해당 Consumer에게 "epoch"라는 일련번호도 함께 할당
  2. Epoch 검증: Consumer가 메시지 소비후 오프셋 커밋시, epoch 번호가 함께 전송. 브로커는 이 epoch 번호를 검증하여, 현재 유효한 Consumer 세션에서 발생한 요청인지 확인
  3. Fencing Mechanism: 만약 더 이상 유효하지 않은 consumer에서 요청을 보낸 경우, 브로커에서 요청 거부. 이를 통해 잘못된 오프셋 커밋이나 메시지 소비를 방지하여 데이터의 중복 처리나 유실을 막음

***
> Reference
> * https://soft.plusblog.co.kr/14
> * https://stackoverflow.com/questions/49649241/apache-kafka-batch-size-vs-buffer-memory
> * https://firststep-de.tistory.com/43
> * https://leeyh0216.github.io/posts/kafka_concept/
> * https://access.redhat.com/documentation/en-us/red_hat_amq_streams/2.3/html/kafka_configuration_tuning/con-consumer-config-properties-str
> * https://4orty.tistory.com/20
> * https://firststep-de.tistory.com/44
> * https://medium.com/@rinu.gour123/kafka-performance-tuning-ways-for-kafka-optimization-fdee5b19505b
> * https://redpanda.com/guides/kafka-performance/kafka-performance-tuning
> * https://strimzi.io/blog/2021/01/07/consumer-tuning/
> * https://access.redhat.com/documentation/en-us/red_hat_amq_streams/2.3/html/kafka_configuration_tuning/con-consumer-config-properties-str
> * https://kafka.apache.org/documentation/#consumerconfigs_fetch.max.wait.ms
> * https://godekdls.github.io/Apache%20Kafka/consumer-configuration/
> * https://leeyh0216.github.io/posts/kafka_concept/
> * https://bigdatalab.tistory.com/25
> * https://www.baeldung.com/java-kafka-send-large-message
> * https://kafka.apache.org/documentation/#brokerconfigs_message.max.bytes
> * https://pula39.tistory.com/19
> * https://medium.com/@rramiz.rraza/kafka-programming-different-ways-to-commit-offsets-7bcd179b225a
> * https://docs.spring.io/spring-kafka/docs/1.1.1.RELEASE/reference/htmlsingle/
> * https://www.baeldung.com/spring-retry-kafka-consumer
> * https://p-bear.tistory.com/59
> * https://4orty.tistory.com/20#recentEntries
> * https://velog.io/@lsang89/Kafka-Consumer-Rebalancing-%EA%B4%80%EB%A6%AC
> * https://www.confluent.io/blog/dynamic-vs-static-kafka-consumer-rebalancing/
> * https://medium.com/@rinu.gour123/kafka-performance-tuning-ways-for-kafka-optimization-fdee5b19505b
> * https://d2.naver.com/helloworld/0974525
> * https://redpanda.com/guides/kafka-performance/kafka-performance-tuning
> * https://bigdatalab.tistory.com/25
> * https://devidea.tistory.com/100
> * https://studyandwrite.tistory.com/547
> * https://ssnotebook.tistory.com/48
> * https://bistros.tistory.com/entry/Kafka-idempotent-producer-%EB%A9%B1%EB%93%B1%EC%84%B1%EC%97%90-%EA%B4%80%ED%95%B4%EC%84%9C
