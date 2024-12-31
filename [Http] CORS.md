# CORS(Cross-Origin Resource Sharing)
* 웹 브라우저에서 실행되는 웹 애플리케이션이 다른 도메인, 프로토콜 또는 포트로 리소스를 요청할 수 있도록 허용하는 보안 기능
* 동일 출처 정책(Same-Origin Policy)을 완화하기 위한 메커니즘으로, 동일 출처 정책은 악의적인 웹 페이지가 사용자의 세션 정보를 훔치거나 민감한 데이터를 전송하는 것을 방지

### CORS 동작 과정
1. 사용자가 웹브라우저를 통해 http://weather.com 로 요청 전송
2. weather.com 도메인에 bind 되어있던 A 서버는 html 웹페이지 응답, 브라우저는 응답받은 html 페이지를 렌더링하여 사용자에게 보여줌
3. 사용자가 웹페이지에서 '날씨보기' 버튼을 누름. 해당 버튼은 javascript 를 통해 기상청 서버로부터 오늘 날씨 정보를 가져와 보여주는 버튼임
   * 전체 페이지를 새로 고침하지 않고도 서버와 데이터 송수신
4. 버튼을 누른 순간 브라우저는 javascript 를 실행하여 koreaWeather.go.kr 로 요청 전송
5. 이때, 웹페이지 출처(weather.com) 와 리소스를 요청한 출처(koreaWeather.go.kr) 가 다르기때문에 CORS 정책이 적용됨
6. koreaWeather.go.kr 에서 날씨 정보 응답 전송시, 응답헤더로 `Access-Control-Allow-Origin: https://example.com` 를 보내주어야함. 그렇지 않을시 브라우저에서 보안상의 이유로 응답 차단. 

### CORS 헤더
* Access-Control-Allow-Origin: 요청을 허용할 출처 지정. '*' 는 모든 출처를 허용
* Access-Control-Allow-Methods: 허용할 요청 메서드 지정
* Access-Control-Allow-Headers: 허용할 요청 헤더 지정
* Access-Control-Allow-Credentials: 자격 증명이 포함된 요청을 허용할지 여부를 지정

<br>

# Preflight 요청
* CORS(Cross-Origin Resource Sharing) 정책의 일부로, 브라우저가 복잡한 요청을 보내기 전에 서버가 해당 요청을 허용하는지 확인하기 위한 메커니즘
* 보안을 강화하고 불필요한 요청을 줄이기 위한 목적

### Preflight 요청이 필요한 경우
1. 사용하는 HTTP 메서드가 GET, POST, HEAD가 아닌 경우(PUT, DELETE, PATCH 등의 메서드를 사용할 때)
2. 요청 헤더에 커스텀 헤더를 사용하는 경우(표준이 아닌 헤더(e.g. X-Custom-Header)가 포함된 경우)
3. 리소스 Content-Type 이 application/x-www-form-urlencoded, multipart/form-data, text/plain 외의 타입(e.g. application/json) 인 경우

### Preflight 요청 동작 과정
1. OPTIONS 요청
   * 브라우저는 실제 요청을 보내기전 OPTIONS 메서드로 preflight 요청 전송
   * 이때, 요청에 대한 정보를 다음의 헤더들로 표현하여 서버측에서 요청을 허용할지 판단할 수 있게 함
     * Origin: 요청을 보내는 페이지의 출처.
     * Access-Control-Request-Method: 실제 요청에서 사용할 HTTP 메서드.
     * Access-Control-Request-Headers: 실제 요청에서 사용할 커스텀 헤더 목록.

```
OPTIONS /api/resource HTTP/1.1
Host: api.example.com
Origin: https://example.com
Access-Control-Request-Method: PUT
Access-Control-Request-Headers: X-Custom-Header
```

2. CORS 헤더와 함께 서버 응답
   * 서버는 요청을 허용할지 여부를 판단하여 다음과같은 CORS 응답헤더에 이를 표현
     * Access-Control-Allow-Origin: 요청을 허용할 출처.
     * Access-Control-Allow-Methods: 허용할 HTTP 메서드.
     * Access-Control-Allow-Headers: 허용할 헤더 목록.
     * Access-Control-Max-Age: Preflight 요청의 결과를 캐시할 수 있는 시간(초).

```
HTTP/1.1 200 OK
Access-Control-Allow-Origin: https://example.com
Access-Control-Allow-Methods: GET, POST, PUT
Access-Control-Allow-Headers: X-Custom-Header
Access-Control-Max-Age: 3600
```
