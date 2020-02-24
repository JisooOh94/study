# 쿠키, 세션 목적
* 비연결지향성인 Http 프로토콜은 요청-응답이 끝나면 통신이 종료되며 수신자,발신자의 상태정보 또한 삭제
* 클라이언트의 하나의 요청을 수행하기 위해서는 여러번의 통신 트랜잭션이 이루어져야함
* 통신이 종료되어도 상태정보를 유지하여 다음 통신에서 활용되도록 하는 기능

# 쿠키, 세션 차이
|차이점| 쿠키 | 세션 |
|:----:|:----:|:----:|
|저장위치 | 클라이언트 | 서버 |
| 라이프사이클 | 브라우저 종료해도 유지 | 브라우저 종료시 삭제 |
| 속도 | 빠름 | 느림 |

# 쿠키
* 로컬에 파일로 저장되어 보안이 약함
* 키-값 구조
* 만료 시간 설정 가능
* 자동로그인, 로그인 상태 유지, "오늘 더이상 이광고 보지 않음" 등
* 작동과정
   1. 클라이언트에서 서버로 요청 전송
   2. 서버에서 전송받은 요청 메시지의 헤더를 탐색해 쿠키 파일이 있는지 확인
   3. 쿠키 파일 있을 시, 요청 처리에 활용
   4. 쿠키 파일 없을 시, 최초 요청으로 간주하고 요청 처리 후 쿠키 파일 생성하여 응답메시지에 포함시켜 전송
   5. 클라이언트에서 전송받은 응답메시지의 쿠키 파일을 로컬에 저장
* 쿠키 관련 메소드 리스트

![image](https://media.oss.navercorp.com/user/13474/files/e20482c0-4fdc-11e9-9ec3-937fe96ce61a)

* 쿠키 설정 예제(javax.servlet.http 패키지의 Cookie 클래스 사용)
1. 쿠키 생성

```
Cookie info = new Cookie("testCookie", "Hello Cookie");
info.setMaxAge(365*24*60*60);
info.setPath("/");
response.addCooke(info);
```

2. 요청 메시지로부터 쿠키를 읽어 데이터 추출

```
Cookie[] cookies = request.getCookies();            // 요청정보로부터 쿠키를 가져온다.

for(int i = 0 ; i<cookies.length; i++){                            // 쿠키 배열을 반복문으로 돌린다.
out.println(i + "번째 쿠키 이름 : " + cookies[i].getName());            // 쿠키의 이름을 가져온다.
out.println(i + "번째 쿠키에 설정된 값 : " + cookies[i].getValue());    // 쿠키의 값을 가져온다.
}
```

3. 클라이언트에 저장되어있는 쿠키 모두 삭제

```
Cookie[] cookies = request.getCookies();            // 요청정보로부터 쿠키를 가져온다.

for(int i = 0 ; i<cookies.length; i++){                           
cookies[i].setMaxAge(0);
response.addCookie(cookies[i]);            // 해당 쿠키를 응답에 추가(수정)한다.
}
```