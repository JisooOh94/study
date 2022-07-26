# Front Controller 패턴
### 등장 배경
* 기존의 Servlet 맵핑 방식은 요청 url 당 Servlet 을 생성하고 그에 맞는 Controller 에게 요청을 전송하는 방식
* 그에 따라 url 마다 처리할 Servlet 을 web.xml 에 모두 등록 필요
* 또한, 모든 요청에 공통적으로 수행되어야하는 처리(인코딩, 에러 핸들링등)를 각각의 서블릿에서 개별적으로 수행

![image](https://user-images.githubusercontent.com/48702893/107381090-da00ff00-6b31-11eb-9c26-969cdfe4b453.png)

### Front Controller
* 서블릿 컨테이너의 요청 처리 프로세스 제일 앞에서 들어오는 모든 요청을 처리하는 컨트롤러
* 하나의 Servlet 에서 모든 요청을 받은후, 적절한 Controller 에게 요청을 전송하는 방식
* 하나의 Servlet 만 등록해주면 되므로, web.xml 이 간결해짐
* 모든 요청에 필요한 공통처리를 한곳에서 수행하므로 공통로직 관리 용이

![image](https://user-images.githubusercontent.com/48702893/107381142-e84f1b00-6b31-11eb-9675-c4fa25c8211a.png)

<br>

# Dispatcher Servlet
* Spring 에서 Front Controller 기능을 수행하는 Servlet
* 모든 요청을 가로채 필요한 공통처리를 수행한뒤, 요청을 처리할 Controller 에게 요청 dispatch

### 처리 과정

![image](https://user-images.githubusercontent.com/48702893/107381836-99ee4c00-6b32-11eb-8838-a59f46a2b3ce.png)

### 등록 방법
1. DispatcherServlet 를 타입으로 servlet 등록
	* contextConfigLocation 초기화 파라미터로 servlet scope 빈 및 설정이 등록되어있는 context.xml 파일 명시
```xml
<servlet>
	<servlet-name>foo</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/NAVER/servletContext.xml</param-value>
	</init-param> 
</servlet>
```

2. <servlet-maaping> 태그를 통해 등록한 DispatcherServlet 으로 처리할 url 맵핑
```xml
<servlet-maaping>
	<servlet-name>foo</servlet-name>>
	<url-pattern>*.com</url-pattern>
</servlet-maaping>
```
```