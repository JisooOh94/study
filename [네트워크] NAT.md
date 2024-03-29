# NAT
### Overview
* 내부 네트워크에 속해있는 인스턴스들이 하나의 Public IP 를 공유하며 외부 인터넷망과 통신할 수 있도록 해주는 IP 변환 라우터
* IPv4 의 한정된 IP 주소 수로 인해 인스턴스에 할당할 수 있는 공용 IP 수에 제한이 존재 
* 따라서 내부 네트워크에 존재하는 인스턴스들엔 Private IP 를 할당하고, 이들이 외부 인터넷망과 통신할때에만 Public IP 를 가지고있는 NAT 를 통해 통신 수행
* Public IP 의 절약효과 뿐만 아니라, 내부 인스턴스들의 주소를 은닉함으로서 방화벽과 같은 보안향상 효과도 추가

IP 패킷의 TCP/UDP 포트 숫자와 소스 및 목적지의 IP 주소 등을 재기록하면서 라우터를 통해 네트워크 트래픽을 주고 받는 기술이다. 패킷에 변화가 생기기 때문에 IP나 TCP/UDP의 체크섬(checksum)도 다시 계산되어 재기록해야 한다.

<br>

### Architecture
* NAT 라우터는 공용 네트워크와 개인 네트워크 사이에 위치하며 공용 네트워크와 개인 네트워크 간에 IP 데이터그램을 라우팅
* 데이터그램을 라우팅하기 전에 IP주소 변환을 수행하며 데이터그램 헤더의 내부 원본 주소, 원본 포트 번호, 외부 대상 주소, 대상 포트 번호를 변환 및 IP 체크섬 재계산 수행
* 내부 인스턴스들과 통신할때 사용하는 private IP 와, 외부 인터넷망과 통신할때 사용하는 public IP 를 각각 소유

![image](https://user-images.githubusercontent.com/48702893/211192305-ed9261e6-f082-4d66-8e9d-d43e218a72d0.png)

<br>

### Process

![image](https://user-images.githubusercontent.com/48702893/211192322-e279c8e1-7d49-411e-893e-329e85423180.png)

* 공용 네트워크 쪽의 NAT 라우터 인터페이스 IP 주소(Public IP)를 202.151.25.14로, 개인 네트워크 인터페이스쪽의 IP 주소를 192.168.1.254 로 가정

![image](https://user-images.githubusercontent.com/48702893/211192444-cc52474f-4bf8-41d4-8984-9fefcfd78788.png)

1. 클라이언트에서 TCP SYN 메시지를 웹 서버로 보냅니다. 발신자 주소는 192.168.1.15이고 포트 번호는 6732입니다. 대상 주소는 128.15.54.3이고 포트 번호는 80입니다.

2. 클라이언트의 패킷은 NAT 라우터에 의해 개인 네트워크 인터페이스에서 수신됩니다. 아웃바운드 트래픽 규칙이 패킷에 적용됩니다. 즉, 발신자(클라이언트)의 주소는 NAT 라우터의 공용 IP 주소 202.15.25.14로 변환되고 발신자(클라이언트) 원본 포트 번호는 공용 인터페이스의 TCP 포트 번호 2015로 변환됩니다.

3. 그러면 패킷이 인터넷을 통해 전송되고 궁극적으로 대상 호스트인 128.15.54.3에 도달합니다. 수신 쪽에서는 IP 계층 원본 주소와 TCP 계층 포트 번호를 기반으로, 패킷이 202.151.24.14, 포트 번호 2015에서 시작된 것으로 나타납니다. 그림 4에서는 반환 경로에 대한 NAT 프로세스를 보여 줍니다.

![image](https://user-images.githubusercontent.com/48702893/211192450-f5cc940d-1edd-414a-afb0-6ba1ce2951d0.png)

4. 이 시나리오에서 인터넷 호스트 128.15.54.3은 NAT 라우터의 인터넷 주소를 대상으로 하는 응답 패킷을 보냅니다.

5. 패킷이 NAT 라우터에 도달합니다. 이는 인바운드 패킷이므로 바인딩된 변환 규칙이 적용됩니다. 대상 주소가 원래 발신자(클라이언트)의 IP 주소(192.168.1.15, 대상 포트 번호 6732)로 다시 변경됩니다.

6. 그러면 패킷이 내부 네트워크에 연결된 인터페이스를 통해 클라이언트에 전달됩니다.

<br>

### CentOS NAT 라우터 구성


> Reference
> * https://namu.wiki/w/NAT
> * https://learn.microsoft.com/ko-kr/azure/rtos/netx-duo/netx-duo-nat/chapter1