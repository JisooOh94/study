# Broker 성능 관련 feature
## log 자료구조
* 브로커는 프로듀서로부터 받은 메시지를 "세그먼트" 라는 파일에 로그 자료구조 형태로 저장
  * 브로커의 log.dirs 설정값으로 설정한 경로 하위에 세그먼트 파일 존재
  * 세그먼트 파일은 각 토픽별로 각각 생성된다.
* 로그 자료구조는 append-only 특징(쓰기 작업이 데이터 끝에서만 수행됨, 따라서 이미 쓰여진 데이터는 수정 불가)을 가지고 있어 빠른 쓰기작업 가능

## Zero copy
* 브로커가 세그먼트 파일로부터 메시지를 읽고, 이를 네트워크를 통해 컨슈머로 전달하는 과정에서 User 영역 - Kernel 영역간 context switching 없이, 오로지 Kernel 영역에서만 수행되는 zero-copy 로 동작함

| Non zero copy | Zero copy | 
|:--:|:--:|
|![image](https://github.com/JisooOh94/study/assets/48702893/7a3de2ce-35f3-4863-b3cc-624121f9205a)|![image](https://github.com/JisooOh94/study/assets/48702893/fb61fc66-8750-408c-9003-a63b4360e0ce)|

> 다만, 메시지 암호화같이 유저 영역에서 수행되는 작업이 필요한 경우 Non zero copy 로 동작함

## 페이지 캐시
* OS에서 제공하는 CPU 캐시인 페이지 캐시에 파티션 메시지를 캐싱하여 메시지 조회 성능 향상
  * Consumer 에서 메시지 조회시, 해당 메시지 다음의 메시지들도 함께 디스크에서 조회하여 페이지 캐시에 캐싱(ReadAhead)
* 표면적으론 디스크에 기반한 영속적인 저장 방식을 사용하지만, 내부적으로 페이지 캐시를 활용하여 높은 처리량을 제공하는 인메모리 방식 처럼 동작
* Kafka 프로세스가 직접 캐시를 관리하지 않고 OS에 위임하기 때문에 Kafka를 재시작 하더라도 OS의 페이지 캐시는 그대로 유지되어, 캐시 웜업 불필요
* OS 가 애플리케이션에 할당된 메모리를 제외한 나머지 잔여 메모리를 페이지 캐시로 전환하여 디스크 접근 최소화 및 IO 성능 향상

## 메시지 Disk 저장
* 메시지들은 파티션 별로 세그먼트 라는 파일로 브로커 로컬(kafka/log/dirs)에 저장
  * append-only 자료구조인 로그 자료구조 형태로 파일에 저장하여, 빠른 쓰기 작업 가능
* 파일시스템(디스크)에 저장하기때문에 컨슈머가 프로듀싱된 메시지를 빠르게 컨슘할 필요 없이 보존 기간내에 언제든지 컨슘 가능
  * 전통적인 메시지 큐 시스템은 메시지를 브로커의 메모리에 저장
  * 이로인해, 컨슈머는 프로듀싱된 메시지를 빠르게 컨슘하여 처리 필요
  * 컨슈머의 처리속도가 느리거나, 갑자기 많은 양의 메시지가 프로듀스된다면, 브로커 서버에 OOM 에러 발생 가능
* 컨슈머의 장비 성능이 떨어지거나, 순간적인 부하 증가로 처리 속도가 느려질때, 브로커측에 컨슈머의 부하가 전파되지 않고, 컨슈머도 자신이 처리 가능한 속도로 컨슈밍 가능
* 컨슈머에서 컨슘한 메시지 처리과정중에 에러가 발생했을떄, 세그먼트 파일을 통해 다시 메시지 조회 가능
* 세그먼트 파일은 설정된 보존 기간동안 보존후, 자동 삭제됨

<img src="https://user-images.githubusercontent.com/48702893/149331939-9259ea2e-18b9-41c8-a8c2-3aa6140a076d.png" width="400" height="200">

# Broker 성능 관련 설정

## 파티션 개수
* 카프카의 메시지 처리 성능(throughput)은 토픽의 파티션 수, 컨슈머 수에 따라 결정(파티션수, 컨슈머 수를 함께 늘려줘야 성능 증가)
* 그러나, 파티션수, 컨슈머수를 늘린다고 항상 성능이 좋아지는것이 아님

### 파티션 증대에 따른 trade-off
1. 파일 핸들러 낭비
* 브로커는 모든 세그먼트 파일(파티션의 메시지 데이터 저장소)의 파일 핸들러(open file handle)를 유지한채로 관리
* 각 세그먼트 파일은 메시지 데이터 저장파일, 그 데이터의 인덱스 파일 2개로 구성되므로 하나의 파티션당 2개의 파일 핸들러 필요
* 파티션의 개수가 많아지면, 그만큼 유지해야하는 파일 핸들러의 개수도 많아지므로 메모리 사용량이 많아지고 부하 증대

2. 장애 복구 시간 증가
* 장애등으로 인해 리더파티션이 사용 불가능해질경우, 컨트롤러 브로커에서 자동으로 팔로워 파티션중 하나를 다시 리더로 선출
* 한 브로커 장비에 장애가 발생하여, 해당 브로커의 리더파티션들을 재선출할경우, 재선출하는과정에서 부하 및 딜레이가 발생
* 이 딜레이는 장애가 발생한 브로커에서 가지고있던 리더파티션의 개수가 많아질수록 증가(즉, 파티션 개수가 많아질수록 증가)
  * 브로커에 1000개의 리더파티션이 있고, 하나의 리더파티션 재선출에 5ms 가 걸린다면, 1000개의 리더파티션 재선출에는 5초가 소요
* 카프카 재단에서는 브로커 당 최대 약 2000개의 리더 파티션 수 권장

3. 파티션 변경 내용 복제 부하 증가
* 파티션 개수가 많아질수록 리더 파티션의 변경내용을 팔로워 파티션이 가져와 복사하는 과정의 부하 증대
* ISR(In Sync Replica) 로 유지하기 위해 팔로워 파티션들은 매우 짧은 주기로 리더 파티션의 변경 내용 조회하여 적용
* 브로커가 보유한 리더 파티션이 많아질수록, 토픽의 replifcation factor 가 클수록 부하가 증가함. 보통 1000개의 리더 파티션 변경 내용 복제시, 약 20ms 소요
* 일반적으로 브로커 당 리더 파티션 수를 100 x 브로커 수 x replication factor ..? 로 제한 권장

4. 프로듀서 메모리 증가
* 0.8.2 버전부터 프로듀서에서 토픽의 각 파티션에 전송할 메시지를 버퍼링
* 파티션의 개수가 많아질수록 프로듀서에 버퍼링되는 메시지 크기도 커지고, OOM 에러 발생 위험 증가
* 컨슈머 또한, 각 파티션의 메시지를 벌크로 컨슈밍하므로, 파티션 개수가 많아지면, 더 많은 메모리 사용

### 적정 파티션 개수 판단
* 토픽에 대해 초당 프로듀스 되는 메시지 수와 컨슈머 그룹의 각 컨슈머의 메시지 처리 성능에 따라 파티션 개수 및 컨슈머 개수 설정
  * e.g. 메시지 생산 속도가 10rps 인 프로듀서가 4개 있고, 컨슈머의 메시지 처리속도가 5rps 라면, 최소 파티션 8개 - 컨슈머 8개로 구성해야 메시지 펜딩 없이 최상의 throughput 을 낼 수 있다.  
* 적절한 파티션 수 예측이 어려울 경우, 우선 적은 수의 파티션으로 운용, 운용중 메시지 처리의 병목 발생시 파티션, 컨슈머 늘려가며 튜닝
  * 런타임중, 토픽의 파티션수 증가는 자유롭게 가능하나, 축소는 불가능하므로

## 쓰루풋 관련 설정
* num.network.threads
  * 네트워크 통신(요청 수신, 응답 전송)을 처리하는 스레드 수 (default : 3)
  * 프로듀서, 컨슈머와의 통신량 및 replication factor 값을 고려하여 스레드 수 설정 필요
  * 카프카에서 제공하는 "kafka.network:type=SocketServer,name=NetworkProcessorAvgIdlePercent" 메트릭(network thread 들이 idle 상태로 대기하는 시간 비율)을 참조하여 num.network.threads 값 튜닝
    * idle 비율이 30% 이하면 네트워크 스레드 수가 부족한것이다. num.network.threads 값 증가 필요
* num.io.threads
  * 요청을 처리하는 스레드 수 (default : 8)
  * 스레드수를 늘리면 throughput 이 증대될 수 있으나, cpu 자원 및 디스크 대역폭등이 부족하면 스레드수가 많아도 쓰루풋이 bound 될 수 있으므로 이들을 고려하여 늘려야한다.
  * 일반적으로 disk volume 수와 동일한 값으로 설정하는것을 권장
  * 카프카에서 제공하는 "kafka.server:type=KafkaRequestHandlerPool,name=RequestHandlerAvgIdlePercent" 메트릭(IO thread 들이 idle 상태로 대기하는 시간 비율)을 참조하여 num.io.threads 값 튜닝
    * idle 비율이 30% 이하면 네트워크 스레드 수가 부족한것이다. num.network.threads 값 증가 필요
* queued.max.requests
  * 요청 큐에 저장되어 처리 대기할 수 있는 최대 요청 수 (default : 500)
  * 큐가 가득차면 네트워크 스레드는 더이상 새로운 요청을 수신하지 않는다.
  * 요청큐가 지속적으로 가득 차있을때,
    * 서버의 cpu 리소스, 디스크 대역폭등에 여유가 있다면
    * 서버의 리소스가 부족하다면 broker 를 추가하여 scale-out 한다.
* [socket.receive.buffer.bytes](https://kafka.apache.org/documentation/#brokerconfigs_socket.receive.buffer.bytes), [socket.send.buffer.bytes](https://kafka.apache.org/documentation/#brokerconfigs_socket.send.buffer.bytes)
  * 서버 tcp socket 의 수신/송신 버퍼 크기 (default : 102400 (100 kibibytes))
  * producer 또는 consumer 와 broker 서버 사이의 네트워크 latency 가 높은경우 socket buffer 크기를 키워 한번에 더 많은 양의 segment 를 송/수신하게 함으로서 어느정도 보완 가능
  > client-server 간 network latency 는 [BDP(Bandwidth Delay Product)](https://en.wikipedia.org/wiki/Bandwidth-delay_product) 를 측정하여 알 수 있다.

## 가용성 관련 설정
* min.insync.replicas
  * 메시지 프로듀싱 성공으로 응답하기 위해 최소로 요구하는, 리더 파티션에 프로듀싱된 메시지를 복제 성공한 팔로워 파티션 개수 (default: 1)
  * acks 가 all 일경우, 브로커는 리더 파티션 프로듀싱 + min.insync.replicas 수 만큼의 팔로워 파티션 동기화 가 모두 되어야 프로듀서로 ack 를 응답한다.
  * 토픽의 replication factor 값보다 작아야하며, 일반적으로 replication factor 보다 1 ~ 2 정도 작은 값으로 설정
    * 팔로워 파티션의 목적은, (리더든, 팥로워든)파티션중 하나에 장애가 발생하더라도 문제없이 서비스가 돌아가게 하기 위함이다(fault tolerance)
    * 가용성을 높힌다고 min.insync.replicas 를 replication factor 와 동일한 값으로 설정할경우, 브로커 한대에서만 장애가 발생해도 전체 kafka cluster 장애로 이어져(오히려 가용성 저하) 위험하다.
      * 리더 파티션 뿐만 아니라 모든 팔로워 파티션에까지 동기화가 되어야 프로듀싱 성공으로 판단
      * 파티션중 하나에 장애가 발생할경우, min.insync.replicas 를 만족할 수 없고 그로인해 모든 프로듀싱 요청에 실패로 응답
* default.replication.factor
  * auto.create.topics.enable 에 의해 자동으로 생성된 topic 에 적용할 replication factor 값 (default : 1)
    * 수동으로 생성된 topic 은 topci 생성 시점에 replication factor 명시할 수 있다.
  * replication.factor 를 클러스터의 브로커 서버 수의 배수로 설정하여 각 브로커 서버에 가해지는 부하를 동일하게 유지하는것이 좋다.

## 기타 Broker 설정
* auto.create.topics.enable
  * 클라이언트에서 프로듀싱 / 컨슈밍 요청한 토픽이 존재하지 않는 토픽일시, 토픽을 새로 생성할지 여부 (default : true)
  * 상용 환경에선 미리 생성된 토픽에 대한 요청만 처리하는것이 안전하므로 false 로 설정 권장
* [num.replica.fetchers](https://kafka.apache.org/documentation/#brokerconfigs_num.replica.fetchers)
* [request.timeout.ms](https://kafka.apache.org/documentation/#brokerconfigs_request.timeout.ms)
* [message.max.bytes](https://kafka.apache.org/documentation/#brokerconfigs_message.max.bytes)
* [socket.request.max.bytes](https://kafka.apache.org/documentation/#brokerconfigs_socket.request.max.bytes)
* [connections.max.idle.ms](https://kafka.apache.org/documentation/#brokerconfigs_connections.max.idle.ms)
<details> 
<summary> 참고 </summary>
* unclean.leader.election.enable
  * ISR 파티션뿐만 아니라 OSR 파티션도 리더 파티션으로 선출될 수 있도록 설정
* broker.rack
  * 카프카 클러스터의 broker 서버들이 각기 서로 다른 rack(zone)에 떠있도록 설정
  * 가용성 증대, but 메시지 복제시 NW 부하 증가
* log.flush.interval.messages / log.flush.interval.ms
  * broker 는 producer 로부터 전송된 메시지를 메모리 버퍼(페이지 캐시)에 일정시간(log.flush.interval.ms) 저장했다가 디스크로 이동
  * 디스크로 이동시킬 메시지의 최소 메시지 크기 제한 및 마지막 디스크 이동 작업이후 최소 경과 시간
  * 값이 클수록 disk IO 적게 발생하여 처리량이 증가하나, 메시지 유실확률이 커짐
* 브로커 디스크 IO 성능
  * 브로커 디스크 IO 성능에 따라 프로듀서의 메시지 프로듀스 성능 결정
    * 프로듀서에서 메시지 프로듀스시, 브로커에서 메시지를 세그먼트파일에 저장 후, offset 커밋 완료하여 확인응답을 줄때까지 프로듀서는 대기
    * 따라서 브로커의 세그먼트 파일(디스크)write 속도가 빠를수록 Producer 대기시간 감소
  * 컨슈머의 메시지 컨슈밍 또한 브로커의 세그먼트 파일을 read 하여 수행하는것이므로 디스크 IO 성능에 영향
  * 브로커 추가
    * 브로커의 디스크 공간이 부족하거나, CPU 리소스가 부족할때 토픽에 브로커 추가 및 파티션 재분배 수행
    * 카프카에서 권장하는 최소 replication-factor 값인 3에 따라, 토픽당 최소 3개 이상의 브로커 구성
* 브로커 페이지 캐시 크기
  * 애플리케이션에 할당된 메모리를 제외한 나머지 잔여 메모리를 페이지 캐시로 사용 하므로, 잔여 메모리가 클 수록 브로커의 IO 성능 향상
  * 따라서 카프카 서버에 다른 어플리케이션을 함께 실행하는 것을 권장하지 않으며, 카프카 또한 5gb 정도의 메모리면 충분
</details>

> Reference
> * https://knight76.tistory.com/entry/kafka-lag-%EC%83%9D%EA%B8%B4%EB%8B%A4%EA%B3%A0-%ED%8C%8C%ED%8B%B0%EC%85%98-%EC%B6%94%EA%B0%80%ED%95%98%EB%8A%94-%EA%B2%83%EC%97%90-%EB%8C%80%ED%95%B4
> * https://allg.tistory.com/65
> * https://devidea.tistory.com/95?category=762832
> * https://brocess.tistory.com/79
> * https://needjarvis.tistory.com/602
> * https://www.popit.kr/kafka-consumer-group/
> * https://dydwnsekd.tistory.com/68?category=932037
> * https://needjarvis.tistory.com/601?category=925090
> * https://allg.tistory.com/65
> * https://allg.tistory.com/63?category=692384
> * https://songhayoung.github.io/2020/07/13/kafka/acks-replicas/#Introduction
> * https://always-kimkim.tistory.com/entry/kafka101-broker
> * https://always-kimkim.tistory.com/entry/kafka-operations-settings-concerned-when-the-message-has-more-than-1-mb --> 크기가 큰 메시지용 kafka 설정
> * https://access.redhat.com/documentation/en-us/red_hat_amq_streams/2.3/html/kafka_configuration_tuning/con-broker-config-properties-str#doc-wrapper
> * https://godekdls.github.io/Apache%20Kafka/broker-configuration/
> * https://free-strings.blogspot.com/2016/04/blog-post.html
> * https://access.redhat.com/documentation/en-us/red_hat_amq_streams/2.3/html/kafka_configuration_tuning/con-broker-config-properties-str#doc-wrapper
> * https://kafka.apache.org/documentation/#configuration
> * https://always-kimkim.tistory.com/entry/kafka101-broker
> * https://soft.plusblog.co.kr/3