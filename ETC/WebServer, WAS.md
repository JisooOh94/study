__![image](https://media.oss.navercorp.com/user/13474/files/76107fc4-4cad-11e9-8cd4-d264aa689381)
# Web Server
* 서버 정적 리소스를 관리하며 클라이언트의 정적 컨텐츠 요청을 처리하는 서버
* 클라이언트가 요청한 정적 컨텐츠를 HTTP 프로토콜로 전송해줌
* 클라이언트의 요청을 판단해 Web Server에서 처리할 수 없는 동적 컨텐츠 요청일시, WAS에 처리를 위임
* 정적컨텐츠 : html, png, css
* Apache, Nginx
*** 
# Web Application Server
* 서버 동적 리소스를 관리하며 Web Server 로부터 위임받은 동적 컨텐츠 요청 처리
* 동적 컨텐츠 요청 처리를 위한 웹 애플리케이션 실행 역할
* Web Server 역할도 함께 수행 가능
* Tomcat
![image](https://media.oss.navercorp.com/user/13474/files/75f4a532-4cae-11e9-988f-f821e364af8a)
***
# Web Container
* 웹 애플리케이션을 실행시켜주는 소프트웨어
* 서블릿 컨테이너, JSP 컨테이너, EJB 컨테이너등
* JSP 페이지 요청시, 웹 컨테이너에서 서블릿으로 변환하여 컴파일 수행하고 그 결과를 웹서버에 전달
# Web Server/ WAS 구분 이유
* 정적컨텐츠 요청처리와 동적 컨텐츠 요청처리를 분산처리하여 처리 속도를 향상시키기 위해
