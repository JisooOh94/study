# Spring MVC
* 화면 렌더링을 담당하는 프레젠테이션 로직과 비즈니스 로직을 분리한 재사용 가능한 웹어플리케이션 구조
* 비즈니스 로직을 프레젠테이션 로직과 서블릿으로부터 분리해내어 POJO 로 개발할 수 있어 의존성이 해소되고 이식성이 높아짐

### MVC
* Model
	* 데이터 처리부
	* POJO 로 개발
	* 비즈니스 로직 수행
* View
	* 화면 처리부
	* 주로 JSP 로 개발
	* 프레젠테이션 로직 수행
	* model 파라미터를 바탕으로 html 페이지 생성
* Controller
	* 요청 처리부
	* Servlet 으로서 Model 에게 처리를 요청하고, Model 이 응답한 처리결과를 파라미터로 적절한 View 호출
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