# Annotation driven
* Handler Mapping을 위한 기본 Component 들을 default 설정으로 자동으로 생성
* 사용자 요청을 적절한 Controller bean에게 전달하기 위한 Handler Mapping 과 HandlerAdapter를 bean으로 등록
* Default Handler Mapping은 BeanNameUrlHandlerMapping(요청 url과 일치하는 이름의 Controller bean에 전달)
* 선언 코드 : ``` <mvc:annotation-driven></mvc:annotation-driven>```
> Annotation을 이용한 빈 등록을 위해 component-scan 선언시 Annotation-driven 자동 활성화

# Handler Mapping 구현(annotation-driven 없이)
* 다음이 Handler Mapping 중 하나를 bean으로 등록하여 구현

| Handler Mapping 방식 | Description |
|:--------------------:|:------------|
|BeanNameUrlHandlerMapping|URL 과 일치하는 bean name 을 갖는 Controller 빈으로 매핑|
|ControllerClassNameHandlerMapping|URL과 일치하는 bean class 를 갖는 Controller 빈으로 매핑|
|SimpleUrlHandlerMapping|URL 패턴에 매핑되는 지정된 Controller 빈으로 매핑, 하나의 컨트롤러에 2개이상의 url매핑가능
|DefaultAnnotationHandlerMapping|@RequestMapping(value="url")어노테이션을 통해 매핑|

* Controller 클래스 구현시 AbstractController 클래스 상속 후 handleRequestInternal 메소드 오버라이딩

```
@Controller("/create")
public class CreateController extends AbstractController {
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map parameters = request.getParameterMap();
                ~
          }
}
```
* Controller 빈 등록시 빈 이름을 매핑할 url 주소로 설정
> Mapping 할 url : http://localhost:8080/read?id=abc<br>
  Controller bean name : @Controller("/read")