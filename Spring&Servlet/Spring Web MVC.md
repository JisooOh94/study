 # Spring 범위
### 넓은 의미
* DI/IoC 기능을 제공하는 Spring io의 전체 기술
### 좁은 의미
* Spring-web 과 Spirng-webmvc에서 제공하는 기술
* Sppring framework가 web을 지원하기 위해 가지고 있는 자체 web framework

***

# Spring Web MVC Framework
* 스프링 프레임워크의 자체 웹 프레임워크
* Dispatcher Servlet 중심으로 설계되어있음
* @Controller 와 @RequestMapping 애노테이션을 기반으로 다양한 핸들링 메서드 제공
* Servlet 과의 호환이 중요하므로 각각의 버전을 잘 맞춰주어야 함(Spring 4.x - Servlet 3.0 + + Tomcat 7+)

## 개발환경 설정
1. pom.xml에 spring-webmvc dependency 추가
2. src\main\webapp\WEB-INF\ 에 web.xml 생성 후 
3. dispatcher servlet 등록, url mapping 지정
4. application context 경로 지정

![image](https://media.oss.navercorp.com/user/13474/files/dfbd65e0-5964-11e9-8e21-daf9a60109dc)

***

# 웹 애플리케이션 아키텍쳐
## 레이어 아키텍쳐
* 각 기능별로 레이어로 구분을 하고 각 레이어가 기능에 대한 책임을 지는 구조
* 프리젠테이션 레이어 + 서비스 레이어 + 데이터 엑세스 레이어
* 레이어 아키텍쳐를 통해 관심사의 분리가 가능
### 관심사의 분리
* 기능별로 계층이 나위어져있기 때문에 기능간에 커플링이 느슨함
* 애플리케이션 설계의 유연, 견고
* 유지보수 및 테스트에 용이
## JSP 모델 1
* 레이어 아키텍쳐를 접목한 초기의 JSP 모델
* JSP 파일 내에 많은 로직들이 포함되어 관심사 분리정도가 낮았음

![image](https://media.oss.navercorp.com/user/13474/files/2afc870a-5967-11e9-9224-732522229151)

## JSP 모델 2(MVC)
* JSP 파일이 모두 담당하고 있는 기능, 책임을 나누어 관심사 분리정도를 높힘
* JSP -> Servlet(Controller) + JSP(View)

![image](https://media.oss.navercorp.com/user/13474/files/43898520-5967-11e9-9373-943d27797c94)

### JSP 모델 2(MVC) 패턴의 문제점
* 사용자 요청 종류에 따라 각 요청을 처리할 Controller 맵핑을 web.xml에 모두 기술해주어야함
* 사용자 요청 종류가 많아질수록 web.xml  또한 비대해짐

***

# Front Controller 패턴
* JSP 모델 2의 문제점인 web.xml이 비대해지는 문제를 해결한 패턴
* Handler Mapping을 개발자가 직접 web.xml에 일일히 기술해주는 것이 아닌, 자동으로 수행해주는 패턴
* Spring의 경우 Dispatcher Servlet 이 Front Controller

![image](https://media.oss.navercorp.com/user/13474/files/3f8d6ac6-5968-11e9-89be-26cb573db333)

## Front Controller를 이용한 사용자 요청 처리 과정

![image](https://media.oss.navercorp.com/user/13474/files/b82b98ea-5792-11e9-9fae-43730a3a9512)
1. 사용자 요청 Http 메시지 수신
2. Dispatcher Servlet에서 Handler Mapping에게 요청 전송
3. Handler Mapping에서 사용자 요청을 처리할 Controller 이름 반환
4. Dispatcher Servlet이 Handler Mapping에게 전달받은 Controller에게 요청 전송
5. Controller는 bo-dao 를 통해 전달받은 요청 처리 후 그 결과를 ModelAndView 객체에 담아 반환
6. ModelAndView 객체에는 요청 처리 결과가 담겨있는 object(addObject)와 응답을 출력해줄 view 객체 이름(addViewName)이 담겨있음
7. Dispatcher Servlet은 Controller로부터 받은 ModelAndView 객체에서 view name을 추출해 View Resolver에게 전송
8. View Resolver는 view name에 해당하는 view 객체 반환
9. Dispatcher Servlet은 반환받은 view에 요청처리결과object를 담아 최종적으로 response

***

# 알아두어야할 Annotation
### @ResponseBody
* View없이 모델만을 JSON이나 XML로 반환할때 컨트롤러에 추가
```
@RequestMapping("/info")
@ResponseBody
public user showUser(String id){
   return bo.getUser(id);
}
```
### @ResponseStatus
* View없이 모델만 전송할때, Http 상태코드도 함께 전송시 사용
```
@ResponseStatus(Http.Status.FINE)
public user showUser(HttpServletRequest req, HttpServletResponse resp){
   return bo.getUser(req.getParamter(id));
}
```
### @RequestParam
* GET메서드 등으로 전송받은 파라미터를 변수에 저장할때 사용
* request.getParameter() 와 같은 기능
```
@RequestMapping("/info")
public user showUser(@RequestParam("id") String id){
   return bo.getUser(id);
}
```
### @CookieValue
* 전송받은 요청 메시지의 쿠키 데이터를 받아올때 사용
* request.getCookies 를 통해 가져오는 방식과 동일
```
@RequestMapping("/info")
public user showUser(HttpServletRequest req){
   String id = req.getParameter("id");
   for(Cookie cookie : req.getCookies(){
      if(id.equals(cookie.getName()){
         bo.updateData(id);
      }
   }
   return bo.getUser(id);
}

@RequestMapping("/info")
public user showUser(@CookieValue String userId, @RequestParam("id") String id){
      if(id.equals(userId)){
         bo.updateData(id);
      }
   }
   return bo.getUser(id);
}
```