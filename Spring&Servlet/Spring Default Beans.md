***
# ```<mvc:annotation-driven/>``` 자동 등록 Bean

### RequestMappingHandlerMapping
* 사용자 요청을 처리할 Con`troller를 찾아 그 이름을 반환

### LocalValidatorFactoryBean
* JSR-303 빈 검증 기능의 구현체
* 스프링 웹어플리케이션 상에서 사용되는 데이터 검증 기능
* Hibernate Validator 이용
* 모델 데이터에 제약조건 설정을 통한 검증 기능등...
```
public class Person {
    @NotEmpty(message = "이름이 있어야 합니다.")
    private String name;

    @PositiveOrZero(message = "나이는 0 이상이어야 합니다.")
    private int age;
}
```

### FormattingConversionServiceFactoryBean
* Spring 에서 제공하는 다양한 Converter와 Formatter 를 사용가능하게 해주는 Bean
* 사용자 정의 Converter/Formatter 확장 기능 제공
   #### Converter / Formatter
   * 사용자 요청 메시지에 담겨오는 파라미터 데이터를 자동으로 Controller에서 정의한 파라미터 형태로 Binding 해주는 기능
   * Formatter의 경우 user-define type 객체를 문자열로 변환해주는 기능도 제공
   * 사용자 요청 메시지의 파라미터는 기본적으로 모두 String 타입
   * DateTimeFormat, NumberFormat, NumberToCharacterConverter, StringToEnum, StringToBoolean 등등...

### MessageConverters
* Ajax 통신시, XML/JSON 형태로 메시지를 송수신 할 수 있도록 자동으로 메시지를 변환해주는 기능
* 컨트롤러에서 클라이언트에게 XML/JSON 형태의 메시지로 응답시, 이를 클라이언트에서 인식 할 수 있는 Http 메시지로 변환
* MappingJackson2HttpMessageConverter, StringHttpMessageConverter 등..

### RequestMappingHandlerAdapter
* Dispatcher Servlet이 핸들러매핑으로 선택된 컨트롤러의 메소드를 호출할때 사용하는 어댑터

### HandlerExceptionResolver
* 웹애플리케이션 실행 중 발생하는 예외에 대한 처리를 담당하는 리졸버

### MappedInterceptor
* 사용자 요청이 dispatcher servlet에 전달되어 처리가 되기 전, 임의의 작업을 가능하게 해주는 기능
* 주로 클라이언트 인증 검사등의 로직 작성


### ExceptionHandlerExceptionResolver
* 예외가 발생한 컨트롤러 내에서 @ExceptionHandler 어노테이션이 붙은 메소드를 찾아 발생한 예외의 처리를 위임
* ExceptionHandler의 파라미터로 명시되어있는 예외 클래스와 발생한 예외 클래스가 다를경우, 또는 ExceptionHandler 어노테이션이 붙은 메소드가 없는경우, ResponseStatusExceptionResolver에게 처리를 위임
```
public class HelloController{
   @RequestMapping("/hello") 
   public void hello(){
      "Exception 예외 발생 가능성이 있는 메소드"
   }

   @ExceptionHandler(DataAccessException.class) 
   public ModelAndView dataAccessExceptionHandler(DataAccessException ex)  { 
      return  new  ModelAndView("dataexception" ).addObject("msg" ,ex.getMessage( ));
   } 
} 
```

### ResponseStatusExceptionResolver
* 예외 발생시, 단순한 HTTP 500 상태코드 대신, 좀 더 구체적이고 의미있는 User-define 상태코드를 응답해주는것
* 예외클래스에 @ResponseStatus 어노테이션을 통해 정의
* 처리하지 못할경우, DefaultHandlerExceptionResolver에게 처리를 위임

```
@ResponseStatus (value=HttpStatus.SERVICE_UNAVAILABLE , reason="서비스일시중지“ ) 
public  class  NotlnServiceException  extends  RuntimeExcept ion { }
```

### DefaultHandlerExceptionResolver
* 위의 두 리졸버로 처리하지 못한 예외를 다루는 마지막 리졸버
***
# 그 외의 Bean들
### MultipartResolver
* Spring MVC 프레임워크에서 제공하는 파일업로드 기능 구현 클래스
* Multipart 형식으로 전송된 파라미터와 파일 사용 기능 제공
### LocaleResolver
* 지역 정보를 설정해주는 리졸버
* default 리졸버인 AcceptHeaderLocalResolver는 HTTP 헤더 정보를 통해 지역정보 설정
### ThemeResolver
* 테마를 가지고 이를 변경해서 사이트를 구성할 경우, 사용가능한 테마 정보를 설정해주는 리졸버
### ViewResolver
* 컨트롤러가 리턴한 뷰 이름에 맞는 뷰 오브젝트를 찾아 Dispatcher Servlet에 반환해주는 리졸버
### ViewNameTranslator
* 컨트롤러에서 뷰 이름이나 뷰 오브젝트를 제공해주지 않았을경우, 요청정보(uri등)를 참고하여 자동으로 뷰 이름 생성해주는 리졸버

***
# 사용자 요청 처리 과정
1. 사용자 요청 Http 메시지 수신, Dispatcher Servlet에서 가로챔
2. Dispatcher Servlet에서 Handler Mapping에게 요청 전송
3. Handler Mapping에서 @RequestMapping을 참고해 사용자 요청을 처리할 Controller 메소드 이름 반환
4. Dispatcher Servlet이 Controller 메소드 이름과 함께 Handler Adapter에게 처리를 위임
4. Handler Adapter가 전달받은 Controller 객체의 메소드 호출
5. Controller는 bo-dao(DI) 를 통해 전달받은 요청 처리 후 그 결과를 ModelAndView 객체에 담아 반환
6. ModelAndView 객체에는 요청 처리 결과가 담겨있는 object(addObject)와 응답을 출력해줄 view 객체 이름(addViewName)이 담겨있음
7. Dispatcher Servlet은 Controller로부터 받은 ModelAndView 객체에서 view name을 추출해 View Resolver에게 전송
8. View Resolver는 view name에 해당하는 view 객체 반환
9. Dispatcher Servlet은 반환받은 view객체에 요청처리결과object 추가 및 render 수행
10. render된 응답결과를 Dispatcher Servlet이 HttpResponse body에 담아 전송