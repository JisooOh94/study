# Zookeeper
* 분산 애플리케이션들의 관리를 도와주는 서비스로서 클라이언트(kafka)의 관리를 대신해주고 개발자로 하여금 비즈니스 로직에만 집중할 수 있게 해줌
* 정보들은 Zookeeper의 지노드(znode)라 불리는 곳에 키-값 형태로 저장됨
* znode 는 메모리 영역에 존재한다. 따라서, 데이터가 메모리에 저장되므로 높은 처리량과 낮은 대기 시간을 갖음
* 브로커는 zookeeper 를 통해 메세지 저장 및 관리 작업을 위해 필요한 공유정보 조회

### 역할
* 컨트롤러 브로커 선출 및 컨트롤러 브로커의 h/c 수행
  * 컨트롤러 브로커 h/c 실패시, 새로운 컨트롤러 브로커 선출
* 토픽 정보 저장 (파티션 개수, replicas 위치등)
* 파티션 offset 저장

### 구축
* Zookeeper 에서는 데이터(offset) 읽기/쓰기 연산이 빈번히 발생한다.
* 따라서 Zookeeper 는 별도의 장비에 zookeeper 만 단독으로 구축하는것이 좋다.

### Zookeeper 앙상블

![image](https://github.com/JisooOh94/study/assets/48702893/ebfaafb4-f317-47c8-a5a7-6ebbdec4a163)

* zookeeper 도 여러대의 zookeeper 서버를 묶은 클러스터(a.k.a ensemable) 로 구성하여 고가용성을 확보할 수 있다.
* ensemble 로 구성할 경우, 여러대의 zookeeper 노드중 한대가 자동으로 leader 노드로 선출되며, leader 를 제외한 follower 노드들은 leader 로부터 데이터를 동기화받는다.
* leader 노드에 장애가 발생하게되면, 다른 노드가 다시 leader로 선출되어 서비스를 이어나감으로서 고가용성을 확보한다. 
* 하지만, 살아있는 zookeeper 노드수가 전체 노드의 과반수를 넘지못하면 zookeeper 는 majority 를 확보하지 못하여 더이상 서비스를 지속하지 않고 중단한다. (Quorum 알고리즘)
* 따라서 zookeeper 노드수는 majority 관점에서 홀수개수로 설정하는것이 더 좋다.
    * 노드수가 5개일 경우, 2개의 노드가 죽어도 살아있는 노드가 전체노드의 과반을 넘으므르 zookeeper 는 서비스를 계속 이어나간다.
    * 반면에 노드수가 6개일경우에도, 2개의 노드가 죽었을때까지만 zookeeper 서비스를 이어나갈 수 있다. 3개의 노드가 죽는다면 살아있는 노드가 과반을 넘지못하므로 서비스가 중단된다.
    * 따라서 홀수개수로 구성했을떄 짝수개수로 구성했을떄보다 더 적은 서버수로 동일한 가용성을 보여주므로 홀수개수로 구성하는것이 효율적이다.

### swapping 억제
* Zookeeper 의 데이터는 znode 라 불리는 메모리 영역에 저장된다. 이때, 이 메모리 영역 크기를 충분한 크기로 설정하지 않으면 운영중 zookeeper 의 메모리 공간이 부족하게되고, swapping 이 발생하게된다.
* swapping 이 발생하게되면, zookeeper 의 데이터 읽기/쓰기 성능을 크게 저하시키고 그에따라 처리량이 중요한 kafka 클러스터의 성능까지 저하시키게 된다.
* Zookeeper 는 jvm 위에서 동작하므로, swapping 이 발생되지 않도록 충분한 크기로 jvm heap size 를 설정해주는것이 좋다. (적절한 heap size 는 성능테스트를 통해 도출)
* 물리메모리에 여유공간이 있다고 해서 swapping 이 발생하지 않는것은 아니다. vm.swappiness 커널 파라미터값에 따라 swap 메모리 사용률이 결정되므로 이 값을 낮은 값으로 설정해주는것이 좋다. 
  * 0(스왑미사용) ~ 100(스왑 적극적 사용) 으로 설정할 수 있으며, swapping 최소화 목적으론 1로 설정하는것이 좋다.
  * 0으로 설정시, 1로 설정했을 때보다 훨씬 더 많은 page cache를 해제하게 됨. 거의 한자리 수까지 털어 버리기 때문에 좀 더 안정적인 성능의 시스템을 원한다면 1로 세팅해서 사용하는 것이 좋음 (page cache를 지나치게 버리면 I/O가 높아지고 시스템의 load를 상승시킬 수 있다)

### zookeepers.properties
```java
# the directory where the snapshot is stored.
# 멀티 서버 설정시 각 서버의 dataDir 밑에 myid 파일이 있어야함
dataDir=/tmp/zookeeper

# the port at which the clients will connect
clientPort=2181

# 하나의 클라이언트에서 동시 접속하는 개수 제한, 기본값은 60이며, 0은 무제한
maxClientCnxns=0

# Disable the adminserver by default to avoid port conflicts.
# Set the port to something non-conflicting if choosing to enable this
# port 충돌을 방지하려면 admin server 비활성화(false)
admin.enableServer=false
# admin.serverPort=8080

# 멀티 서버 설정
# 앙상블을 이루는 서버 정보
# server.X=hostname:peerPort:leaderPort
# peerPort : 앙상블 서버들이 상호 통신하는 데 사용되는 포트
# leaderPort : 리더를 선출하는데 사용되는 포트
server.1=localhost:2888:3888
# server.2=server_host_1:2888:3888
# server.3=server_host_2:2888:3888

# 리더 서버에 연결해서 동기화하는 시간
#initLimit=5
 
# 리더 서버를 제외한 노드 서버가 리더와 동기화하는 시간
#syncLimit=2
```

> Reference
> * https://brewagebear.github.io/fundamental-os-page-cache/
> * https://kwonnam.pe.kr/wiki/linux/ubuntu/hibernation
> * https://dysong.tistory.com/28#google_vignette
> * https://strange-developer.tistory.com/40
> * https://yeon-kr.tistory.com/184
> * https://zookeeper.apache.org/doc/r3.3.3/zookeeperAdmin.html
> * https://velog.io/@bbkyoo/Apache-Kafka-%ED%81%B4%EB%9F%AC%EC%8A%A4%ED%84%B0-%EA%B5%AC%EC%B6%95%ED%95%98%EA%B8%B0
> * https://velog.io/@hyun6ik/Apache-Kafka-Broker-Zookeeper