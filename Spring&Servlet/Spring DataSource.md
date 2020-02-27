# Connection
* 어플리케이션과 Database 서버를 연결하여 통신을 할 수 있도록 해주는 일종의 세션
* DriverManager 클래스의 getConnection() 을 통해 획득

# Connection Pool(DataBaseConnectoinPool)
* 웹 클라이언트로부터 요청이 있을때마다 매번 새로운 Connection을 생성하여 DataBase 서버와 통신하는것은 속도 저하 및 서버 부하 발생
* 대부분의 웹 애플리케이션은 웹 애플리케이션 서비스가 시작 되는 시점에 웹 서버에 미리 여러개의 커넥션을 생성해 둔 후, 필요할때마다 Connection을 가져다 사용하는 형태 사용
* 이때, 생성한 여러개의 Connection 이 저장되는곳이 Connection Pool

# DataSource
* Connection Pool에 저장되어있는 여러개의 Connection을 관리하고, 요청이 있을 시 Connection을 제공 및 반환 작업 수행
* Web Application 은 DataSource 객체의 getConnection 메소드를 통해 Connection 획득 및 반환
* Web Application 은 JNDI Service의 lookup 메소드를 통해 DataSource 객체 획득

# ApacheDBCP(Commons DBCP)
* APache 에서 제공하는 무료 DBCP
* JDK 1.7 버전 이상부터는 DBCP2 사용
## ApacehDBCP 속성 정리
### Connection 관련 속성
| 속성 | Description |
|:----:|:------------|
|initailSize|최초실행시, 커넥션풀에 채워둘 커넥션 수|
|maxTotal|동시에 사용될 수 있는 최대 커넥션 수|
|maxIdle|커넥션풀에 보관될 수 있는 최대 커넥션 수|
|minIdle|커넥션풀에 유지되야하는 최소 커넥션 수|
|maxWaitMillis|커넥션풀에 free상태의 커넥션이 없을때, 커넥션을 요청한 스레드의 최대 대기 시간|
|poolPreparedStatement|자주 실행되는 Statement를 사전에 Prepare하여 어플리케이션이 직접 호출 가능한 Common pool에 저장해두는 기능|

* maxTotal 과 maxIdle 의 수는 같에 설정하는것이 바람직하다. 그렇지 않을경우, maxTotal - maxIdle 만큼의 커넥션들은 매번 요청이 올때마다 생성되고 삭제되고, 이는 서버에 부하를 발생시킨다.
* maxWaitMillis 의 경우 default 값은 -1로 무한정 대기를 의미. 무한정 대기로 설정될 경우, 사용자 요청이 몰렸을때, 요청들이 커넥션을 할당받을때까지 무한정대기하게 되므로 계속해서 쌓이게 되고, 결국Tomcat 서버 스레드가 고갈되어 서버 자체가 죽어버리는 문제 발생\
* 적절한 maxWaitMillis 를 설정하고자 할 경우, 웹애플리케이션의 TPS 와 Tomcat 서버의 스레드 수를 고려하여 설정

### Connection Validation 관련 속성들
| 속성 | Description|
|:----:|:-----------|
|validationQuery|커넥션 유효성 검사시에 사용할 쿼리문|
|testOnBorrow|커넥션풀에서 커넥션을 받아올때, 해당 커넥션이 유요한지 검사|
|testOnReturn|커넥션풀에 커넥션을 반환할때, 해당 커넥션이 유효한지 검사|
|testWhileIdle|커넥션풀에 대기중인 free상태의 커넥션이 대기시간이 길어져 결국 끊어지는것을 방지하기 위해 validationQuery를 수행하도록 하는것|

* validationQuery의 경우 서버 리소스를 최대한 적제 사용하는 쿼리문으로 설정(select 1 from db_root)
* validation에 서버 리소스를 너무 많이 사용하는것은 지나친 낭비이므로 보통 testOnBorrow 와 testOnReturn은 사용하지 않음
* Cubrid의 경우 자체적으로 장시간 대기상태인 커넥션을 다시 refresh해주는 기능을 수행하므로 testWhileIdle 또한 비활성화