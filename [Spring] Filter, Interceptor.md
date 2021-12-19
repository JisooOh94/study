# Servlet Filter
* 서블릿(디스패쳐서블릿) 호출 전/후에 공통로직 수행해주는 필터
* 서블릿 컨테이너가 서블릿에게 사용자 요청 전달시, Filter가 가로채 공통로직 수행
* javax.servlet.Filter 인터페이스를 통해 구현
	* doFilter(ServletRequest, ServletResponse,FilterChain)에 공통 로직 구현
	```java
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//전처리
		chain.doFilter(request, response);	//수행해야할 다른 필터 호출
		//후처리
	}
	```

1. 인증(사용자 인증) 필터
2. 로깅 및 감시(Audit) 필터

### Filter 등록
1. web.xml 을 통한 등록
* <filter> 로 필터  등록 후, <filter-mapping> 으로 필터 적용할 url 지정 
```java
//web.xml
<filter> 
	<filter-name>MyFilter</filter-name>
	<filter-class>com.my.web.filter.MyFilter</filter-class>
</filter> 

<filter-mapping> 
	<filter-name>MyFilter</filter-name> 
	<url-pattern>/get/user</url-pattern> 
</filter-mapping>
```

2. annotation을 통한 등록
* Filter 구현 클래스에 @WebFilter 어노테이션 추가
* 어노테이션 파라미터로 필터 적용할 url 설정
```java
@WebFilter("/get/user")
public class MyFilter implements Filter { ... }
```

<br>

# Interceptor
* 디스패쳐 서블릿에게 호출되어, 클라이언트 요청을 컨트롤러에게 전달하기전 먼저 수행되어야하는 공통로직을 수행하는 객체 
* HandlerInterceptorAdapter 클래스를 상속받아 구현 
	* preHandle() , postHandle() , afterCompletion() 중 필요한 메서드만 오버라이딩하여 구현
	```java
	public class MyInterceptor extends HandlerInterceptorAdapter {
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception { ... }
		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {}
		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {}
	}
	```

### Interceptor 등록
* ServletContext.xml 의 <interceptors> 목록에 추가
* <mapping path> 로 인터셉터 적용할 url 설정
```java
//ServletContext.xml
<interceptors>
         <interceptor>
                  <mapping path="/**" /> 
                  <bean class="com.victolee.interceptor.MyInterceptor" />
         </interceptor>
</interceptors>
```

<br>

# Filter Interceptor 차이

![image](https://user-images.githubusercontent.com/48702893/140781451-3db388d3-8770-4216-b4dc-c07ef560a6de.png)

### Filter
* WAS가 디스패처 서블릿으로 요청을 전달하기 전/후에 부가작업 수행
* 필터체인에 부가작업을 수행할 웹필터 추가
* ServletRequest, ServletResponse 를 직접 조작할 수 있음(더 많은 기능 제공)
* 서블릿 컨테이너(e.g. Tomcat)가 관리
* 스프링과 무관하게 전역적으로 처리해야 하는 작업들을 구현하기에 적합
	* 보안(XSS 방어 등), 데이터 압축, 문자열 인코딩/디코딩, 모든 요청정보 로깅 등
	> 웹필터에서 보안 처리를 하는경우, 인증되지 않은 요청이 서블릿컨테이너에 전달되기전에 차단되므로 더 안전함

![image](https://user-images.githubusercontent.com/48702893/140780537-5117d39c-24ed-459d-bea8-cb937873b9b6.png)

### Interceptor
* 디스패처 서블릿이 컨트롤러로 요청을 전달하기 전/후에 부가작업 수행
* 실행체인에 부가작업을 수행할 인터셉터 추가
* HttpServletRequest, HttpServletResponse 를 직접 조작할 수 없음
* 스프링에서 관리
* 클라이언트의 요청과 관련되어 전역적으로 처리해야 하는 작업들을 처리
	* API 호출 로깅, 컨트롤러로 넘겨줄 정보 가공, 인증/인가 처리 등

![image](https://user-images.githubusercontent.com/48702893/140781198-1c1dd0fa-a118-4f74-94db-6b08537220c1.png)

***
> Reference
> * https://mangkyu.tistory.com/18
> * https://programmers.tistory.com/entry/JSP-연습문제-더하기-웹-프로그램
> * https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=adamdoha&logNo=221665607853
> * https://victorydntmd.tistory.com/176
> * https://mangkyu.tistory.com/173