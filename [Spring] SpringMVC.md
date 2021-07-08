# Spring MVC
* 화면 렌더링을 담당하는 프레젠테이션 로직과 비즈니스 로직을 분리한 재사용 가능한 웹어플리케이션 구조
* 비즈니스 로직을 프레젠테이션 로직과 서블릿으로부터 분리해내어 POJO 로 개발할 수 있어 의존성이 해소되고 이식성이 높아짐

### MVC
* Controller
	* 사용자 요청 수신 및 Model 에게 처리 요청
* Model
	* 요청 처리 및 처리 결과를 보여줄 View 선택하여 처리 결과 전송
* View
	* Model 에게 전송받은 처리결과를 이용해 화면 생성하여 출력

### MVC 장단점
* 장점
	* 프레젠테이션 로직과 비즈니스 로직을 분리하여 확장에 용이
	* 개발자와 디자이너의 협업에 용이
* 단점
	* 기본기능 설계를 위해 많은 클래스들이 사용되므로 복잡해짐
	* 설계시간이 오래 걸리고 어려움
	* Model 과 View 사이의 의존성으로 인해 완벽히 분리 할 수 없어 패턴이 모호해지고 변형이 올 수 있음
	
### Spring MVC (MVP 와 유사)
* Model
	* 데이터와 데이터 처리부
	* POJO 로 개발
	* 비즈니스 로직 수행
* View
	* 화면 처리부
	* 주로 JSP 로 개발
	* 프레젠테이션 로직 수행
	* model 파라미터를 바탕으로 html 페이지 생성
* Controller
	* 요청 처리부
	* 사용자 요청 수신 및 Model 에게 처리 요청, Model 이 응답한 처리결과를 파라미터로 적절한 View 선택하여 호출
	* View - Model 간 통신 역할

### Spring MVC 요청 처리 과정

![image](https://user-images.githubusercontent.com/48702893/107517418-10ea1a00-6bf1-11eb-8764-ad82b7955a30.png)

1. [DispatcherServlet(FrontController)]() 에서 공통 처리 수행 후 HandlerMapping 객체를 통해 요청을 처리할 컨트롤러 검색 후 처리 위임
	* 검색된 컨트롤러를 호출하는것은 HandlerAdapter 가 수행
2. Controller는 Service 객체(Model)에게 비스니스 로직 수행 요청, Service 객체는 비즈니스 로직 수행 결과를 Controller 에게 응답
3. Controller는 응답을 model 객체에 적재, 프레젠테이션 로직을 수행할 View Name을 반환하여 ViewResolver 에게 전달
	* model
		* Controller 에서 View 로 html 페이지에 필요한 데이터를 전달할때 사용되는 map 객체로서 key-value 형태로 데이터 저장
		* Controller 에게 파라미터로 model 객체 전달시, Spring 이 자동으로 ViewResolver 에게 전송
		```java
		@RequestMapping("/foo")
		public String foo(@RequestParam String val, Map<String, Object> model) {
			model.put("value", val);
			return "bar";
		}
		```
		* Spring 에서 제공하는 Model 타입 객체로 map 대체 가능
		```java
		@RequestMapping("/foo")
		public String foo(@RequestParam String val, Model model) {
			model.addAttribute(val);	//key : 변수명("val")
			return "bar";
		}
		```  
4. ViewResolver 는 View Name을 통해 View 검색, 검색된 View 객체에게 model 객체 전달
	* ServletContext.xml 에 bean 으로 등록시 주입한 prefix, sufix 값과 View Name 을 조합하여 View 검색
	```xml
	<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/" />
        <property name="suffix" value=".jsp" />
    </bean>
	```
5. View 객체는 model 객체의 데이터를 가지고 Html 페이지 생성, DispatcherServlet 에게 응답
6. DispatcherServlet 은 페이지를 HttpServletResponse 객체에 담아 웹서버에게 응답

# Spring Context
### ApplicationContext
* 웹애플리케이션의 모든 Servlet 에서 공유하는 bean 및 설정을 등록하는 Context
* 모든 서블렛에서 사용되는 비즈니스 로직관련 설정 및 bean(Bo, @Service), DB 연결 관련 설정 및 bean(Dao, @Repository), logging 관련 설정 및 bean 등록
* Bean Factory 를 상속받아 IoC 컨테이너로서 동작[[참고]](https://github.com/JisooOh94/study/blob/master/%EC%A0%84%EB%AC%B8%EA%B0%80%EB%A5%BC%20%EC%9C%84%ED%95%9C%20%EC%8A%A4%ED%94%84%EB%A7%815/3.%20Spring%20IoC.md#ioc-container) 
* 등록 방법
	1. web.xml 에서 <context-param> 으로 applicationContext 설정 파일경로 명시
	2. <listener> 로 ContextLoaderListener 등록
	3. 서블릿 컨테이너 구동하여, ServletContext 인스턴스가 생성되면, ServletContextListener 를 구현한 ContextLoaderListener가 호출됨
	4. ContextLoaderListener 는 <context-param> 으로 명시한 설정파일을 읽어들여 ApplicationContext 생성 및 bean 등록, 설정 적용 수행
```xml
//web.xml
<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>/WEB-INF/NAVER/applicationContext.xml</param-value>
</context-param>

<listener>
	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

### ServletContext
* 하나의 Servlet 에서 사용되는 bean 및 설정을 등록하는 Context
* 주로 Servlet 에 mapping 되는 url 들을 처리할 Controller bean 등록
* ApplicationContext 를 상속받으므로 ApplicationContext에 등록된 bean 및 설정들 사용 가능
* Servlet 등록시, contextConfigLocation 초기화 파라미터로 ServletContext 설정 파일 경로 명시하여 등록
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

