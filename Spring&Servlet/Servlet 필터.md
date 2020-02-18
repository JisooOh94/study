# Filter 역할
* 클라이언트와 서버 사이에서 request, response 메세지를 가로채 전처리/후처리 작업 수행
* Servlet 내부 코드 수정 없이 기능 수정, 추가 가능
* 컨테이너에 등록되어있는 전체 Servlet에서 공통적으로 수행해줘야 하는 작업 정의에 적합
> e.g. 클라이언트 통신 시간 기록

# Filter Chain
* 한번의 필터링에 여러 필터 적용 가능

![image](https://user-images.githubusercontent.com/48702893/74338527-b94e7e00-4de5-11ea-8543-23cd9f7d4377.png)

# Filter 구현
* Request Filter, Response Filter 로 구성
* javax.servlet.Filter 인터페이스를 구현하여 개발
* init, doFilter, destroy 메서드로 구성
* doFilter : ServletRequest, ServletResponse, FilterChain 을 파라미터로 사용, 필터링 로직 정의

|ServletRequest | HttpServletRequest |
|:-------------:|:------------------:|
|ServletResponse | HttpServletResponse|
|FilterChain|해당 필터 다음에 호출할 필터/서블렛 객체|

![image](https://user-images.githubusercontent.com/48702893/74338559-c79c9a00-4de5-11ea-9fb0-12aff988b551.png)

# Filter 정의
* 배포 서술자에 정의
* 필터 등록 및 URL 패턴과 필터 매핑 선언
* 필터링할 요청 매핑
   * url-patter 을 설정하여 특정 url에 해당하는 서블릿 요청에 대해 필터링
   * servlet-name 을 설정하여 특정 서블릿 요청에 대해 필터링
   * 하나의 요청에 대해 여러개의 필터 적용시 DD에 정의되어있는 순서대로 정의됨
   * 여러개의 필터 적용시 url-patter으로 매핑되어있는 필터부터 탐색 후 servlet-name으로 매핑되어있는 필터 탐색

```
<filter>
   <filter-name>testFilter</filter-name>
   <filter-class>service.bo.testFilter</filter-class>
</filter>

<filter-mapping>
   <filter-name>testFilter</filter-name>
   <url-pattern>/irteam/getJson</url-patter>
   <servlet-name>service.dao.responseJson</servlet-name>
</filter-mapping>
```

# Filter 생명주기
1. 필터 인스턴스 생성
* 컨테이너가 init()메소드를 호출하며 필터 인스턴스 생성
* 필터 호출 전 필터 설정사항들을 init()메서드에 정의
2. 필터링 수행
* 컨테이너가 현재 요청에 필터 적용시 doFilter()메서드 호출
3. 필터 인스턴스 제거
* 컨테이너가 destroy() 메서드 호출
* 인스턴스 삭제 전 후처리 작업 정의(주로 필터에 사용된 메모리 공간 해제 작업)

