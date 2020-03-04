# MyBatis
* 객체지향 언어인 자바의 관계형 데이터 베이스 프로그래밍을 좀더 쉽게 할수 있게 도와주는 개발 프레임워크
* 자바에서 제공하는 데이터베이스 프로그래밍 API 인 JDBC 의 기능을 좀 더 사용하기 쉽고 가볍게 구현한 프레임워크

# MyBatis 의 장점
1. 코드의 간결화, 설정의 간소화
2. 데이터 접근 속도를 높여주는 join 매핑, SQL 줄이기 기법, 가져오기 미루기 전략등으로 성능 향상
3. 리소스를 계층화하여 각 리소스의 관심사를 분리, 그를 통해 유지보수 효율 증대
4. SQL 문이 애플리케이션 소스코드로부터 완전히 분리

# MyBatis Components
| Component | Description |
|:---------:|:------------|
| SqlSessionFactoryBuilder | config 파일을 파라미터로 전달받아 그를 참조하여 SqlSessionFactory 생성 |
| SqlSessionFactory | SqlSession 인스턴스를 생성/삭제 하여 통신을 열고 닫는 세션 컨테이너|
| SqlSesson | Mapper를 참고하여 실제 Sql 통신 수행

### SqlSessionFactory
* SqlSession 인스턴스를 생성할 수 있는 컨테이너
* SqlSessionFactoryBuilder를 통해 XML 설정파일에서 인스턴스 빌드
* SqlSession 쓰레드의 안정성 이슈로 SqlSessionFacotory 인스턴스를 유지하며 DB와의 통신이 필요할때마다 Session 을 열어 통신, 완료후 통신 폐쇄
* config 파일 내용
   1) DB 연결 정보 (DB url, 계정 아이디/비밀번호 등...)
   2) Sql 쿼리 매퍼 경로
   3) type alias 정보
### SqlSession
* 데이터베이스에 대해 Sql 명령어를 실행하는데에 필요한 모든 메소드 포함
* 세션 생성, 차단 코드
```
SqlSession session = sqlSessionFactory.openSession();
< SQL 쿼리 작업 >
session.close()
```
### SQL Mapping
* Sql 쿼리문을 따로 xml에 작성하고 코드내에선 쿼리문의 id만 호출하여 사용
* 자바 코드와 Sql 쿼리문을 분리
* 쿼리문 작성 및 사용 효율성 증대
# 자주사용되는 SQL Mapper Attribute
### First Component
* insert,update,delete,select
* sql : 다른 구문의 부품으로 사용될 sql 조각 정의
* resultMap : 데이터베이스 쿼리 결과(select) 데이터를 원하는 객체에 대입하는 방법 정의
### Attribute
|Attribute|Description|
|:-------:|:----------|
|id|Sql문 고유 ID, 자바 코드에서 이 ID로 해당 Sql문 호출|
|parameterType|Sql문에 필요한 파라미터 타입|
|resultType|Sql문 처리 결과로 반환될 반환데이터 타입|
|resultMap|Sql문 처리 결과로 반환될 반환데이터를 대입할 resultMap ID|
|useCache|Sql문 처리 결과 데이터를 캐싱|
|flushCache|Sql문 처리 후 캐싱되어있던 데이터 삭제|
|timeout|Sql문 처리 요청 후 최대 대기 시간, 시간초과시 timeout|

# MyBatis Component Scope & Life cycle
### SqlSessionFactoryBuilder
* sqlSessionFactory 생성만 하면 그 후 유지할 필요 없음
* sqlSessionFactory 를 생성하는 메소드내에서만 생명이 있는 method scope로 생성
### SqlSessionFactory
* 한번 생성 후 애플리케이션이 실행되는 동안 계속해서 요청이 들어올때마다 SqlSession 생성해야함
* 싱글턴 패턴이나 빈으로 등록하여 application scope로 생성
### SqlSession
* Thread-Safe 하지 않으므로 공유자원이 되어서는 안됨
* Http 요청이 들어올때마다 SqlSessionFactory에 의해 새로 생성되고 해당 요청을 처리하는 스레드동안만 유지되다가 Http 응답을 보낼때 소멸되는 thread scope로 생성

# MyBatis 예제 
### 1. pom.xml 설정
```
pom.xml
... 위에 생략
<!-- 1. MyBatis 디펜던시 -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.4.6</version>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>1.3.2</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.1.5.RELEASE</version>
</dependency>
 
<!-- 2. MySQL 커넥터 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.12</version>
</dependency>
... 아래 생략
```

### 2. applicationContext.xml 설정
```
applicationContext.xml
... 위에 생략
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    <property name="driverClassName" value="com.mysql.jdbc.Driver" />
    <property name="url" value="jdbc:mysql://127.0.0.1:3306/test?useSSL=false&amp;characterEncoding=UTF-8&amp;serverTimezone=UTC" />
    <property name="username" value="[현재 사용중인 MySQL 사용자명]" />
    <property name="password" value="[현재 사용중인 MySQL 사용자 비번]" />
</bean>
 
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource" />
    <property name="configLocation" value="classpath:/mybatis-config.xml" />
    <property name="mapperLocations" value="classpath:/mappers/**/*Mapper.xml" />
</bean>
 
<bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate" destroy-method="clearCache">
    <constructor-arg name="sqlSessionFactory" ref="sqlSessionFactory"/>
</bean>
... 아래 생략
```

### 3. mybatis 설정 파일 생성하기
src > main > resources > mybatis-config.xml 파일 생성
```
mybatis-config.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <typeAliases>
 
    </typeAliases>
</configuration>
```

### 4. SQL문을 저장할 mappers 디렉토리 생성하기
src > main > resources > mappers 디렉토리 생성

### 5. 테스트 매퍼 작성
```
testMapper.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="mappers.testMapper">
 
    <insert id="insert">
        insert into
            tbl_user (id)
            values ('test')
    </insert>
 
</mapper>
```

### 6. DataSource 테스트
src > main > test > java > com.worksmobile.testapp.test 하위에 TestDatabase 클래스 작성
```java
TestDatabase.java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:web/WEB-INF/applicationContext.xml")
public class TestDatabase {
 
    @Autowired
    private DataSource dataSource;
 
    @Test
    public void testDataSource() throws Exception {
        System.out.println(dataSource.getConnection());
    }
 
}
```

### 7. SqlSession 테스트
```java
TestDatabase.java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:web/WEB-INF/applicationContext.xml")
public class TestDatabase {
 
    @Autowired
    private DataSource dataSource;
 
    @Autowired
    private SqlSession sqlSession;
 
    @Test
    public void testDataSource() throws Exception {
        System.out.println(dataSource.getConnection());
    }
 
    @Test
    public void testSqlSession(){
        System.out.println(sqlSession.getConnection());
    }
}
```

### 8. 데이터 삽입 테스트
```java
TestDatabase.java
... 위에 생략
@Test
public void testInsertColumn(){
    sqlSession.insert("mappers.testMapper" + ".insert");
}... 아래 생략
```