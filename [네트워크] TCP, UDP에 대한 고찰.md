# TCP - UDP 비교

| TCP | UDP |
|:----|:----|
| 연결지향형 프로토콜 | 비연결지향형 프로토콜 |
| 연결이 성공해야 통신 가능 | 연결 없이 통신 가능 |
| 바이트 스트림을 통한 연결 | 메세지 스트림을 통한 연결 |
| 혼잡제어, 흐름제어 순서보장 지원 | 혼잡제어, 흐름제어 순서보장 지원 X |
| 통신 속도 느림 | 통신 속도 빠름 |
| 신뢰성 있는 데이터 전송(재전송 수행) | 데이터 전송 보장 X(재전송 수행하지 않음) |
| 전송단위 : 세그먼트 | 전송단위 : 데이터그램 |
| HTTP, Email 등에 사용 | DNS, 실시간 스트리밍등에 사용 |
| 일대일 통신 | 일대일, 일대다, 다대다 통신 | 

### TCP
* 데이터 송수신의 신뢰성을 보장하는 연결형 프로토콜 (커넥션이 수립되어야 데이터 송신 가능)
* 확인 응답 패킷을 수신하지 못했거나 잘못되었을경우 재전송 수행
* 데이터 송수신 신뢰성을 보장하기 위한 여러 부가 기능 수행(흐름제어, 혼잡제어 등)
* 신뢰성과 견고함이 중요한 어플리케이션간 통신시 주로 사용 
* 일대일 통신만 가능

![image](https://user-images.githubusercontent.com/48702893/108603804-61315b00-73ed-11eb-8f2c-be801636801c.png)

### UDP
* 데이터 송수신의 신뢰성을 보장해주지 않는 비연결형 프로토콜 (커넥션이 수립되지 않았어도 일방적으로 데이터 송신)
* 일방적으로 데이터를 송신하므로 데이터의 정상 수신은 보장해주지 않지만 통신 과정이 간단하고 속도가 빠름
* 간단한 데이터를 빠른속도로 전송해야하는 애플리케이션 통신시 주로 사용
* 일대다, 다대다 통신 가능

![image](https://user-images.githubusercontent.com/48702893/108603797-5e366a80-73ed-11eb-83a0-15a743eeb3ca.png)

# UDP
### UDP 도입 배경
* 게임, 동영상 스트리밍 등의 실시간 데이터 전송 속도가 중요한 서비스들에서 TCP 프로토콜을 이용할경우, 신뢰성있는 데이터 전송을 위한 TCP 의 여러가지 장치들로인해 속도가 저하되어 문제 발생
  * 동영상 스트리밍 서비스의 경우, 패킷중 하나가 전송 실패시 재전송을 하는데 그동안 영상은 버퍼링 상태로 대기하여 유저가 제대로 시청할 수 없음
  * TCP 의 혼잡제어로 데이터 전송양도 계속 조절되어 동영상 재생 퀄리티가 안정적이지 못함
* 위의 서비스들은 전송되는 데이터 패킷중 일부가 유실되어도 크게 지장이 없어 TCP 의 신뢰성있는 데이터 전송 기능이 굳이 필요하지 않음
  * 1080*720 영상의 1 frame 의 1 px 정보가 유실되어도 그 영상을 시청하는 사용자는 거의 느끼지 못함
  * 게임 서버의 경우 패킷 유실로 사라진 키 입력등의 정보를 내부적으로 자동으로 복구하여 사용하는 등의 여러가지 메커니즘 존재
* 네트워크 환경이 좋지 않은 지역(e.g. 동남아, 중국등) 에선 패킷 유실이 빈번히 발생하여 계속 재전송이 수행되므로 레이턴시가 길어짐


### UDP 특징
#### 비연결자형
* TCP 와 같이 Handshaking 과정을 통한 논리적인 가상 회선 연결을 수행하지 않음
* connection 연결 없이, 일방적인 데이터 전송만 수행
* 논리적인 가상 연결이 없으므로, 가상 연결을 통한 라우팅 경로도 정해지지 않아 동일한 목적지로 보내는 데이터그램이 각각 개별적인 경로를 가지고 전송됨

#### 신뢰성있는 데이터 전송을 보장 하지 않음
* 신뢰성 없고, 순서화되지 않은 데이터그램 송수신 
  * 확인응답 없음 : 메세지가 제대로 도착했는지 확인하지 않음
  * 순서제어 없음 : 수신된 메세지의 순서를 맞추지 않음 (TCP 헤더와 달리 순서번호 필드 없음)
  * 흐름제어 없음 : 흐름 제어를 위한 피드백을 제공하지 않음
  * 오류제어 거의 없음 : 검사합을 제외한 특별한 오류 검출 및 제어 없음(따라서 응용 계층 등의 UDP를 사용하는 프로그램 쪽에서 오류제어 수행 필요)
* 복잡한 기능 없이 데이터 송수신만 지원하기에 단순한 구조로 빠른 요청과 응답 및 멀티캐스팅 가능

#### 단순한 헤더
* 프로토콜에서 제공하는 기능이 별로 없기에, 헤더에 포함되어야하는 정보가 적어 헤더가 단순함(8 byte)
* 따라서 TCP 와 비교하여 헤더의 처리에 적은 리소스만 사용
* UDP 헤더
  * Source Port (16 bit) : 출발지의 포트 번호
  * Destination Port (16bit) : 목적지 포트번호
  * Length (16 bit) : 헤더와 데이터를 포함한 전체 UDP 메세지의 길이
  * Checksum (16 bit) : 헤더와 데이터의 에러를 확인하고 검출하는 필드
![image](https://user-images.githubusercontent.com/48702893/162378728-8d95e0e1-b3ca-4c5d-ab55-9c958befcd68.png)

#### 단일 소켓 사용
* TCP 에서는 하나의 클라이언트 소켓당 하나의 서버 소켓이 1:1 로 연결되어 통신했으나, UDP 에선 하나의 UDP 소켓이 모든 클라이언트의 데이터그램 수신
* 따라서 서버 어플리케이션이 열 명의 클라이언트에게 동시에 서비스를 제공하려면 TCP의 경우 열개의 소켓이 필요하나, UDP 는 한개로 가능

### UDP 속도 저하 이유
#### 대역폭 제한
* UDP 는 혼잡제어를 하지 않고 네트워크 상태에 상관없이 무분별하게 데이터 전송
* 한 네트워크 회선에서 몇몇 클라이언트가 UDP로 데이터 전송시, 소수에 의해 대역폭이 점유당해 다른 클라이언트들이 통신을 못하는 상황 발생 가능 (Congestion Collapse) 
* 이같은 이유로 네트워크 관리자나 ISP 업체에서 UDP 프로토콜로 통신할 수 있는 대역폭을 강제로 제한해버리거나, 일정량 이상의 데이터 캐리어는 자동으로 차단하는 방식 사용 
> 국내 ISP의 경우 UDP 통신을 별도로 제한을 걸고 있지는 않지만, 학교나 회사와 같은 곳에서는 UDP에 제한을 거는 경우가 많음


###
In some applications TCP is faster (better throughput) than UDP.

This is the case when doing lots of small writes relative to the MTU size. For example, I read an experiment in which a stream of 300 byte packets was being sent over Ethernet (1500 byte MTU) and TCP was 50% faster than UDP.

The reason is because TCP will try and buffer the data and fill a full network segment thus making more efficient use of the available bandwidth.

UDP on the other hand puts the packet on the wire immediately thus congesting the network with lots of small packets.

You probably shouldn't use UDP unless you have a very specific reason for doing so. Especially since you can give TCP the same sort of latency as UDP by disabling the Nagle algorithm (for example if you're transmitting real-time sensor data and you're not worried about congesting the network with lot's of small packets).

#### 비효율적인 라우팅 경로 

사용자A<->사용자B 가 사용자A<->서버<->사용자B보다 중간에 거치는 것이 존재하기 때문에 얼핏 더 빠를 것으로 보입니다만, 프로세싱을 제외한 순수 넷웍으로 보자면 아래의 경로가 더 빠를수 있다는 것이죠.
이런 현상이 발생하는 이유는 사용자A와 사용자B의 경로는 적극적인 PATH관리 대상이 아니기 때문입니다. 통상적으로 저런 네트워크의 priority는 전체 넷 구성에 있어서 우선도가 낮고, 투자 리턴이 거의 없기 때문에 최소한의 밴드를 확보하거나 ISP사이의 다른 망으로 우회하는 경우가 많습니다. 하지만 사용자와 IDC(주로 서버가 있는 곳이 IDC니까)의 트래픽은 사용자의 체감에 직접적인 영향을 끼치기 때문에 네트워크 우선도가 높게 됩니다.
즉, 그림상으로 가깝지만, 실제 사이의 트래픽은 최적 Path를 타지 않을 확률이 대단히 높아지고, 이로인해 P2P의 순수한 퍼포먼스 이득이 실제로는 존재하지 않을 가능성이 상당히 큽니다. (즉, CS방식으로 통신할때 150ms의 레이턴시로 A와 B가 통신하지만 P2P로 하면 경로가 최적화 되지 않기 때문에 180ms의 순수 레이턴시가 발생할수도 있다는 뜻입니다.)
물론 이는 절대적인 이야기는 아닙니며, 한국과 같이 밀도 높은 네트웍에서는 경로가 아무리 꼬여도 실제 밀도가 높기 때문에 비최적화 P2P경로의 레이턴시가 그다지 높지 않을 확률이 더 크긴 합니다. 따라서 이런 인프라적인 사항들이 P2P가 쓸모 있다 없다고 판단하는 가장 상단의 이유로 보기는 어려워지겠죠.

### Conclusion
* 항상 UDP 가 TCP 보다 빠른것이 아님 (오히려 대부분의 경우에 TCP 가 더 빠르거나 성능차이 미미)

TCP UDP benchmark[(Performance Comparison between TCP and UDP Protocols in Different Simulation Scenarios)](https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.sciencepubco.com%2Findex.php%2Fijet%2Farticle%2Fdownload%2F23739%2F11888&psig=AOvVaw1__UicsnWVl3DYyDUdlPxi&ust=1649406418985000&source=images&cd=vfe&ved=0CAsQjhxqFwoTCMCc2u_DgfcCFQAAAAAdAAAAABAD)

![image](https://user-images.githubusercontent.com/48702893/162158746-45a4a023-8f8d-4f64-bf0d-6219410734a1.png)

![image](https://user-images.githubusercontent.com/48702893/162158871-7ba5a04d-4735-4811-9214-7a69a6dcc0ff.png)

![image](https://user-images.githubusercontent.com/48702893/162158939-f001ee1f-f9de-4d56-8730-82bbe68b3436.png)

The results have shown that the performance of TCP is  outperformed the UDP in both of the two scenarios. Therefore, this  paper concluded that the TCP is more reliable and better than UDP  in terms of all the performance measures.
평균 종단 간 지연, 평균 처리량, 패킷 전달 비율 및 패킷 손실률 측면에서 측정, 결과는 TCP의 성능이 두 가지 시나리오 모두에서 UDP를 능가한다는 것을 보여주었다. 따라서, 본 논문은 TCP가 모든 성능 측정 측면에서 UDP보다 더 신뢰할 수 있고 더 우수하다는 결론을 내렸다.

사실 UDP도 얼마든지 신뢰성있는 네트워크를 구축할 수 있다. TCP에서 하는 신뢰성 작업을 프로그래머가 직접 코딩해서 구현하면 된다.
보통 TCP로 쓰면 효율이 떨어져서 쓰기 싫은데, 신뢰성을 보장해줘야 되는 상황일 경우 사용하는 방법이다.
TCP에 쓰이는 흐름 제어 및 신뢰성 제어 알고리즘은 현대에 들어서는 성능 상으로 지나치게 비효율적이라는 평을 받고 있어 개발자들이 UDP 소켓에서 더 효율적인 알고리즘으로 TCP의 기능을 구현하려는 시도가 이어지고 있다.

TCP가 UDP에 비해 느린 이유 2가지만 말해달라고하면 다음 2가지를 들 수 있다.
- 데이터 송수신 이전, 이후에 거치는 연결설정 및 해제과정
- 데이터 송수신 과정에서 거치는 신뢰성보장을 위한 흐름제어
따라서 송수신하는 데이터의 양은 작으면서 잦은 연결이 필요한 경우에는 UDP가 TCP보다 훨씬 효율적이고 빠르게 동작한다.
브로드캐스트나 멀티캐스트, 소량의 데이터 전송 시에는 UDP를 이용하는 것이 효율적입니다.


### Reliable UDP

### QUIC

### HTTP 3.0


***
> Reference
https://bestskp.tistory.com/29
https://cjwoov.tistory.com/5
https://namu.wiki/w/UDP
http://www.gpgstudy.com/forum/viewtopic.php?t=23388
