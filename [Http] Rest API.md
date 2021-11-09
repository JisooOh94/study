# Rest(REpresentational State Transfer)
* Http 프로토콜을 이용하여 원격시스템의 기능을 호출하는 API 정의 방법중 하나
* 기존에 Rest API 가 나오기 전 사용되던 SOAP 방식에 비해 더 단순하고 확장에도 용이

### REST API 구성
* 자원(resource)
	* URI
	* 행위를 가할 대상, 자원
* 행위(verb)
	* Http Method
	* 수행할 행위
	* Get(read), Post(create), Put/Patch(update), Delete(delete)
* 표현(representation)
	* 자원(자원 상태)에 대한 표현
	* JSON, XML 등의 여러가지 자원 상태 표현 방식 존재

### Rest API 특징
* Uniform (균일한 인터페이스)
	* 동일한 리소스에 대한 요청은 항상 동일한 응답을 보장해야함
* Stateless(무상태)
	* 작업을 위한 별도의 상태정보(세션, 쿠키등)를 따로 저장하지 않음
	* 각각의 요청을 완전히 별개의 것으로 인식, 이전 요청이 다음 요청의 처리에 영향 x
	* 작업처리시 별도의 상태정보를 신경쓰지 않아도되므로 구현 및 처리가 단순해짐
* Cacheable(캐시 가능)
	* Http 프로토콜 상에서 동작하므로 캐시 기능 지원 
* Self-descriptiveness (자체 표현 구조)
	* Rest API 메시지만 보고 해당 메시지의 내용 및 요청을 쉽게 이해할 수 있음
* Client - Server 구조
	* Client와 Server 의 책임 및 역할을 명확히 구분
		* Client : 사용자 인증, 컨텍스트(세션) 관리
		* Server : API 제공
	* 그를 통해 개발해야할 내용이 명확해지고 서로간의 동작방식에 영향 받지 않으므로 의존성 해소
* 계층형 구조
	* Rest 서버는 다중계층으로 구성 가능(로드밸런싱 서버 - 인증 서버 - 서비스 서버 등의 계층 구조)

### Rest API 의 장점
* 통일성 : 정해진 규칙에 따라 URI 생성
	* URI 개발 리소스 절약
	* API 해석 및 이해에 용이하여 유지보수 비용 및 개발자간 커뮤니케이션 비용 절약
* 확장성 : 기존 API URI 를 확장하여 신규 API URI 개발에 용이
	* 기존 사원 정보 조회 api : /company/employees/{id} > 신규 사원이 담당하는 서비스 정보 조회 api: /company/employees/{id}/services/{name}
* 낮은 러닝커브 및 높은 호환성
	* HTTP 프로토콜 기반으로 동작하기때문에 Rest API 를 위한 별도의 인프라 구축 불필요
	* HTTP 프로토콜에 따르는 모든 플랫폼에서 사용이 가능
	
<br>

# REST API 설계 규칙
### 자원은 URI 로 표현
* 동사보다는 명사를, 대문자보다는 소문자 사용
```java
POST     /club/member/delete (X)
DELETE  /club/member           (O)
```

### 행위는 Http Method로 표현
* 기존의 POST Method 만 사용하고 행위는 URI 로 명시하던 방식에서 Http Method 로 행위 명시
```java
POST   /club/member  (회원 생성)
GET     /club/member  (회원 목록)
PUT     /club/member  (회원 정보 수정)
DELETE /club/member  (회원 탈퇴)
```

### 슬래시 구분자( / )는 계층 관계를 나타내는데 사용
```java
/club/member
/company/employees
```

### URI 마지막 문자로 슬래시( / )를 포함하지 않음
```java
/club/member/ (X)
/club/member  (O)
```

### 하이픈( - )은 URI 가 길어질때 가독성 향상을 위해 사용
* 밑줄( _ )은 폰트나 특정 상황에서 가려지는 경우가 있어 하이픈(-)을 사용
```java
/club/elderlymembers
/club/elderly-members
```

### URI에는 파일확장자를 포함하지 않음
```java
/club/salesgraph.jpeg (X)
/club/salesgraph      (O)
```

### 컬렉션은 복수, 도큐먼트는 단수로 표현
* 도큐먼트 : 객체, 자원 (e.g. diesel, gasolin, electric)
* 컬렉션 : 도큐먼트의 집합 (e.g. cars, manufacturers)
```java
GET /cars/gasolin/manufacturers		//가솔린 자동차 제조사 목록 조회
GET /cars/gasolin/manufacturers/hyundai	//이름이 hyundai 인 가솔린 자동차 제조사 정보 조회
```

> Rest API 설계 예시

![image](https://user-images.githubusercontent.com/48702893/133921013-84c26b96-8b1e-4660-9d13-55870d66291b.png)

<br>

***

> Reference <br>
> https://meetup.toast.com/posts/92 <br>
> https://www.a-mean-blog.com/ko/blog/%ED%86%A0%EB%A7%89%EA%B8%80/_/REST%EC%99%80-RESTful-API <br>
> https://gmlwjd9405.github.io/2018/09/21/rest-and-restful.html <br>