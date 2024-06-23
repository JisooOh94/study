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

#### Non Buffering
* TCP 는 수신, 발신 버퍼가 있어 데이터 전송시, TCP 버퍼에 축적해두었다가, 버퍼가 가득찼을때 하나의 패킷으로 묶어 전송하므로 패킷 생성 비용 및 전송비용 절약
* UDP 는 버퍼 없이, 데이터 그램을 바로바로 패킷으로 전송하므로 많은 패킷 생성 비용 및 전송 비용 소모되고, 네트워크에 혼잡 유발 가능
* 따라서, 송수신하는 데이터의 크기가 거질수록 효율 저하 발생

### Conclusion
* 항상 UDP 가 TCP 보다 빠른것이 아님 (오히려 대부분의 경우에 TCP 가 더 빠르거나 성능차이 미미)
* 그러나 송수신하는 데이터의 양이 작거나, 잦은 연결이 필요한 경우에는 UDP가 TCP보다 효율적이고 빠르게 동작
* 더해서 한편으론, 현대에 들어서 TCP 에서 사용되는 신뢰성 알고리즘이 성능상으로 지나치게 비효율적이라는 평 또한 존재
* 이에따라, UDP 소켓에서 더 효율적인 알고리즘으로 TCP의 신뢰성 통신 기능을 구현하려는 시도가 이어지고 있음

#### TCP UDP benchmark[(Performance Comparison between TCP and UDP Protocols in Different Simulation Scenarios)](https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.sciencepubco.com%2Findex.php%2Fijet%2Farticle%2Fdownload%2F23739%2F11888&psig=AOvVaw1__UicsnWVl3DYyDUdlPxi&ust=1649406418985000&source=images&cd=vfe&ved=0CAsQjhxqFwoTCMCc2u_DgfcCFQAAAAAdAAAAABAD)
![image](https://user-images.githubusercontent.com/48702893/162158746-45a4a023-8f8d-4f64-bf0d-6219410734a1.png)
![image](https://user-images.githubusercontent.com/48702893/162158871-7ba5a04d-4735-4811-9214-7a69a6dcc0ff.png)
![image](https://user-images.githubusercontent.com/48702893/162158939-f001ee1f-f9de-4d56-8730-82bbe68b3436.png)

* The results have shown that the performance of TCP is  outperformed the UDP in both of the two scenarios. Therefore, this  paper concluded that the TCP is more reliable and better than UDP  in terms of all the performance measures.
> 평균 종단 간 지연, 평균 처리량, 패킷 전달 비율 및 패킷 손실률 측면에서 측정, 결과는 TCP의 성능이 두 가지 시나리오 모두에서 UDP를 능가한다는 것을 보여주었다. 따라서, 본 논문은 TCP가 모든 성능 측정 측면에서 UDP보다 더 신뢰할 수 있고 더 우수하다는 결론을 내렸다.

<br>

# Http 3.0 (Http Over QUIC)
* UDP 기반의 프로토콜인 QUIC을 사용하여 통신하는 프로토콜
* 안전하고, 빠르며, 또한 스트림 기반의 다중화 서비스를 제공
* TCP 를 사용하는 경우에 발생할 수 있는 단점을 해결

```java
사실 HTTP/3는 처음에는 HTTP-over-QUIC이라는 이름을 가지고 있었는데, IETF(Internet Engineering Task Force) 내 HTTP 작업 그룹과 QUIC 작업 그룹의 의장인 마크 노팅엄이 이 프로토콜의 이름을 HTTP/3로 변경할 것을 제안했고, 2018년 11월에 이 제안이 통과되어 HTTP-over-QUIC이라는 이름에서 HTTP/3으로 변경되게 되었다.

즉, HTTP/3는 QUIC이라는 프로토콜 위에서 돌아가는 HTTP인 것이다. QUIC은 Quick UDP Internet Connection의 약자로, 말 그대로 UDP를 사용하여 인터넷 연결을 하는 프로토콜이다.(참고로 발음은 그냥 퀵이라고 한다)

HTTP/3는 QUIC을 사용하고, QUIC은 UDP를 사용하기 때문에 결과적으로 HTTP/3는 UDP를 사용한다 라고 이야기 할 수 있는 것이다.
```

### 배경
* HTTP/2 에 이르면서 많은 성능 개선 및 체감 지연 시간 개선이 이루어짐
* 하지만 HTTP/2 도 결국 TCP 프로토콜 기반이었기때문에, TCP 가 가지고있는 성능이슈(HoLB, 3 way handshake 로 인한 느린 연결설정 시간)률 그대로 상속
  * TCP HoLB : Http1.1 의 Pipeline connenction 으로 인한 HoLB 문제가 아닌, TCP 레벨의 HoLB 문제
* 현대에 들어서 스트리밍 서비스등 초지연 실시간 전송을 요구하는 인터넷 서비스가 늘어나면서 더욱 고속의 통신 프로토콜에 대한 수요 증가

#### TCP HoLB
* HTTP/2가 여러 면에서 HTTP/1.x의 Head-of-Line Blocking 문제를 해결하지만, HTTP/2 자체는 여전히 단일 TCP 연결을 사용
* TCP 프로토콜 자체의 특성상, 패킷 손실이 발생하면 해당 TCP 세그먼트의 재전송이 완료될 때까지 연결의 다른 모든 데이터 전송이 대기
* 즉, 하나의 커넥션의 한 스트림에서 패킷 손실 발생시, 동일 커넥션에서 처리중이던 모든 스트림이 일시중지됨. 이것이 TCP 레벨에서의 Head-of-Line Blocking

### Why UDP?
* TCP 는 신뢰성있는 통신을 보장하기 위한 여러가지 장치가 시대에 걸쳐 추가되어왔음
* 이러한 TCP 의 신뢰성 보장 장치들로 인해 레이턴시가 발생하는데, 이를 수정하여 성능을 개선하려해도, http 표준이고, 커널단까지 내려간느 로우레벨의 로직이기에 함부로 수정하거나 커스터마이징하기에 어렵고 리소스도 많이 소모
  * HTTP 헤더만 봐도 알수 있다. 신뢰성있는 통신을 위한 여러가지 헤더가 이미 추가되어있고, 개발자가 따로 헤더를 추가하려해도, 추가영역인 OPTION 영역은 이미 다른 신뢰성 보장장치의 헤더(e.g MSS(Maximum Segment Size), WSCALE(Window Scale factor), SACK(Selective ACK))로 인해 여유공간이 별로 없다.
* 그에 반해, UDP 는 TCP 가 제공하는 신뢰성있는 통신을 위한 장치같은것 없이 데이터 그램 송수신이라는 심플한 기능만 수행하는 일종의 백지상태의 프로토콜
* 수행하는 기능이 적으므로 레이턴시도 적고 백지상태의 프로토콜이라 수정이나 기능 확장에 용이하다. 즉, 기능 확장을 통해 TCP 에서 제공하는 신뢰성있는 통신 보장 장치들도 더 효율적으로 쉽게 개발하여 적용할수도 있다.

### 1-RTT Hand Shake
* QUIC는 TLS 1.3을 내부적으로 포함하여 메시지 전달에 필요한 인수 교환 및 암호화에 필요한 절차가 동시에 수행
* 이를 통해, 기존 TLS+TCP 에선 TLS 연결을 위한 핸드셰이크와 TCP를 위한 핸드셰이크가 각각 발생했다면(2-RTT), QUIC 에선 이를 한단계로 축소하여 1-RTT 로 hand shake 가능
  * 클라이언트는 Connection ID(QUIC는 연결 식별자로 Connection ID 사용)로 Initail Key를 생성하고 이를 데이터와 함께 전송
  * 그러면 서버에서 클라이언트에 토큰과 서버의 공개 Diffie-Hellman 값을 반환하여 서버와 클라이언트가 Inital Key에 동의
  * 클라이언트와 서버는 최송 세션키 설정과 동시에 즉시 데이터 교환 시작 

![image](https://user-images.githubusercontent.com/48702893/162645708-cb3b9ac7-a547-46f1-9557-f7d91e080060.png)

### 0-RTT Hand Shake
* 한번 서버와 연결했던 클라이언트는 이전 커넥션의 연결정보를 저장해두고, 이후 커넥션 연결시, 데이터 패킷과 함께 서버로 전송하여 0-RTT로 바로 통신 가능  
  * 첫 번째 연결 중에 협상 된 인수, 암호화 키를 계산하는데 사용되는 서버의 Diffie-Hellman 값등\
* 하지만 0-RTT 는 순방향 기밀성을 제공해주지 않아 멱등성이 없는 요청일경우, 보안이슈 발생 가능
  * 첫번째 연결에 사용된 비밀 파라미터 값 노출시, 재시작으로 만들어진 새 연결의 0-RTT 단계에서 보내는 어플리케이션 데이터의 보안도 유지할 수 없음
  * 0-RTT 로 보내는 어플리케이션 데이터는 공격자가 데이터를 해독할 수는 없지만, 중간에 탈취하여 동일 서버에 몇번이고 다시 보내질 수 있음
  * 이같은 이슈로, HTTP 3.0 을 제공하는 CDN 업체(e.g. cloudflare)들은 0-RTT 재연결 기능을 비활성화 해두거나, 멱등성이 없는 0-RTT 요청 거부

![image](https://user-images.githubusercontent.com/48702893/162646895-93adefa8-639e-4985-b01d-748710c46334.png)

### 보안 내장
* QUIC 프로토콜은 TLS 1.3 보안 기능을 포함하여 설계되어 기본적으로 인증, 암호화등의 보안 기능 자체적으로 제공
  * 기존에는 일반적으로 TLS와 같은 더 높은 계층에서 제공 
* 이를 통해 연결이 항상 암호화 및 인증되는 것뿐만 아니라 Hand Shake 과정도 축소되어 더 빠르게 연결할 수 있음

### 멀티 스트리밍
* HTTP 2 + TCP 프로토콜은 하나의 TCP 커넥션에서 여러 HTTP 요청을 처리하기때문에, 두 엔드포인트 사이 네트워크에서 한 요청의 패킷이 유실된다면, 유실된 패킷을 다시 전송하고 목적지를 찾는 동안 동일한 커넥션에서 처리되고있던 다른 모든 요청의 패킷전송도 중단되는 HoLB 문제 존재

![image](https://user-images.githubusercontent.com/48702893/162612513-4768b984-41ab-4e85-a6d9-9733eab3d965.png)

* QUIC 에선 두 가지 다른 스트림을 설정했을 때 이들을 독립적으로 다루므로 스트림 중 하나에서 패킷이 유실되더라도 해당 스트림만 멈추고 재전송하므로, 다른 스트림엔 영향 없고 혼잡상황이 전파되지 않음

![image](https://user-images.githubusercontent.com/48702893/162612542-78598add-2aa6-4f57-84f9-89d9e7b26eb8.png)

### Connection Migration
* QUIC 는 4-tuple(Source IP, Source Port, Destination IP, Destination Port)이 아닌 Connection ID로 연결을 식별하기떄문에, IP 주소 혹은 포트 번호가 변경되는 경우에도 지속적으로 연결 유지 가능
* 이를 통해, 커넥션이 달라지거나 종단의 정보가 달라져도 세션 유지 가능하여 끊김없이 네트워크 이용 가능 
  * e.g 1) 이동통신 환경에서 QUIC을 사용하여 파일을 다운로드 중, 클라이언트 단말이 wifi가 지원되는 곳으로 이동하여 wifi 네트워크로 변경되어도, 끊김 없이 wifi 네트워크를 통해 남은 파일 계속 다운로드 가능
  * e.g 2) NAT(Network Address Translator)를 사용하는 네트워크 이용시, NAT 에서 클라이언트 IP 주소 및 포트번호 변경시에도, 클라이언트는 Connection ID 기능을 활용하여 서버와 지속적인 연결 유지 가능  

### SACK 기반 오류 제어
* QUIC 는 별도의 ACK 프레임을 통해 오류 검출 및 복구를 수행하는 ARQ(Automatic Repeat Request) 방식 이용
* SACK (Selective ACK)를 이용하며, ACK 프레임의 Largest Acknowledged 필드로 수신한 패킷중 가장 큰 packet number 를, ACK Range 필드로 정상 수신한 모든 packet number 를 표현
* 기존의 packet number 를 통한 손실감지 기법과 달리, time-out 기반으로 손실 감지
  * 아직 수신확인이 되지 않은 패킷의 전송시간과, 가장 최근에 수신확인한 패킷의 수신 시간차이가 임계값을 넘어가면 손실로 간주 
  * 가장 최근에 전송한 패킷이 손실되는 경우에는, 패킷 전송시 설정한 Probe Time-out 시간이 지났을때  Probe 패킷을 전송하여, 그 수신 시간과 비교 수행

#### SACK
* 기존 ACK 프레임을 통한 손실 패킷 검출 방식의 경우, 손실된 packet 이후의 이미 전송되었던 모든 패킷까지 중복 재전송이 수행되는 비효율 존재
* 이런 비효율적인 구조를 극복하기 위해서 selective acknowlegment TCP option 등장
  * SACK는 손실된 패킷으로 인해 ACK 재전송시, 손실패킷 이후의 정상 수신한 패킷에 대한 정보도 추가해서 전송
  * 이를 이용하여 정상 수신된 패킷의 중복 재전송 없이, 누락된 패킷만 재전송 수행
```
AS-IS
1 : Segment #2가 손실되었다.
2 : Client는 Segment #3을 받았다. 하지만 Segment #2가 누락된 것을 알게되고 Server에 Segment #1의 ACK를 2번 보내어 Segment #2의 손실을 Server쪽에 알린다.
3: 아직 Server쪽에서는 Client로 부터 중복된 Segment #1의 ACK를 못받은 상태인지라 계속 다음 Segment인 Segment #4를 전송한다. 하지만 Client는 아직 Segment #2를 못받았으므로 다시 Segment #1의 ACK를 전송한다.
4: Server가 Client로 부터 중복된 Segment #1의 ACK를 받았다.이에 Segment #2부터 이후 #3,#4에 대해 다시 재전송한다. 두번째로 전달된 중복된 Segment #1의 ACK는 무시된다. (Segment #2 만 손실되었음에도 #3, #4 까지 중복 재전송 수행)
5: Client가 나머지 Segment를 전송받고, ACK도 정상적으로 전송한다.

TO-BE
1: Segment #2가 손실되었다.
2: Client는 segment #1과 #3사이가 누락되었음을 확인하고, segment #1에 대한 duplicated ack을 전송한다.(단, 이전과 다르게 해당 duplicated ack에 segment #3은 전송받았다고 SACK option을 통해 표시한다.)
3: Server가 아직 duplicated ACK을 받기 전이라 segment 4를 Client로 전송하게 되는데 Client는 segment #3을 받았을 때와 마찬가지로 segment #1에 대한 duplicated ack을 보낼 때 segment #3, #4를 받았다고 표시하고 ack을 전송한다.
4: 이후 server는 segment #1에 대한 duplicated ACK을 전달받고, segment #3이 포함된 SACK를 받게 되는데 이를 통해 segment #2가 누락된 것을 확인하고 segment #2를 재전송하게 된다. 이후 한번 더 전송받은 segment #1에 대한 dupplicated ACK(SACK #3, #4) 는 내용상 더 전송할게 없으므로 추가 동작은 없다.
5: Client는 segment #2를 전송받게 되고, 모든 segment 를 전송받았다는 의미로 segment #4에 대한 ACK를 server로 전송한다.
```

### 우려사항
* TLS 1.3은 여전히 TCP 위에서 독립적으로 실행할 수 있지만 QUIC는 TLS 1.3을 내장하기때문에, TLS 없이 QUIC를 사용할 방법이 없음
* QUIC는 TCP 에선 암호화하지 않던 헤더필드까지 모두 암호화, 이로인해 네트워크 중개자가 암호화된 헤더필드를 읽을 수 없게되는 문제 발생
* QUIC 는 UDP 프로토콜을 사용하므로, 기존의 UDP 프로토콜이 가지고있던 문제들 모두 상속(e.g. 증폭공격 취약, 인터넷 제공업체의 트래픽 제한 및 차단)

### [Benchmark](https://requestmetrics.com/web-performance/http3-is-fast)

![image](https://user-images.githubusercontent.com/48702893/163102658-8e343bb9-4553-4b57-b0fd-59ca832d5ebc.png)

### 도입상황
* Safari : 2020년 10월에 macOS Big Sur를 사용 중인 Mac을 한정적으로 정식적으로 제공
* Firefox : 2021년 4월의 정식 버전 88 버전부터 활성화
* Chrome : Chrome Canary 버전 79 이상에서 지원
* Cloudflare : 2019년 9월부터 일반 사용자에게 제공
* Nginx : 1.19 릴리즈를 목표로 개발 중

<br>

그럼 아예 새로운 프로토콜을 만들면 되지 않을까?
가능은 하지만 현실적 문제가 존재한다. 실제 인터넷 트래픽은 거의 TCP와 UDP가 차지하고 있다. 가령 HTTP는 TCP 위에서 동작하고 DNS는 UDP나 TCP위에서 동작한다. TCP나 UDP가 아니면 아예 통과하지 못하는 라우터나 방화벽들도 존재한다. 그리고 방화벽 설정을 할 때 TCP나 UDP 트래픽이 아니면 모두 막도록 설정하는 경우도 있다. 또한 가정용 라우터와 같이 NAT환경을 기본적으로 만드는 경우 TCP나 UDP가 아니라면 제대로 주소 변환이 안 될 수도 있다. 만약 새로운 프로토콜을 만들었다 하더라도 실제 인터넷상에서 사용할 수 있는지 보장이 안된다.
QUIC은 수송 계층 프로토콜이지만 UDP 위에서 동작하도록 설계되어 SCTP와는 달리 배포 및 보급이 용이하다. (고착화 문제 없음 (SCTP가 가지고있던...))


### QPACK
HPACK을 QUIC에 맞게 수정한 것을 QPACK이라 볼 수 있다.
HPACK의 동적 테이블은 인코더(HTTP 요청이나 응답을 보낸 쪽)와 디코더(받는 쪽)에서 항상 동일해야 한다. 그렇지 않으면 디코더가 받은 내용을 해석할 수 없을 것이다.
TCP를 통한 HTTP/2의 경우 이 전송 계층(TCP)이 HTTP 요청과 응답의 순서를 보장해주기 때문에 동기화 하는 것이 명확하다. 동적 테이블이 업데이트 되어야 할 경우 인코더가 요청이나 응답을 보낼 때 같이 담아서 보내면 된다. 하지만 QUIC에서는 더 복잡 해진다.
QUIC는 여러 개의 HTTP 요청이나 응답을 서로 다른 스트림으로 보낼 수 있기 때문에, 각 스트림에서의 순서는 보장되지만 여러 개의 스트림들 사이의 순서는 보장되지 않는다.
예를 들어 만약 클라이언트가 QUIC 스트림 A를 사용해 A라는 HTTP 요청을 보내고, 스트림 B에 B라는 요청을 보낸다면 네트워크에서 패킷 재정렬이나 손실에 의해 B라는 요청이 서버에 먼저 도달할 수 있게 된다. 만약 요청 B가 요청 A에 담겨있는 헤더를 사용해 인코딩이 되어 있다면 요청 A가 도달하기 전까지는 읽을 수 없을 것이다.
gQUIC 프로토콜은 모든 HTTP 요청과 응답의 헤더를(바디는 말고) 같은 gQUIC 흐름으로 직렬화 하여 해결했다. 이렇게 하면 헤더의 도착 순서는 항상 보장된다. 이는 기존의 HTTP/2 코드 대부분을 재사용할 수 있게 해주는 아주 간단한 방법이다. 하지만 QUIC이 줄이고자 했던 head-of-line 블로킹을 증가시키게 된다. IETF QUIC 제작자들은 HTTP와 QUIC 사이의 새로운 맵핑(HTTP/QUIC)과 더불어 새로운 압축 전략을 만들어 QPACK이라 이름 붙였다.

### 반사회피 방어[[cloudflare]](https://blog.cloudflare.com/the-road-to-quic/)
UDP 기반 프로토콜의 공통적인 약점은 반사 공격에 취약하다는 것이다.
공격자가 서버 하나를 속여서 대량의 데이터를 희생자인 제 3자에게 보내게 만드는 것이다.
공격자는 패킷의 발신지의 IP 주소를 변경해 대상 서버에게 보내 마치 희생자가 보낸 패킷처럼 위장한다.
이런 종류의 공격은 보통 서버에서 보내는 응답이 받은 요청보다 더 크기 때문에 매우 효율적
TCP는 이런 종류의 공격에 사용되지 않는데, 핸드쉐이크 동안 전송되는 초기 패킷(SYN, SYN+ACK, …)의 길이가 같기 때문에 증폭이 일어날 가능성이 없기 때문이다.
반면에 QUIC의 핸드쉐이크는 매우 비대칭적이다. TLS와 마찬가지로, 클라이언트가 몇 바이트(QUIC 패킷 안에 담긴 TLS ClientHello 메시지)만 보내도 서버는 보통 인증 정보를 담은 매우 큰 데이터를 돌려준다.

클라이언트가 보낸 최초의 QUIC 패킷은 심지어 실제 내용이 훨씬 작더라도 특정 크기까지 채워져서 와야 한다.

발신지-주소를 명시적으로 확인하기 위해 확인 매커니즘을 사용한다. 서버가 긴 응답을 보내는 대신 고유의 암호화 토큰을 담은 훨씬 작은 재시도 패킷을 보내면 클라이언트는 그 정보를 포함한 새로운 패킷을 서버에 보낸다.
패킷이 한번이 아니라 두 번 왕복해야 하기 때문에 초기 핸드쉐이크 시간이 느려지는 단점이 있다.

서버의 응답을 반사 공격이 효과적이지 않게 될 때까지 줄이는 것이 있다. 예를 들어 일반적으로 RSA 보다 훨씬 작은 ECDSA 인증서를 사용하는 방법이 있다.

***
> Reference
* https://bestskp.tistory.com/29
* https://cjwoov.tistory.com/5
* https://namu.wiki/w/UDP
* http://www.gpgstudy.com/forum/viewtopic.php?t=23388
* https://velog.io/@wsong0101/QUIC%EC%9D%84-%ED%96%A5%ED%95%9C-%EC%97%AC%EC%A0%95%EB%B2%88%EC%97%AD
* https://technote.kr/31
* https://blog.cloudflare.com/ko-kr/even-faster-connection-establishment-with-quic-0-rtt-resumption-ko-kr/
* https://evan-moon.github.io/2019/10/08/what-is-http3/
