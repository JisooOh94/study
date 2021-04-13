![image](https://user-images.githubusercontent.com/48702893/114559714-fbec3e80-9ca6-11eb-97ea-08b1b61f0cf5.png)

<br>

# L4 로드밸런싱
* 4계층(TCP/UDP Port) 기반 로드밸런싱
* L4 스위치가 클라이언트 요청 수신후, 로브밸런싱 알고리즘에 따라 요청처리서버를 선택하여 요청 전송
* 처리 서버들의 네트워크 상태만을 확인하여 로드밸런싱하므로 속도가 빠름 
* 클라이언트 요청 패킷의 목적지 IP 주소 및 MAC 주소를 처리 서버 주소로 변조(NAT)하여 처리서버로 전송
* 클라이언트로부터 전송되는 3 way handshake 패킷을 처리 서버로 전달하여 클라이언트 - 처리 서버간 커넥션 수립

### 로드밸런싱 알고리즘
* Round Robin
	* 요청이 들어온 순서대로 각 서버에 균등하게 분배
	* 가장 단순하면서 가장 빠름
* Least Connection
	* 서버에 연결되어있는 Connection 수를 기반으로 선택
* Weighted Least Connection
	* 서버에 연결되어있는 Connection 수와 각 서버에 부여된 가중치 값을 기반으로 선택
* Fastest Response Time
	* 응답시간이 가장 빠른 서버 선택

<br>

# L7 로드밸런싱
* 7계층(어플레이케이션) 기반 로드밸런싱
* L7 스위치가 요청 패킷의 내용(HTTP 헤더, 쿠기, 요청 url 등)을 분석하여 요청처리서버 선택
* 요청 내용 정보를 사용하므로 L4 로드밸런시에 비해 더 섬세한 로드밸런싱 가능
* 패킷분석하며 로드밸런싱 뿐만 아니라 바이러스, DDOS 공격등도 검출 및 차단 가능
* Client - L7 스위치, L7 스위치 - 처리 서버가 각각 개별적으로 커넥션 생성
 
### L7 로드밸런싱 예시
* 리버스 프록시를 이용한 요청 url 기반 Nginx 로드밸런서
```
http{
	# default loadbalancing algo is round robin
	upstream allbackend {
		server 127.0.0.1:2222;
		server 127.0.0.1:3333;
		server 127.0.0.1:4444;
		server 127.0.0.1:5555;
	}

	upstream app1backend{
	    server 127.0.0.1:2222;
	    server 127.0.0.1:3333;
	}
	upstream app2backend{
	    server 127.0.0.1:4444;
	    server 127.0.0.1:5555;
  	}

	server{
   		listen 80;
		
		location /{
			proxy_pass http://allbackend/;
		}
		location /app1{
			proxy_pass http://app1backend/;
		}

		location /app2{
		    proxy_pass http://app2backend/;
		}	

		location /admin{
			return 403;
		}
	}
}
```