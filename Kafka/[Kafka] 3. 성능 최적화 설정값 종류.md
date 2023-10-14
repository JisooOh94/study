# Kafka 성능 목표
* Throughput : 초당 처리할 수 있는 메시지 개수 증대
* Latency : 하나의 메시지 처리시간 단축
* Durability : 내구성, 메시지 유실 최소화
* Availability : 가용성, 서버 장애 방지 및 복구 성능

> Throughput vs Latency , Durability vs Availability

<br>

# 성능 관련 설정값
### Producer
* batch.size 
    * kafka 는 메시지 send 호출시, 바로 전송하지 않고 Accumulator 버퍼에 쌓아뒀다가 일정 크기만큼 쌓이면 한번에 묶어서 전송하는 기능을 제공한다. 이때, 묶어서 전송할 메시지 bulk 크기 설정값이 batch.size
      * 다만, 동일한 파티션에 전송되는 데이터만 묶어서 전송가능 (서로 다른 파티션의 메시지를 묶어서 전송하는 기능은 미지원)
      * [HTTP 의 네이글 알고리즘](https://github.com/JisooOh94/study/blob/master/HTTP%20%EC%99%84%EB%B2%BD%EA%B0%80%EC%9D%B4%EB%93%9C/Content/4.1%20TCP%20%ED%94%84%EB%A1%9C%ED%86%A0%EC%BD%9C%20%EC%84%B1%EB%8A%A5%EC%A7%80%EC%97%B0.md#%EB%84%A4%EC%9D%B4%EA%B8%80-%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98%EA%B3%BC-tcp-no_delay)과 유사
    * 메시지를 바로 전송하지 않고, 묶어서 전송함으로서 네트워크 비용 절약 및 브로커 부하 감소 가능 
    * 너무 작은 batch.size 는 빈번하게 메시지 전송을 유발하여 과도한 네트워크 비용 및 브로커 부하에 따른 전체적인 throughput 을 감소 시킬 수 있다.
    * 너무 큰 batch.size 는 메모리를 불필요하게 많이 설정해야 한다.
    * 일반적으로 어플리케이션에서 전송하는 메시지 평균 크기에 따라 5개 ~ 10개 정도의 메시지를 묶어서 보낼 수 있는 크기 정도로 설정한다. 
    * default : 16384 byte
* linger.ms
    * 마지막 메시지 전송 이후 다음 메시지 전송 trigger 시간 
    * linger.ms 시간 이전에 Accumulator 버퍼에 batch.size 만큼 메시지 축적시 linger.ms 무시하고 바로 전송
    * 너무 짧게 설정하여 batch.size 만큼 메시지가 축적되기 전에 메시지 전송이 계속 trigger 된다면, 네트워크 비용이 많아지므로(tcp 커넥션 생성, rtt 등) 비효율적이다.
    * 너무 길게 설정하면, bath.size 만큼 메시지가 축적되길 대기하는 시간이 너무 길어지므로 일부 메시지의 producing 이 지연될 수 있다.
    * 일반적으로, 지연과 네트워크 비용사이에 적절히 타협하여 10 또는 100 으로 설정
    * default : 0
* compression.type
    * 메시지 압축 포맷 설정. none, gzip, snappy 등
    * default : none
* acks : 메시지 전송 성공 판단 기준
    * default : 1
    * 0
        * 브로커의 ack 응답 여부 상관 없이, 전송 즉시 성공으로 판단
        * 높은 처리량을 얻을 수 있으나, 메시지 유실이 많아짐
    * 1
        * 리더 파티션에 메시지 저장 성공시 성공으로 판단. 리더 파티션은 팔로워 파티션들의 메시지 저장 성공 여부 상관없이 ACK 응답 전송
        * 처리량과 내구성 사이에서 적절한 타협을 본, (보편적 용도에서) 가장 합리적인 설정 값
    * all / -1
        * 리더 파티션 및 팔로워 파티션(ISR) 모두에 메시지 저장 성공시 성공으로 판단. 리더 파티션은 팔로워 파티션으로부터 ack 응답 수신 후 ack 응답 전송
        * 높은 내구성 보장하나 처리량 많이 떨어짐 
* retries
    * 메시지 전송 실패시, 재시도 횟수
    * default : 0
* max.in.flight.requests.per.connection : 한번에 전송할 메시지 bulk 개수. 2개 이상일 경우, 메시지 전송 순서 바뀔수있음 
* buffer.memory
	* Accumulator 버퍼 크기
	* 33554432 byte

### Broker
* num.replica.fetchers : 리더 파티션의 메시지를 팔로워 파티션으로 전송하는 백그라운드 스레드 개수
* min.insync.replicas : acks=all 인 메시지 전송 요청에 대해 ack 응답을 보내기 위해 필요한, 메시지 복사에 성공한 팔로워 파티션 최소 개수 
* unclean.leader.election.enable : ISR 파티션뿐만 아니라 OSR 파티션도 리더 파티션으로 선출될 수 있도록 설정
* broker.rack
	* 카프카 클러스터의 broker 서버들이 각기 서로 다른 rack(zone)에 떠있도록 설정
	* 가용성 증대, but 메시지 복제시 NW 부하 증가
* log.flush.interval.messages / log.flush.interval.ms
	* broker 는 producer 로부터 전송된 메시지를 메모리 버퍼(페이지 캐시)에 일정시간(log.flush.interval.ms) 저장했다가 디스크로 이동
	* 디스크로 이동시킬 메시지의 최소 메시지 크기 제한 및 마지막 디스크 이동 작업이후 최소 경과 시간
	* 값이 클수록 disk IO 적게 발생하여 처리량이 증가하나, 메시지 유실확률이 커짐 
* num.recovery.threads.per.data.dir
	* 신규 브로커가 클러스터에 추가되었을때, 다른 브로커와 sync 를 맞추기 위해 각 브로커의 로그 파일을 스캔하는 쓰레드 개수
	* 스레드 개수가 많을수록, 여러 로그 파일을 동시에 스캔 가능하므로 신규 브로커 구동 속도가 빨라짐
 
### Consumer
* fetch.min.bytes
	* 브로커로부터 한번에 가져올 메시지 bulk 크기
	* Producer 의 batch.size 와 동일
	* 네트워크 트래픽 및 브로커 부하 감소
	* default : 1 byte
* fetch.max.wait.ms
	* 마지막 메시지 컨슈밍 이후 다음 메시지 컨슈밍 trigger 시간 
	* fetch.max.wait.ms 시간 이전에 파티션에 fetch.min.bytes 만큼 메시지 축적시 fetch.max.wait.ms 무시하고 바로 컨슈밍
	* Producer 의 linger.ms 와 동일
	* default : 500ms 
* auto.commit.enable
	* 파티션에 오프셋 정보가 없는경우(에러로 인한 유실 or 처음 생성된 파티션 등) 처리 방법
	* earliest : 최초의 오프셋 값으로 설정
	* latest : 가장 마지막 오프셋 값으로 설정
	* none : 예외 throw
* session.timeout.ms 
	* 컨슈머와 컨슈머 그룹 사이의 세션 타임 아웃 시간
	* 컨슈머는 heartbeat.interval.ms 시간 간격으로 컨슈머 그룹 코디네이터에세 하트비트 api 전송
	* 컨슈머가 session.timeout.ms 시간 내에 주기적으로 하트비트 api 를 컨슈머 그룹으로 날리지 않으면, 컨슈머 그룹은 컨슈머에 장애가 발생한것으로 인지하고 리밸런싱 수행
	* 값이 작을수록 더 빠르게 컨슈머의 장애를 감지할 수 있다는 장점이 있으나, 실제 장애가 아닌, 일시적인 지연까지 장애로 감지하여 빈번한 리밸런싱을 유발하는 단점 존재 
	
***
> Reference
> * https://soft.plusblog.co.kr/14
> * https://stackoverflow.com/questions/49649241/apache-kafka-batch-size-vs-buffer-memory
> * https://firststep-de.tistory.com/43
> * https://ohjongsung.io/2020/01/04/%EC%B9%B4%ED%94%84%EC%B9%B4-%ED%8A%9C%EB%8B%9D-%EB%B0%A9%EC%95%88-%EC%A0%95%EB%A6%AC
> * https://blog.voidmainvoid.net/475
> * https://devidea.tistory.com/90