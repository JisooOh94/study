# SpringBoot
* Spring 의 프로젝트 초기 환경 구성(필요한 의존성 등록, 배포서술자 작성, bean 컨테이너 작성, bean 등록 등)을 자동으로 수행해주어 생산성을 높힌 스프링 서브 프레임워크

* 웹 애플리케이션 컨테이너(e.g. Tomcat)을 내장하고 있어 별도 웹서버 필요 없이 단독으로 실행 가능

* 프로젝트 의존성버전관리를 대신해주어 개발자가 비즈니스 로직 개발에 집중할 수 있도록 해줌

<br>

# Spring vs SpringBoot

![image](https://user-images.githubusercontent.com/48702893/131243086-fd6c61b1-8632-4eea-9934-e1f10c352d6e.png)

### Spring
* DI, IoC를 통해 재사용 및 유지보수 용이한 코드 작성
* 타 프레임워크와의 통합에 용이하여 개발자가 비즈니스 로직 개발에 집중 가능(부가적인 필요한 기능들은 타 프레임워크 활용)
* 이를 통해 엔터프라이즈 환경의 기업용 애플리케이션 개발이 용이해지고 생산성 높아짐

### SpringBoot
* 스프링 기반 애플리케이션을 빠르고 쉽게 만들 수 있도록 도와주는 프레임워크
* 최소한의 설정으로 스프링 플랫폼 및 서드파티 라이브러리를 사용가능하여 초기 환경 구축 비용 절약 가능
* 웹 애플리케이션 서버를 내장하고 있어 별도의 서버 구축 및 관리 비용 절약 가능
* 이를 통해 Spring 기반 애플리케이션 개발이 용이해지고 생산성 높아짐

<br>

# SpringBoot 주요 기능
### AutoConfiguration
* starter 를 통해 써드파티 모듈을 사용하기 위해 필요한 설정 및 의존성 추가를 자동으로 수행
* pom.xml 에 모듈 starter 등록시, maven이 모듈 autoConfiguration jar 를 클래스패스에 추가 및 autoConfiguration jar 수행 코드를 spring.factories 에 추가
> aop starter 등록

![image](https://user-images.githubusercontent.com/48702893/131246827-3e068c32-a390-4556-b5b6-e8ac08d0ef37.png)

> aop starter jar 추가

![image](https://user-images.githubusercontent.com/48702893/131246835-b817c397-e05c-484a-ba29-2088badd5ca6.png)

> aop autoConfiguration 수행코드 추가

![image](https://user-images.githubusercontent.com/48702893/131246834-34140593-3867-485b-8ad6-1916396c6b3c.png)

* @EnableAutoConfiguration 을 통해 AutoConfiguration 활성화시, spring.factories 에 등록되어있는 autoConfiguration jar 들 수행
* autoConfiguration jar 는 모듈에 필요한 하위 의존성들의 등록 및 버전관리, bean 등록, properties 주입등을 자동으로 수행
	* e.g. Spring-boot-starter-jpa 등록시
	1. spring-aop, spring-jdbc 등의 의존성 추가
	2. classpath를 참고하여 사용중인 dbms 파악후, 자동으로 entityManager 빈 등록및 구성
	3. 모듈 설정에 필요한 properties 설정 제공

### 자동 의존성 관리
* spring-boot-dependencies를 이용해 starter 들과 그 내부 의존성들의 버전을 자동으로 관리
* spring-boot-starter 의 부모 의존성인 spring-boot-starter-parent 은 다시 spring-boot-dependencies 를 부로 의존성으로 가짐

![image](https://user-images.githubusercontent.com/48702893/131246842-9104b270-704a-4731-bb47-d733b56d08d0.png)

![image](https://user-images.githubusercontent.com/48702893/131246847-1c064200-8938-4ab4-863c-917a5ac09e20.png)

* spring-boot-dependencies 의 spring-boot-dependencies.pom에 spring boot 에서 관리하는 starter 버전 명시되어있음

![image](https://user-images.githubusercontent.com/48702893/131246851-ffe5624b-f385-44e1-86fe-240660589563.png)

* pom.xml 에 starter 추가시, spring-boot-dependencies.pom 에 명시되어있는 버전 사용

### 내장 웹애플리케이션 서버
* 기존 Spring 은 별도의 웹애플리케이션 서버(e.g. Tomcat, Netty) 을 두고, 웹 애플리케이션을 서버에서 돌아갈 수 있는 war 형태로 압축하여 실행
* SpringBoot는 자체적으로 웹애플리케이션 서버를 내장하고있어 별도의 서버 필요 없이 웹 애플리케이션을 jar 로 패키징 배포하여 단독 실행
* 서버에 의존성을 가지던 기존 웹 애플리케이션에서 반대로 웹애플리케이션에 서버가 의존
* 별도의 WAS 세팅이 필요없으므로 초기 환경 구성 및 유지 보수 비용 절약

***

> Reference <br>
https://goddaehee.tistory.com/238 [갓대희의 작은공간] <br>
https://donghyeon.dev/spring/2020/08/01/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8%EC%9D%98-AutoConfiguration%EC%9D%98-%EC%9B%90%EB%A6%AC-%EB%B0%8F-%EB%A7%8C%EB%93%A4%EC%96%B4-%EB%B3%B4%EA%B8%B0/ <br>
https://atoz-develop.tistory.com/entry/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B6%80%ED%8A%B8%EC%9D%98-%EC%9D%98%EC%A1%B4%EC%84%B1-%EA%B4%80%EB%A6%ACDependency-Management <br>