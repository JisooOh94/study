# Spring Model-View-Controller 패턴
* Controller 역할을 하는 Dispatcher Servlet이 각종 요청을 해당하는 서비스에 분산 시켜 전달하면 이를 각 서비스들이 처리하여 결과를 생성하고 생성한 결과를 미리 정의되어있는 다양한 view 페이지에 합쳐 화면에 출력

# Dispatcher Servlet
* Front Controller로서 사용자와 Controller, view 간 요청/응답의 교환 담당
* Handler Mapping, View Resolving 기능 수행
* web.xml 에 Dispatcher Servlet 등록
```
<servlet>
   <servlet-name>dispatcher</servlet-name>
   <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
   <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>/WEB-INF/dispatcher-servlet.xml</param-value>
</servlet>
```


# Handler Mapping
* 사용자로부터 전송받은 요청을 분석하여 해당 요청을 처리할 수 있는 Controller 를 찾아 처리를 위임
* Handler Mapping 방식

| Handler Mapping 방식 | Description |
|:--------------------:|:------------|
|BeanNameUrlHandlerMapping|URL 과 일치하는 bean name 을 갖는 Controller 빈으로 매핑|
|ControllerClassNameHandlerMapping|URL과 일치하는 bean class 를 갖는 Controller 빈으로 매핑|
|SimpleUrlHandlerMapping|URL 패턴에 매핑되는 지정된 Controller 빈으로 매핑, 하나의 컨트롤러에 2개이상의 url매핑가능
|DefaultAnnotationHandlerMapping|@RequestMapping(value="url")어노테이션을 통해 매핑|

# View Resolver
* Controller가 사용자 요청 처리 결과를 표시해줄 적절한 뷰의 논리적 이름을 반환하면, 이를 View Resolver가 받아 이름에 해당하는 뷰 오브젝트 생성, 이것을 Controller 가 다시 Dispatcher Servlet에 반환
## View Resolver Return type

### ModelAndView
* String 반환방식과 비슷하나 View 객체 이름을 파라미터로하는 ModelAndView객체 생성후 이를 반환|

```
@RequestMapping("/read", method=RequestMethod.GET);
public ModelAndView ReadController(HttpServlet request, Model model)throws Exception {
   String ID = request.getParameter("ID");
   model.addAttribute("ID",ID);
   return new ModelAndView("index");
}
```

### String
* 생성할 View 객체 이름 반환, View Resolver가 이름에 해당하는 뷰를 ModelMap에서 찾아 객체 생성|

```
@RequestMapping("/read", method=RequestMethod.GET);
public String ReadController(HttpServlet request, Model model)throws Exception {
   String ID = request.getParameter("ID");
   model.addAttribute("ID",ID);
   return "index";
}
```

### void
* RequestMapping 되는 url을 View 객체 이름으로 사용

```
@RequestMapping("/read", method=RequestMethod.GET);
public void ReadController(HttpServlet request, Model model)throws Exception {
   String ID = request.getParameter("ID");
   model.addAttribute("ID",ID);
   // read.jsp view 객체 생성
}
```

### View
* View 객체를 직접 생성하여 반환하는 방식

```
@RequestMapping("/read", method=RequestMethod.GET);
public View ReadController(HttpServlet request, Model model)throws Exception {
   String ID = request.getParameter("ID");
   model.addAttribute("ID",ID);
   return new InternalResourceView("/WEB-INF/views/index.jsp");
}
```

### ResponseBody
* View 페이지 자체를 String 형식으로 반환

```
@RequestMapping("/read", method=RequestMethod.GET);
public String ReadController(HttpServlet request, Model model)throws Exception {
   String ID = request.getParameter("ID");
   model.addAttribute("ID",ID);
   return "<html><body> ID= <@ ID @>" </body></html>";
}
```

## View Resolver 종류

### UrlBasedViewResolver
* 논리적인 view 이름과 실제 view 파일 이름이 같을 때 사용
```
<bean class="org.springframework.web.servlet.view.UrlBasedViewResolver">
   <property name="prefix" value="/WEB-INF/view"/>
   <property name="suffix" value=".jsp"/>
</bean>
```
### InternamResourceViewResolver
* 뷰 리졸버를 명시적으로 지정하지 않았을때 사용되는 디폴트 뷰 리졸버
* UrlBasedViewResolver 와 비슷하나 view 호출시 전체 경로와 함께 호출해주어야 해서 prefix,subfix와 함께 사용하는것이 필수
### ResourceBundleViewResolver
* 하나의 웹 어플리케이션에서 여러개의 view를 사용하고 또 코드 외부에서 각 컨트롤러가 사용하는 view의 종류를 자주 바꿔줘야 할때, 외부 property 파일을 통해 바꿀 수 있는 View Resolver
```
applicaioncontext.xml
<bean class="org.springframework.web.servlet.view.ResourceBundleViewResolver"/>

configuration.property
read.(class) = org.springframework.web.servlet.view.JstlView;
read.url = /WEB-INF/view/showUserInfo.jsp
```

# Spring MVC 사용자 요청 처리 과정
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