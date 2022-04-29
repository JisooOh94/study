# 대역폭 튜닝
### receiver window size
#### 배경
* 네트워크 대역폭을 높히기 위해선 Round Trip Time 을 줄이거나 receiver window size 증대
> window size : 수신자의 가용한 TCP 수신 버퍼 크기, 송신자는 해당 크기를 넘지 않도록 데이터 전송함으로서 흐름제어 수행
* Round Trip Time 은 종단간 물리적 거리에 영향을 많이 받으므로 유의미하게 줄이기가 어렵다. 따라서 receiver window size를 늘림으로서 대역폭 확장
* receiver window size 를 늘릴경우 Bandwidth Delay Product 도 함께 증가하나, 현대 네트워크장비의 스펙이 좋아짐에 따라 크게 영향받지 않음
> Bandwidth Delay Product : 출발지 장비에서 목적지 장비까지의 네트워크 경로상에 전달중인 데이터 패킷의 양, 대역폭 * RTT

#### 설정
1. TCP 세그먼트 헤더의 window size 필드 설정
* TCP 세그먼트 헤더의 window size 크기를 늘림으로서, 송신자가 더 많은 양의 데이터를 한번에 전송하도록 수정(default : 64KB)
* 세그먼트 헤더의 window size 필드는 16bit 로서 최대 65535(2^16) 의 값만 표현가능
* 이는 현대와 같은 대용량 고속 통신환경에선 너무 작은 크기이므로, 주로 세그먼트 헤더 option 필드에 WSCALE 값 추가하여 window size 크기 확장
  * receiver window size = windown size * 2^WSCALE
  * e.g. window size : 8192, WSCALE : 8, receiver window size : 8,192 * (2^8) = 2097152 byte

2. net.ipv4.tcp_window_scaling 수정
* WSCALE 을 통해 window size 를 한계치 이상으로 변경하기 위해선 송신지 장비, 수신지 장비 모두 커널 파라미터 'net.ipv4.tcp_window_scaling' 값이 '1'로 설정되어 있어야함
* sysctl 명령어를 통해 net.ipv4.tcp_window_scaling 값 수정
```shell
$ sysctl -w net.ipv4.tcp_window_scaling="1"
```

### socket buffer size
#### 배경
* receiver window size 를 증가하여도, 실제 커널의 소켓 버퍼 크기가 더 작다면 무의미
* 따라서, 소켓 버퍼 크기 관련 커널 파라미터를 수정하여 소켓 버퍼 크기 증대

#### 설정
* 소켓 버퍼 크기 관련 커널 파라미터
  * net.core.rmem_default, net.core.wmem_default : socket read, write buffer default 크기
  * net.core.rmem_max, net.core.wmem_max : socket read, wrtie buffer 최대 크기
  * net.ipv4.tcp_rmem, net.ipv4.tcp_wmem : tcp 소켓 read, write buffer 크기, space 로 구분하여 min, default, max 크기 설정
  * net.ipv4.tcp_mem : 소켓에 버퍼로 할당할 수 있는 전체 메모리 크기
* net.ipv4.tcp_rmem, net.ipv4.tcp_wmem 이 설정되어있을경우, tcp 프로토콜 소켓 버퍼에 한하여 net.core.rmem 값보다 우선 적용됨
* 기본값은 리눅스 커널이 자동으로 최적값으로 설정하나, 장비 메모리의 여유가 있을경우, 소켓 버퍼 크기를 늘려줌으로서 receive window size 증대에 따른 성능 개선 효과 증대
  > 단 net.ipv4.tcp_mem 는 수정하지 않는것이 안전 
* 기본적으로 default 값(주로 128 kb)이 소켓 버퍼 크기로 설정되며, 
```shell
$ sysctl -w net.core.rmem_default="253952"
$ sysctl -w net.core.wmem_default="253952"
$ sysctl -w net.core.rmem_max="16777216"
$ sysctl -w net.core.wmem_max="16777216"
$ sysctl -w net.ipv4.tcp_rmem="253952 253952 16777216"
$ sysctl -w net.ipv4.tcp_wmem="253952 253952 16777216"
```

### congestion window size
#### 배경
* 수신자가 receive window size를 늘려도, 송신자가 혼잡제어로 인해 receive window size만큼 데이터를 전송할 수 없음 
* 네트워크 장비는 congestion avoidance algorithm 을 통해 receiver 의 상태와는 상관없이  몇가지 파라미터(RTT...)를 참조하여 congestion window size 설정
  * 최초 커넥션 수립시, 패킷 크기를 initial congestion window size(CWND) 부터 시작하여 점차적으로 증가 
* congestion window size 가 receive window size 보다 작을경우, 대역폭 증대 효과 적음

#### 수정
* initial congestion window size를 증가시킴으로서, 더 빠르게 congestion window size가 receive window size 보다 커지도록 수정
* 커널 파라미터가 아닌, ip route 명령어로 직접 수정
```shell
$ ip route show
192.168.1.0/24 dev eth0  proto kernel  scope link  src 192.168.1.100  metric 1
169.254.0.0/16 dev eth0  scope link  metric 1000
default via 192.168.1.1 dev eth0  proto static

$ ip route change default via 192.168.1.1 dev eth0  proto static initcwnd 10

$ ip route show
192.168.1.0/24 dev eth0  proto kernel  scope link  src 192.168.1.100  metric 1
169.254.0.0/16 dev eth0  scope link  metric 1000
default via 192.168.1.1 dev eth0  proto static  initcwnd 10
```

<br>

# 네트워크 capacity 튜닝
### maximum file count
#### 배경
* 일반적인 유닉스 운영체제에서 소켓은 파일처럼 관리 및 사용
* 전체 시스템에서 가질 수 있는 파일 개수의 제한이 존재한다면, 소켓 생성개수도 함께 제한
* 따라서 많은 수의 소켓을 사용하는 어플리케이션의 경우, 프로세스별 파일 개수 제한 값 수정 필요

#### 수정
* ulimit 명령어를 통해 프로세스별 파일 개수 제한 설정인 user limit 커널 파라미터 값 수정 
```java
$ ulimit -a | grep 'open files'
open files                      (-n) 1024

$ ulimit -SHn 65535

$ ulimit -a | grep 'open files'
open files                      (-n) 65535
```

### backlogs
#### 배경
* CPU 코어는 NIC 로부터 패킷 수신시, ring buffer 라는 backlog queue 에 버퍼링
* backlog queue 가 full 일경우, 수신한 패킷을 drop 하므로, 메모리 공간의 여유가 있다면 backlog queue 의 크기를 최대값으로 설정하는것이 안전

#### 수정
* sysctl 명령어를 통해 net.core.netdev_max_backlog 값 수정
```java
$ sysctl -w net.core.netdev_max_backlog="30000"
```

### listen backlog
#### 배경
* connection 수립 요청 수신시, 우선 connection 요청 대기열(listen backlog queue)에 저장, 대기열의 앞에서부터 순차적으로 connection 수립 처리
* listen backlog queue 가 full 일경우, 수신 요청한 client 에게 error 를 응답하게 되므로, listen systemcall 파라미터로 전달하는 backlog queue size 값을 최대값으로 설정하는것이 안전
* backlog queue size 파라미터값을 최대로 설정하여도, listen backlog queue 크기 제한 커널 파라미터인 net.core.somaxconn 값이 작다면, 제대로 적용되지 않음

#### 수정
* sysctl 명령어를 통해 net.core.somaxconn 값 수정
```shell
$ sysctl -w net.core.somaxconn="1024"
```

'net.ipv4.tcp_max_syn_backlog'라는 listen backlog와 연관된 커널 파라미터가 있습니다. 'net.core.somaxconn'이 accept()을 기다리는 ESTABLISHED 상태의 소켓(즉, connection completed)을 위한 queue라면, 'net.ipv4.tcp_max_syn_backlog'는 SYN_RECEIVED 상태의 소켓(즉, connection incompleted)을 위한 queue입니다.
이 설정값도 아래와 같이 적당히 증가 시킵니다.
$ sysctl -w net.ipv4.tcp_max_syn_backlog="1024"

### port range
#### 배경
* 클라이언트에서 서버로 연결을 맺을때, 특별히 bind() 시스템 콜로 출발지 포트를 지정(bind)하지 않는다면, 커널은 임의의 포트(ephemeral port)를 할당 
* 포트는 유한한 자원이기에 한 시스템에서 동시에 가질 수 있는 클라이언트 소켓의 수는 한정적
* proxy 서버의 경우 클라이언트로 부터 요청을 받아 다른 백엔드 서버에 전달하는데, 이 경우 proxy 서버는 다른 백엔드 서버에 연결하기 위한 클라이언트 소켓 필요
* 따라서 proxy 서버는 서버측에서 클라이언트 소켓에 할당할 포트 개수 설정을 통해 동시에 생성할 수 있는 클라이언트 소켓 개수 조정  

#### 수정
* sysctl 명령어를 통해 net.ipv4.ip_local_port_range 값 수정하여 클라이언트 소켓에 할당할 수 있는 포트 범위 설정
```shell
$ sysctl -w net.ipv4.ip_local_port_range="1024 65535"
```
* 소켓 gracefully shutdown, CLOSE_WAIT, TIME_WAIT 등으로 인해 실제 ESTABLISHED 된 클라이언트 소켓 수는 보편적으로 net.ipv4.ip_local_port_range 로 설정한 포트 개수보다 적음 