# 서블릿
* 동적인 페이지를 생성하는 어플리케이션
* 일종의 자바로 구현된 [[CGI]](https://github.com/JisooOh94/study/blob/master/HTTP%20%EC%99%84%EB%B2%BD%EA%B0%80%EC%9D%B4%EB%93%9C/Content/8.%20%EA%B2%8C%EC%9D%B4%ED%8A%B8%EC%9B%A8%EC%9D%B4.md#cgi%EA%B3%B5%EC%9A%A9-%EA%B2%8C%EC%9D%B4%ED%8A%B8%EC%9B%A8%EC%9D%B4-%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4)
	* CGI : 웹서버와 외부 어플리케이션(웹어플리케이션) 을 연결하기 위한 표준
* 자바 스레드를 통해 동작
* 클라이언트가 동적인 페이지 요청시, 웹서버는 서블릿 컨테이너에게 처리 위임
* 서블릿 컨테이너는 요청을 처리할 수 있는 서블릿을 찾아 처리 명령

### 서블릿 동작 과정
1. 클라이언트로부터 HttpRequest 수신시, 웹서버는 서블릿 컨테이너에게 HttpRequest 전송
2. 서블릿 컨테이너는 배포서술자(web.xml) 을 기반으로 클라이언트 요청을 처리할 서블릿 탐색
	> [배포서술자(web.xml) 에 url - servlet mapping 정보 등록](https://github.com/JisooOh94/study/blob/master/%EC%9D%98%EC%8B%9D%EC%9D%98%ED%9D%90%EB%A6%84/%5BSpring%5D%20Servlet.md#%EB%B0%B0%ED%8F%AC%EC%84%9C%EC%88%A0%EC%9E%90webxml-%EC%97%90-url---servlet-mapping-%EC%A0%95%EB%B3%B4-%EB%93%B1%EB%A1%9D)
3. 서블릿 컨테이너는 요청을 처리할 서블릿의 service 메서드 호출하며 HttpServletRequest, HttpServletResponse 객체를 만들어 파라미터로 전송
4. service 메서드 내에서 클라이언트 요청에 따라 doGet/doPost 메서드를 호출하여 요청처리, 동적페이지 생성
5. 생성한 동적페이지를 HttpServletResponse 객체에 담아 컨테이너로 응답
6. 컨테이너는 HttpServletResponse 객체를 HttpResponse객체로 전환하여 클라이언트로 응답

![image](https://user-images.githubusercontent.com/48702893/107371810-0f085400-6b28-11eb-9992-6cbe5e287e58.png)
    
### 서블릿 컨테이너
* 클라이언트의 요청에 따라 적절한 서블릿을 탐색하여 요청 처리를 명령하는 컨테이너
* 서블릿 생명주기 관리
	* 서블릿 클래스 인스턴스화 및 init() 호출하여 생성된 서블릿 객체 초기화 > 컨테이너 실행할때 1회만 수행 (lazy-loading이 설정되어있을경우, 최초 호출되었을때 수행)
	* 서블릿 객체 참조 해제 및 GC 수행 > 컨테이너 종료시 1회만 수행
	
![image](https://user-images.githubusercontent.com/48702893/107372033-51ca2c00-6b28-11eb-8369-d01de1b43cdc.png)

* 웹서버와의 통신 담당
	* 웹서버와의 소켓통신(소켓 생성, 포트 listen, 패킷 accept 등)을 대신 수행해주어, 개발자가 비즈니스 로직 개발에 집중할 수 있게 함
* 멀티스레드 지원 및 관리
	* 요청마다 새로운 스레드를 생성하여 처리해야하는 서블릿 환경에서 스레드 생성 및, 생성된 다중스레드 관리 수행
	* 개발자가 멀티스레드 관리 및 동기화에 신경쓰지 않고 비즈니스 로직 구현에 집중 가능
* 선언적 보안관리
	* 비즈니스 로직에 직접 보안관련 작업을 할 필요 없이, 배포 서술자(web.xml) 에 필요한 보안기능을 선언적으로 선언해두면, 컨테이너가 자동으로 인식하여 보안작업 수행
	* 개발자가 비즈니스 로직개발에 집중할 수 있으며, 코드 재빌드 없이 배포서술자만 수정하여 보안관련 기능 변경 가능
	
### 배포서술자(web.xml) 에 url - servlet mapping 정보 등록
 1. servlet 등록 : \<servlet>태그 내에 <servlet-class> 으로 servlet 클래스 경로 명시, <setvlet-name> 으로 등록할 servlet 이름 지정
 ```xml
 <servlet>
 	<servlet-name>foo</servlet-name>
 	<servlet-class>com.naver.Foo</servlet-class>
 </servlet>
 ```
 2. servlet 맵핑 : <servlet-maaping> 태그 내에 <url-pattern> 으로 처리할 url 명시, <servlet-name> 으로 처리할 servlet 지정
 ```xml
<servlet-maaping>
	<servlet-name>foo</servlet-name>>
	<url-pattern>.*/get/foo</url-pattern>
</servlet-maaping>
```
