# Throughput
### Producer
* Partition 수 증대
	* Partition 수를 증가시켜, 더 많은 메시지를 분산 처리 가능
* Batch.size, linger.ms 크게
	* 한번에 토픽으로 보내는 메시지 벌크 크기를 늘려 전송 횟수를 줄임으로서 네트워크 트래픽 비용 절약 및 브로커의 부하 감소
* compression.type 적용
	* 메시지 전송시, 압축하여 전송함으로서 네트워크 트래픽 비용 절약 but 압축작업으로 인한 cpu 리소스 사용 증가
	* 다양한 압축 포맷을 지원하나, 데이터 유형에따라 적절한 압축포맷을 선택해야 CPU 리소스 소비 최소화 및 네트워크 트래픽 비용 절약 극대화 가능
* acks 낮게
	* acks 를 낮게 설정할수록, 브로커로부터 메시지 프로듀싱 성공 응답을 받기위해 대기하는 시간이 줄어들어 처리량 증가 but 메시지 유실도 증가 
* retries 적게
	* retries 횟수를 적게 설정할수록, 메시지 재전송 시도가 줄어드므로 Producer 리소스 및 네트워크 트래픽 비용 감소
* buffer.memory 크게
	* buffer.memory 를 크게 설정하여 accumulator 버퍼가 가득차는 상황을 방지함으로서 메시지가 blocking 되어 대기하는 지연 방지
	* 파티션 리소스에 여유가 있을때에만 buffer.memory 증가를 통한 처리량 증대 효과 볼 수 있음 

### Consumer
* fetch.min.bytes, fetch.max.wait.ms 크게
	* 한번에 컨슈밍해가는 메시지 벌크 크기를 늘려 전송 횟수를 줄임으로서 네트워크 트래픽 비용 절약 및 브로커의 부하 감소
* Consumer 수 증대
	* 컨슈머 그룹 내의 컨슈머 수를 토픽의 파티션 수와 맞춰주어 컨슈머에서 하나의 파티션 메시지 처리만 전담  

<br>

# Latency
### Producer
* linger.ms 짧게
	* 짧게 설정할수록 메시지 전송 요청 즉시 (지연없이) 토픽으로 바로 프로듀싱 수행
* compression.type 적용
	* 압축을 통해 페이로드 크기를 줄임으로서 네트워크 전송시간 단축
* acks=1 로
	* 브로커에서 리더 파티션에만 저장 성공시 바로 응답 전송
	* 팔로워 파티션으로의 복제 대기로 인한 성공 응답 지연 없음

### Broker
* parition 개수 적게
	* 파티션의 개수가 적을수록, 메시지 복제 과정으로 인한 end to end 지연 감소
	* 카프카 클러스터의 브로커 수가 많을경우, 파티션 개수가 많아도 end to end 지연 적음
* Broker 당 partition 수 적게
	* 하나의 브로커에서 담당하는 파티션 수를 줄여, 복제해야하는 메시지 수를 줄임으로서 메시지 복제 속도 향상, 지연 단축
* num.replica.fetchers 수 크게
	* 브로커의 리더파티션 메시지 복제 작업을 병렬로 처리함으로서, 메시지 복제 작업으로 인한 지연 단축   

### Consumer
* fetch.min.bytes 작게
	* 작게 설정할 수록, 메시지를 프로듀싱 되는대로 바로 컨슈밍하므로 대기로 인한 지연 없음

<br>

# Durability
### Producer
* acks = all 로 설정
	* 모든 replica 에 복제 성공을 보장하므로 메시지 유실 방지
* min.insync.replicas 크게
	* 메시지 복제본 수가 많아지므로 메시지 유실률 저하 
* retries 횟수 많이
	* 메시지 실패에 따른 메시지 유실 방지
* max.in.flight.requests.per.connection 1로 설정
	* 한번에 하나의 메시지만 전송

### Broker
* replication.factor 높게
	* 메시지 복제본이 많아질수록, 메시지 유실 저하 
* default.replication.factor false 로 설정
	* replication.factor 파라미터를 지정하지 않았을 경우, 자동으로 설정되는 replication.factor
	* 운영상의 안정성을 위해 false 설정을 권장 
* broker.rack 활성화
	* 하나의 rack에 장애가 발생해도 다른 rack 의 브로커가 있으므로 fault tolerant 향상 but 메시지 복제시 네트워크 트래픽 비용 증가 
* unclean.leader.election.enable 비활성화
	* ISR 만 리더 파티션 되게 함으로서 메시지 유실 방지
* log.flush.interval.ms / log.flush.interval.messages 작게 설정
	* 빈번하게 메시지를 디스크에 저장함으로서 유실 방지

### Consumer
* enable.auto.commit 비활성화
	* 활성화시, 메시지 중복처리 버그 발생 가능[참고](https://blog.voidmainvoid.net/262)

<br>

# Availability
### Broker
* unclean.leader.election.enable 활성화
	* 가용한 ISR 파티션이 없을때, 장애상황에 빠지는것이 아닌 OSR 파티션을 리더로 대신 선출하여 동작
* num.recovery.threads.per.data.dir 많이
	* 브로커 구동시 로그 파일 스캔을 여러 스레드가 병렬로 수행하므로 구동속도가 빨라지고 장애 복구 시간이 단축됨

### Consumer
* session.timeout.ms 짧게 설정
	* 컨슈머의 장애를 브로커가 빠르게 감지하여 장애 처리 수행