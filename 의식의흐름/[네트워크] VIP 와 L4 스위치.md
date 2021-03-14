# VIP (Virtual IP)
* 여러대의 서버들의 IP 주소를 대표하는 대표 IP
* 주로 스위치나 라우터의 IP 주소(공인 IP)가 VIP 가 되며, 웹서버가 아니면서 클라이언트에게 웹서버 행세를 하여 가상 IP 라 불림
* 클라이언트는 DNS 에서 알려준 VIP 로 요청을 전송하고, 요청을 수신한 라우터나 스위치에서 요청을 처리할 서버 IP 주소(사설 IP)로 요청 전달
* 주로 방화벽의 이중화나 로드밸런싱에 사용
	* 방화벽의 이중화를 통한 폴트 톨러런트 향상
	![image](https://user-images.githubusercontent.com/48702893/110963541-86363f80-8395-11eb-8425-cc1b3306c731.png)
	
	* L4 스위치를 이용한 로드밸런싱을 통해 부하 분산
	![image](https://user-images.githubusercontent.com/48702893/110963594-99e1a600-8395-11eb-8d0f-5e457d55107c.png)
	 
* 부하 분산 및 폴트 톨러런트 향상


# NAT(Network Address Translation)
* 클라이언트가 공인IP 로 요청전송시, 요청을 수신한 스위치/라우터에서 해당 요청을 처리할 서버의 사설 IP 로 변환 하는것 [[참고 : DHCP]](https://github.com/JisooOh94/study/blob/master/%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC/2.%20TCP%20IP.md#dhcp)
	* 패킷 헤더의 목적지 IP 주소 수정 및 체크섬 값 동기화
* 사설 IP 를 사용함으로서 공인IP 부족 문제를 완화 하고, 내부 네트워크의 보안 강화(외부로 사설 IP 노출 안함으로서 내부 서버로서의 직접적인 공격 차단)

![image](https://user-images.githubusercontent.com/48702893/111074357-eb209f80-8525-11eb-814e-5cec0467f499.png)

# L4 스위치
* VIP(공인IP + Port번호) 를 통한 로드밸런싱에 사용되는 네트워크 장비
* 클라이언트가 VIP 로 요청 전송시, 연결되어있는 서버 pool 의 한 서버에게 요청 전송
* 이때, 스위치에서 VIP 에 명시되어있는 Port 번호(OSI 4계층의 정보)로 요청 전송할 pool 을 구분하기때문에 L4 스위치 로 명명
* 하나의 L4 스위치에는 클라이언트의 요청을 수신하는 여러 가상 서버들(Port 번호가 다른 각각의 VIP 소유)이 존재하고, 각 가상 서버는 요청 처리를 위임할 서버 Pool 정보(사설 IP 주소)를 가지고 있음