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