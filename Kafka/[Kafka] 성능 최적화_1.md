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
	* 브로커로 묶어서 전송할 메시지 bulk 크기
	* 네트워크 트래픽 및 브로커 부하 감소
	* default : 16384 byte
* linger.ms
	* 마지막 메시지 전송 이후 다음 메시지 전송 trigger 시간 
	* linger.ms 시간 이전에 Accumulator 버퍼에 batch.size 만큼 메시지가 축적시 linger.ms 무시하고 바로 전송
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
		* 처리량과 내구성 사이의 적절한 타협
	* all / -1
		* 리더 파티션 및 팔로워 파티션(ISR) 모두에 메시지 저장 성공시 성공으로 판단. 리더 파티션은 팔로워 파티션으로부터 ack 응답 수신 후 ack 응답 전송
		* 높은 내구성 보장하나 처리량이 떨어짐 
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