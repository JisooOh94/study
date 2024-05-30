# Producer 설정

![image](https://user-images.githubusercontent.com/48702893/153410414-f6ceec63-e151-4da7-b8ce-9bda37657bf0.png)

### Accumulator
* buffer.memory
    * 메시지를 축적하는 Accumulator 버퍼 크기
    * 너무 작게 설정하면, 버퍼가 가득 차있는 상황이 자주 발생한다. 메시지 produce 요청시 버퍼가 가득 차있으면 max.block.ms 만큼 대기한후 exception 이 발생
    * 너무 크게 설정하면, 버퍼가 가득차는 상황을 방지함으로서 메시지가 blocking 되어 지연되는 일이 없어져 쓰루풋이나 레이턴시가 개선되나 잉여 메모리가 많아져 리소스를 낭비하게 된다.
    * 어플리케이션에서 전송하는 메시지 평균 크기, batch.size 값을 기반으로 설정한다.
    * default : 33554432 byte
* max.block.ms
    * 메세지 프로듀싱시, 버퍼가 가득차있거나 메타 데이터(브로커에 메시지전송을 위해 필요한 정보. e.g. 파티션 정보) 응답을 기다리는등의 대기작업이 필요할때 max.block.ms 값으로 설정된 시간동안 대기. max.block.ms 만큼 대기했는데도 프로듀싱시 불가능할때, TimeoutExceptiopn throw
    * 너무 짧게 설정한다면, TimeoutExceptiopn throw 로 메시지 유실이 많아짐. 순간적인 네트워크 지연이나 브로커단 딜레이로도 메시지 유실됨
    * 너무 길게 설정한다면, 대기하는 스레드가 많아져 시스템에 부하가 가중되고 전체적인 어플리케이션 응답시간이 지연됨
    * 일반적으로, 어플리케이션 응답 속도가 중요할경우 100 ~ 500ms, 안정성이 중요할경우 1000 ~ 5000 ms 를 권장하나 아래의 조건들을 고려하여 직접 성능 테스트 후 결정하는게 가장 베스트
      * 실시간 처리가 중요한 데이터라면 대기시간 짧게 설정
        * 빠른 실패와 회복 유도
        * 기존 어플리케이션에 추가로 구현했다면, Message 전송 지연으로 전체 시스템 장애가 발생할 수 있으므로 대기시간 짧게 설정 
      * 실시간 보다 안정성이 중요하다면 대기시간 길게 설정
        * 일시적인 네트워크 지연이나 브로커의 지연으로 메타데이터 요청 응답이 느려져도 대기시간이 길기때문에 TimeoutException 발생 x
        * 버퍼가 가득 차있어도, 대기시간이 길기때문에 TimeoutException 발생 x
        * Kafka Producer 기능을 단독 어플리케이션으로 구성했다면, 대기시간 증가로 인한 부하에 영향을 받을 다른 기능이 없으므로 대기시간을 길게 설정하여 전송 안정성 확보
    * default : 60000(ms)

### Sender
* batch.size
    * kafka 는 메시지 send 호출시, 바로 전송하지 않고 Accumulator 버퍼에 쌓아뒀다가 일정 크기만큼 쌓이면 한번에 묶어서 전송하는 기능을 제공한다. 이때, 묶어서 전송할 메시지 bulk 크기 설정값이 batch.size
        * 다만, 동일한 파티션에 전송되는 데이터만 묶어서 전송가능 (서로 다른 파티션의 메시지를 묶어서 전송하는 기능은 미지원)
        * [HTTP 의 네이글 알고리즘](https://github.com/JisooOh94/study/blob/master/HTTP%20%EC%99%84%EB%B2%BD%EA%B0%80%EC%9D%B4%EB%93%9C/Content/4.1%20TCP%20%ED%94%84%EB%A1%9C%ED%86%A0%EC%BD%9C%20%EC%84%B1%EB%8A%A5%EC%A7%80%EC%97%B0.md#%EB%84%A4%EC%9D%B4%EA%B8%80-%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98%EA%B3%BC-tcp-no_delay)과 유사
    * 메시지를 바로 전송하지 않고, 묶어서 전송함으로서 네트워크 비용 절약 및 브로커 부하 감소 시켜 전체적인 쓰루풋 증대 (레이턴시 측면에선 다소 손해)
    * 너무 작은 batch.size 는 빈번하게 메시지 전송을 유발하여 과도한 네트워크 비용 및 브로커 부하에 따른 전체적인 throughput 을 감소 시킬 수 있다.
    * 너무 큰 batch.size 는 메모리를 불필요하게 많이 설정해야 하고, latency 가 증가하며, 한번에 많은 데이터를 네트워크로 전송해야하므로 네트워크 병목을 유발할 수 있다.
    * default : 16384 byte (2.8.10 버젼 기준)
* linger.ms
    * 마지막 메시지 전송 이후 다음 메시지 전송 trigger 시간
    * linger.ms 시간 이전에 Accumulator 버퍼에 batch.size 만큼 메시지 축적시 linger.ms 무시하고 바로 전송
    * 너무 짧게 설정하여 batch.size 만큼 메시지가 축적되기 전에 메시지 전송이 계속 trigger 된다면 레이턴시는 빨라지나, 네트워크 비용이 많아지므로 쓰루풋이 떨어져 비효율적이다.
    * 너무 길게 설정하면, bath.size 만큼 메시지가 축적되길 대기하는 시간이 너무 길어지므로 일부 메시지의 producing 이 지연될 수 있다. (쓰루풋 약간 개선되나 레이턴시가 떨어짐)
    * 일반적으로, 레이턴시와 네트워크 비용사이에 적절히 타협하여 10 또는 100 으로 설정
    * default : 0 (batch.size 에 상관없이 Accumulator 버퍼에 메시지가 축적되면 바로 전송)

> 여기에서 말하는 네트워크 비용은 다음과 같다.
> 1. 네트워크 요청 수 감소
> 각 프로듀싱 요청은 헤더와 기타 메타데이터를 포함하고 있음. batch.size가 클 경우, 더 많은 메시지를 하나의 요청에 묶어 보내어 전체적인 요청 수가 줄어드므로 각 요청에 따른 고정 오버헤드를 감소시킴
> 2. 네트워크 대역폭 사용률 최대화 
> 큰 배치를 전송하면 큰 데이터 블록을 한 번에 전송하므로 네트워크 대역폭을 최대까지 사용하여 빠르게 전송 가능. (하지만 네트워크 대역폭보다 배치 크기가 더 커진다면, 오히려 대역폭제한으로 인한 병목이 발생할 수 있음)
> 3. 브로커 처리 효율성 
> 브로커는 메시지를 디스크에 저장. 큰 배치로 데이터를 전송하면 브로커는 한번의 디스크 I/O 로 더 많은 메시지를 저장할 수 있으므로 디스크 I/O 감소
> 4. 리소스 사용 최적화
> 네트워크 요청 수가 감소하면 서버가 처리해야 할 네트워크 인터럽트와 컨텍스트 스위칭이 줄어들어 서버 리소스 사용이 최적화


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
        * 예를 들어 토픽의 리더 파티션이 순간적인 장애로 사용할 수 없을 경우, 조금만 기다리면 장애 상황이 해소되거나 새로운 리더 파티션이 선출되어 정상적으로 메시지 전송을 할 수 있게 된다.
    * retry.backoff.ms 설정값으로 재시도간 대기시간을 설정하여 의미없이 많은 요청을 하지 않게 컨트롤 할 수 있다.
        * 일반적으로 retry.backoff.ms 는 운영중에 발생하는 브로커 서버의 장애 복구시간(즉, 장애가 발생했을 때 새로운 리더 파티션이 선출되는데 걸리는 시간)을 설정
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
    * "acks=all" 일경우, 브로커 설정인 replica.lag.time.max.ms 값보다 크게 설정해야 producer 가 브로커측에서 프로듀싱에 실패했다고 오해하여 프로듀싱 요청을 재전송하는(이럴경우, 동일한 메시지가 중복 프로듀싱된다.) 상황을 방지 할 수 있다.
      * replica.lag.time.max.ms : 리플리카 파티션이 리더 파티션과 동기화되는 데 허용하는 최대 시간. 시간 초과시 해당 레플리카는 ISR 목록에서 제거
      * replica.lag.time.max.ms 설정이 ISR 리플리카가 리더와 동기화되는 데 걸리는 시간을 결정하므로, acks=all 일때 request.timeout.ms가 이보다 짧으면, 리플리카 동기화 중 발생할 수 있는 일시적인 지연에 대해 Producer가 불필요하게 request timeout 으로 판단
        * acks=all 설정에서 프로듀서는 모든 ISR이 메시지를 성공적으로 복제해야 프로듀싱 성공으로 판단.
    * default : 30000 (30 seconds)
* compression.type
  * 메시지 압축 포맷 설정, 설정시 브로커로 메시지 압축하여 전송
    * 압축한 메시지는 압축 알고리즘에 대한 메타 정보와 함께 브로커에 저장. 이후 컨슈머가 컨슈밍시 압축 해제
    * default : none
  * 메시지 압축이 필요한 경우
    1. 대규모 메시지 전송
       * 메시지의 크기가 크거나 대량의 메시지를 전송하는 경우, 메시지를 압축하여 데이터의 양 축소 가능. 이를통해 네트워크 대역폭 절약 및 데이터 전송 시간 단축  
    2. 네트워크 병목현상 완화
       * 메시지 압축을 통해 더 적은 네트워크 자원을 사용하여 네트워크 병목현상 완화
    3. 브로커 디스크 사용량 축소
       메시지 압축을 통해 브로커 디스크에 저장되는 메시지 크기 축소 가능
    4. 브로커와 컨슈머의 부하 감소
       브로커는 더 적은 양의 데이터를 디스크에 쓰고 읽음으로서 부하 감소. 컨슈머는 비록 압축 해제로 인해 다소 CPU 사용량은 증가할 수 있으나, 더 적은 네트워크 통신 비용이 소모되므로 더 효율적
  * 압축 방식 종류
  
  | 압축 방식 | 압축률 | 압축/해제 속도 | 설명 | Use case |
  |:--:|:--:|:--:|:--:|:--:|
  | snappy | 낮음 | 매우 빠름 | cpu 리소스 적게 사용 | 빠른 압축 및 해제 속도가 필요한 실시간 스트리밍 또는 로그 데이터 처리 |
  | lz4 | 낮음 ~ 중간 | 매우 빠름 | cpu 리소스 적게 사용 | snappy 처럼 빠른 압축 및 해제 속도가 요구되지만 좀 더 나은 압축률이 필요할때 |
  | gzip | 높음 | 중간 | cpu 사용량 높음 | 네트워크 대역폭이 작거나 저장공간 비용이 클때 |
  | zstd | 매우 높음 | 매우 빠름 | Kafka 2.1.0 부터 지원하는 가장 최근에 나온 압축방식. <br> gzip 보다 압축률이 높으면서 압축 및 해제 속도도 빠름 | Kafka 버전이 2.1.0 이상인 경우 |

# Producer 설정시 주의사항 
### retires 에 따른 메시지 중복 처리 또는 순서 위배 문제
* max.in.flight.requests.per.connection 값이 2 이상인 경우, 재시도 처리중 메시지가 중복 producing 되거나 순서가 바뀔 수 있음
  * max.in.flight.requests.per.connection : 한 토픽 프로듀서에서 동시에 전송가능한 메시지 수(앞선 메시지의 성공 응답을 수신하지 못해도 바로 추가로 전송할 수 있는 메시지 개수 설정값)
  * max.in.flight.requests.per.connection 이 1 일경우, 첫번째 메시지 전송 후, 전송 선공시에만 두번쨰 메시지를 전송
  * max.in.flight.requests.per.connection 이 2 이상일 경우, 한번에 첫번쨰,두번쨰 메시지 모두 전송
* max.in.flight.requests.per.connection 이 2 이상일때, 
  ![image](https://github.com/JisooOh94/study/assets/48702893/3cce36a6-8e2c-4b80-8643-7b9b4a14a9d3)
  1. producer 에서 첫번쨰, 두번쨰 메시지를 브로커로 전송한다.
  2. 브로커에선 두 메시지 모두 enqueue 하고 producer 로 ack 를 전송한다.
  3. 이때, 네트워크 이슈 등으로 두번쨰 메시지에 대한 ack 만 producer 로 전달되고, 첫번째 메시지에 대한 ack 는 전달되지 못한다.
  4. producer 는 첫번째 메시지의 프로듀싱이 실패했다 판단하고 첫번쨰 메시지 프로듀싱을 재시도한다.
  5. 이때, 2번과정에서 브로커에 첫번째 메시지도 enqueue 되어있었다면 중복 producing 되는것이고, enqueue 되어있지 않았다면(두번쨰 메시지만 enqueue 한 상태에서 브로커에 이슈발생, max.in.flight.requests.per.connection 값에 따라 동시에 메시지를 전송하기 때문에 타이밍상 두번째 메시지가 먼저 도달할 수 있다.) 두번째 메시지와 순서가 바뀌어 producing 되는것이다.
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
  * enable.idempotence : true
  * acks
    * all 권장 (데이터 내구성과 일관성 최대화)
  * max.in.flight.requests.per.connection
    * 5 이하의 값 : 메시지 처리 순서 보장되나 동시성 처리가 다소 떨어짐
    * 5 이상의 값 : 더 많은 양의 메시지를 동시에 전송할 수 있으나 메시지 처리 순서가 보장되지 않음
    * 메시지 처리 순서도 보장되면서 동시성도 최대로 할 수 있는 5로 설정하는것이 안전
    * 너무 높은 값을 설정한다면, 동시에 전송되는 많은 양의 메시지로 인해 네트워크 및 브로커 부하 증가
  * retries
    * idempotent producer 로 인해 중복 프로듀싱이 방지되므로 데이터 일관성과 내구성 최대화 하기 위해 MAX_INT로 설정하는것 권장
    * retries 와 함께 retry.backoff.ms 값도 적절히 설정 필요

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
