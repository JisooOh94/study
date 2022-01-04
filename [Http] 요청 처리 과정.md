# HTTP 요청 처리 과정
1. 사용자가 웹 브라우저에 URL 주소 입력

2. DNS 서버에 웹 서버의 호스트 이름을 IP 주소로 변경 요청한다. 

3. DNS 로부터 응답받은 웹서버 IP 주소로 TCP 연결 시도.(3 way Handshake)

4. TCP 커넥션 수립시, 통신 수행

5. 통신 완료 후, TCP 커넥션 연결 해제(4 way Handshake)