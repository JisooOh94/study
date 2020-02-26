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