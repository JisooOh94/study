# Producer 설정

![image](https://user-images.githubusercontent.com/48702893/153410414-f6ceec63-e151-4da7-b8ce-9bda37657bf0.png)

### Accumulator
* buffer.memory
    * 메시지를 축적하는 Accumulator 버퍼 크기
    * 너무 작게 설정하면, 버퍼가 가득 차있는 상황이 자주 발생한다. 메시지 produce 요청시 버퍼가 가득 차있으면 max.block.ms 만큼 대기한후 exception 이 발생하기때문에 메세지가 유실된다.
    * 너무 크게 설정하면, 버퍼가 가득차는 상황을 방지함으로서 메시지가 blocking 되어 지연되는 일이 없어져 쓰루풋이나 레이턴시가 개선되나 잉여 메모리가 많아져 리소스를 낭비하게 된다.
    * 어플리케이션에서 전송하는 메시지 평균 크기, batch.size 값을 기반으로 설정한다.
    * default : 33554432 byte
* max.block.ms
    * 메세지 프로듀싱시, 버퍼가 가득차있거나 메타 데이터 응답을 기다리는등의 대기작업이 필요할때 max.block.ms 값으로 설정된 시간동안 대기. max.block.ms 만큼 대기했는데도 프로듀싱시 불가능할때, TimeoutExceptiopn throw
    * 너무 짧게 설정한다면, TimeoutExceptiopn throw 로 메시지 유실이 많아짐. 순간적인 네트워크 지연이나 브로커단 딜레이로도 메시지 유실됨
    * 너무 길게 설정한다면, 대기하는 스레드가 많아져 시스템에 부하가 가중되고 전체적인 어플리케이션 응답시간이 지연됨
    * 일반적으로, 어플리케이션 응답 속도가 중요할경우 100 ~ 500ms, 안정성이 중요할경우 1000 ~ 5000 ms 를 권장하나 아래의 조건들을 고려하여 직접 성능 테스트 후 결정하는게 가장 베스트
    ```java
    + Producer, broker의 성능이 좋아 메시지 처리에 지연이 거의 없다면 길게 설정해도 무방하다.
    + Message 크기가 작다면 짧게 설정해도 무방 --> 왜???
    + Kafka Producer 기능을 단독 어플리케이션으로 구성했다면, 대기시간 증가로 인한 부하에 영향을 받을 다른 기능이 없으므로 대기시간을 길게 설저앟여 전송 안정성 확보
    + 반대로, 기존 어플리케이션에 추가로 구현했다면, Message 전송 지연으로 전체 시스템 장애가 발생할 수 있으므로 대기시간 짧게 설정
    + 실시간 처리가 중요한 데이터라면 대기시간 짧게 설정
    + 실시간 보다 안정성이 중요하다면 대기시간 길게 설정
    ```
    * default : 60000(ms)

### Sender
* batch.size
    * kafka 는 메시지 send 호출시, 바로 전송하지 않고 Accumulator 버퍼에 쌓아뒀다가 일정 크기만큼 쌓이면 한번에 묶어서 전송하는 기능을 제공한다. 이때, 묶어서 전송할 메시지 bulk 크기 설정값이 batch.size
        * 다만, 동일한 파티션에 전송되는 데이터만 묶어서 전송가능 (서로 다른 파티션의 메시지를 묶어서 전송하는 기능은 미지원)
        * [HTTP 의 네이글 알고리즘](https://github.com/JisooOh94/study/blob/master/HTTP%20%EC%99%84%EB%B2%BD%EA%B0%80%EC%9D%B4%EB%93%9C/Content/4.1%20TCP%20%ED%94%84%EB%A1%9C%ED%86%A0%EC%BD%9C%20%EC%84%B1%EB%8A%A5%EC%A7%80%EC%97%B0.md#%EB%84%A4%EC%9D%B4%EA%B8%80-%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98%EA%B3%BC-tcp-no_delay)과 유사
    * 메시지를 바로 전송하지 않고, 묶어서 전송함으로서 네트워크 비용 절약 및 브로커 부하 감소 시켜 전체적인 쓰루풋 증대 (레이턴시 측면에선 다소 손해)
    * 너무 작은 batch.size 는 빈번하게 메시지 전송을 유발하여 과도한 네트워크 비용 및 브로커 부하에 따른 전체적인 throughput 을 감소 시킬 수 있다.
    * 너무 큰 batch.size 는 메모리를 불필요하게 많이 설정해야 한다.
    * 일반적으로 어플리케이션에서 전송하는 메시지 평균 크기에 따라 5개 ~ 10개 정도의 메시지를 묶어서 보낼 수 있는 크기 정도로 설정한다.
    * default : 16384 byte
* linger.ms
    * 마지막 메시지 전송 이후 다음 메시지 전송 trigger 시간
    * linger.ms 시간 이전에 Accumulator 버퍼에 batch.size 만큼 메시지 축적시 linger.ms 무시하고 바로 전송
    * 너무 짧게 설정하여 batch.size 만큼 메시지가 축적되기 전에 메시지 전송이 계속 trigger 된다면 레이턴시는 빨라지나, 네트워크 비용이 많아지므로(tcp 커넥션 생성, rtt 등) 쓰루풋이 떨어져 비효율적이다.
    * 너무 길게 설정하면, bath.size 만큼 메시지가 축적되길 대기하는 시간이 너무 길어지므로 일부 메시지의 producing 이 지연될 수 있다. (쓰루풋 약간 개선되나 레이턴시가 떨어짐)
    * 일반적으로, 레이턴시와 네트워크 비용사이에 적절히 타협하여 10 또는 100 으로 설정
    * default : 0 (batch.size 에 상관없이 Accumulator 버퍼에 메시지가 축적되면 바로 전송)

### Communcating with broker
* acks : 메시지 전송 성공 판단 기준
    * 0
        * 브로커의 ack 응답 여부 상관 없이, 전송 즉시 성공으로 판단
        * 브로커로부터 메시지 프로듀싱 성공 응답을 받기위해 대기하는 시간이 줄어들어 높은 쓰루풋 및 적은 레이턴시를 얻을 수 있으나, 메시지 유실이 많아짐
    * 1
        * 리더 파티션에 메시지 저장 성공시 성공으로 판단. 리더 파티션은 팔로워 파티션들의 메시지 저장 성공 여부 상관없이 ACK 응답 전송
        * 처리량과 내구성 사이에서 적절한 타협을 본, (보편적 용도에서) 가장 합리적인 설정 값
    * all / -1
        * 리더 파티션 및 토픽의 min.insync.replicas 수만큼 팔로워 파티션에 메시지 저장 성공시 성공으로 판단. 리더 파티션은 팔로워 파티션으로부터 ack 응답 수신 후 프로듀서로 ack 응답 전송
        * 높은 내구성 보장하나 쓰르풋 및 레이턴시 많이 떨어짐 
    * default : 1
* retries
    * 메시지 전송 실패시, 재시도 횟수
    * 브로커 서버나 네트워크상의 일시적인 에러 발생시, 이는 금방 복구 될 수 있다. 이런경우 retries 를 설정하여 재시도하다보면 에러가 복구되어 정상 처리할 수 있게된다.
        * 예를 들어 파티션의 리더 리플리카가 순간적인 장애로 사용할 수 없을 경우, 조금만 기다리면 장애 상황이 해소되거나 새로운 리더 리플리카가 선출되어 정상적으로 메시지 전송을 할 수 있게 된다.
    * retry.backoff.ms 설정값으로 재시도간 대기시간을 설정하여 의미없이 많은 요청을 하지 않게 컨트롤 할 수 있다.
        * 일반적으로 retry.backoff.ms 는 운영중에 발생하는 브로커 서버의 장애 복구시간(즉, 장애가 발생했을 때 새로운 리더 리플리카가 선출되는데 걸리는 시간)을 설정
    * retries 횟수를 적게 설정할수록, 메시지 재전송 시도가 줄어드므로 Producer 리소스 및 네트워크 트래픽 비용 감소하여 쓰루풋이 증가하나 메시지 유실율이 높아짐
    * 반대로, retries 횟수를 많이 설정하면, 메시지 유실율은 줄어들지만 그만큼 쓰루풋과 레이턴시가 나빠진다.
    * default : 0 (~ version 2.0), MAX_INT (version 2.1 ~)
        * 2.1 버전 이상부터는 재시도 관련해서 retries 보단 delivery.timeout.ms 로 제어한다. 그래서 retires 의 default 값이 MAX_INT(2147483647) 이다.
* delivery.timeout.ms
    * send() 메서드를 호출하고 브로커로부터 성공 응답(ack)을 받을떄까지 대기하는 시간.
    * 실패 응답 수신시 retries 값에 따라 재시도하는 시간도 포함된 값이며 재시도 횟수를 delivery.timeout.ms 시간 전에 모두 소진하면 에러 응답
    * request.timeout.ms + linger.ms 의 합보다 크거나 같아야함
    * default : 120000 (2m)
* request.timeout.ms
    * producer 가 브로커로 프로듀싱 요청 전송후 응답 대기하는 최대 시간
    * 본 설정값 이내에 브로커로부터 응답이 오지 않으면 producer 는 프로듀싱 요청을 재전송 한다 (retries 횟수가 고갈되었으면 프로듀싱 실패처리한다.)
    * 브로커 설정인 replica.lag.time.max.ms 값보다 크게 설정해야 producer 가 브로커측에서 프로듀싱에 실패했다고 오해하여 프로듀싱 요청을 재전송하는(이럴경우, 동일한 메시지가 중복 프로듀싱된다.) 상황을 방지 할 수 있다.
    * default : 30000 (30 seconds)
* compression.type
  * 메시지 압축 포맷 설정, 설정시 브로커로 메시지 압축하여 전송
  * 메시지 크기가 커 네트워크 대역폭에 부하를 주거나 브로커 저장 공간이 부족할시 적용
  * snappy, gzip, lz4 중 선택할 수 있으며, 데이터 유형에따라 적절한 압축포맷을 선택해야 CPU 리소스 소비 최소화 및 네트워크 트래픽 비용 절약 극대화 가능 
      * snappy : 적당한 압축률에 CPU 부하가 적고 성능이 좋음
      * gzip : CPU 부하가 크고 압축 시간을 많이 소모하지만 압축률이 높음
  * 압축한 메시지는 압축 알고리즘에 대한 메타 정보와 함께 브로커에 저장. 이후 컨슈머가 컨슈밍시 압축 해제
  * default : none

# Producer 설정시 주의사항 
### retires 에 따른 메시지 중복 처리 또는 순서 위배 문제
* max.in.flight.requests.per.connection 값이 2 이상인 경우, 재시도 처리중 메시지가 중복 producing 되거나 순서가 바뀔 수 있음
  * max.in.flight.requests.per.connection : 앞선 메시지의 성공 응답을 수신하지 못해도 바로 추가로 전송할 수 있는 메시지 개수 설정값 (즉, 동시에 전송가능한 메시지 수)
  * max.in.flight.requests.per.connection 이 1 일경우, 첫번째 메시지 전송 후, 전송 선공시에만 두번쨰 메시지를 전송
  * max.in.flight.requests.per.connection 이 2 이상일 경우, 한번에 첫번쨰,두번쨰 메시지 모두 전송
* max.in.flight.requests.per.connection 이 2 이상일때, 
  ![image](https://github.com/JisooOh94/study/assets/48702893/3cce36a6-8e2c-4b80-8643-7b9b4a14a9d3)
  1. producer 에서 첫번쨰, 둣번쨰 메시지를 브로커로 전송한다.
  2. 브로커에선 두 메시지 모두 enqueue 하고(혹은 두번쨰 메시지만 enqueue 한 상태에서 브로커에 이슈발생, max.in.flight.requests.per.connection 값에 따라 동시에 메시지를 전송하기때문에 타이밍상 두번째 메시지가 먼저 도달할 수 있다.) producer 로 ack 를 전송한다.
  3. 이때, 네트워크 이슈등으로 두번쨰 메시지에 대한 ack 만 producer 로 전달되고, 첫번째 메시지에 대한 ack 는 전달되지 못한다.
  4. producer 는 첫번째 메시지의 프로듀싱이 실패했다 판단하고 첫번쨰 메시지 프로듀싱을 재시도한다.
  5. 이때, 2번과정에서 브로커에 첫번째 메시지도 enqueue 되어있었다면 중복 producing 되는것이고, enqueue 되어있지 않았다면 두번째 메시지와 순서가 바뀌어 producing 되는것이다.
* 따라서, retries 가 0이 아닐때 메시지 전송 순서를 보장하려면 max.in.flight.requests.per.connection 을 1로 설정해야한다. 하지만 이럴경우, 동시성이 떨어져 메시지 전송 속도가 느려진다
* kafka 에서는 위와같은 문제를 해결하기 위해 EOS(Exactly once semantics) 를 보장하는 멱등 프로듀서(Idempotent producer) 를 지원한다.

### 멱등 프로듀서(Idempotent producer)
* Kafka 는 기본적으로 최소 한번의 메시지 프로듀싱은 보장 하는 At Least Once Delivery 정책이었다. 
  * 프로듀서에서 메시지가 프로듀싱되었다는 ack를 받지 못할 경우 retry send를 하는 방법으로 최소 1회의 전송을 보장 
* 하지만 0.11 버전부터 정확히 한번만 메시지 전송을 보장(exactly once delivery) 하기 위해 idempotent producer가 추가되었다.
* idempotent producer 동작 방식
  * 프로듀서에서 메시지 프로듀싱시, 메시지 헤더에 producer id, sequence number 를 추가하여 브로커로 전송
    * producer id : 각 프로듀서가 가지는 고유한 세션 id
    * sequence number : 파티션별 메시지에 부여되는 순차적으로 증가하는 sequence number
  * 브로커에선 파티션 - producer id 별로 sequnce number 를 메모리에 저장. 메시지 수신시 메시지의 producer id, sequence number 가지고 이미 프로듀싱된 메시지인지 식별
* 멱등 프로듀서 적용
  * kafka 3.0 부턴 default 로 멱등 프로듀서가 활성화 되어있으나, 2.8 버전 이하에선 직접 설정해주어야 한다. 
  * enable.idempotence : true(enable.idempotence 가 true 일때, 아래의 조건들이 만족되지 않으면 config exception throw)
  * acks : all(-1)
  * max.in.flight.requests.per.connection : 5 이하 (동시성을 최대로 하여 성능을 극대화하기 위해 최대값인 5로 설정하는것이 좋음)
  * retries : MAX_INT(enable.idempotence 를 true 로 설정하면 retries 는 자동으로 MAX_INT 로 설정됨)

> Reference
> * https://ohjongsung.io/2020/01/04/%EC%B9%B4%ED%94%84%EC%B9%B4-%ED%8A%9C%EB%8B%9D-%EB%B0%A9%EC%95%88-%EC%A0%95%EB%A6%AC
> * https://blog.voidmainvoid.net/475
> * https://devidea.tistory.com/90
> * https://devfoxstar.github.io/mq/kafka-producer-max-block-ms/
> * https://d2.naver.com/helloworld/6560422
> * https://velog.io/@xogml951/Apache-Kafka
> * https://velog.io/@xogml951/Kafka%EC%99%80-Exactly-Once
> * https://4betterme.tistory.com/177
> * https://bistros.tistory.com/entry/Kafka-idempotent-producer-%EB%A9%B1%EB%93%B1%EC%84%B1%EC%97%90-%EA%B4%80%ED%95%B4%EC%84%9C
> * https://blog.naver.com/PostView.naver?blogId=fbfbf1&logNo=223101741560&categoryNo=84&parentCategoryNo=37&viewDate=&currentPage=1&postListTopCurrentPage=1&from=postView
> * https://devlog-wjdrbs96.tistory.com/436
> * https://sjparkk-dev1og.tistory.com/207