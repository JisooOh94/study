# Mock
* Junit Test에서 일반적인 방식으로는 테스트 할 수 없는 기능(e.g. DB에 데이터 삽입)을 Test 할 때 사용
* 테스트 하고자 하는 기능을 담당하는 클래스를 상속받는 Mock 클래스 생성하여 기능 재정의
* 기능 수행을 위한 메소드가 제대로 호출되었는지, 기능 수행에 필요한 파라미터가 제대로 전달 되었는지등을 확인 할 수 있도록 메소드 재정의
* Mock 클래스의 오브젝트를 생성하여 메소드를 실행하여도 아무런 작업을 하지 않음. Stubbing을 통해 기능을 재정의하여 사용
### Mock 클래스 오브젝트 생성 방법
1. 클래스를 상속받아 Mock 클래스로 재정의 하여 사용
2. Mockito의 Mock(클래스명.class) 메소드를 통해 생성
3. @Mock 어노테이션을 이용하여 생성
#### 원본 클래스
```
public class UserDao {
	private ConnectionMaker connectionMaker;

	public void add(User user) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = connectionMaker.makeNewConnection();
			ps = conn.prepareStatement("insert into users(id,name,password) values(?,?,?)");

			ps.setString(1, user.getId());
			ps.setString(2, user.getName());
			ps.setString(3, user.getPassword());

			ps.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
.
.
.
```
#### Mock으로 재정의한 클래스
```
public class UserDaoMock extends UserDao {
	String mockUserName;
	String mockUserId;
	String mockUserPassword;
	int mockCount;
	boolean isCalled;

	@Override
	public void add(User user) {
		isCalled = true;
		mockUserId = user.getId();
		mockUserName = user.getName();
		mockUserPassword = user.getPassword();
	}
}
```
#### Mock 오브젝트를 통한 기능 Test
```
@Test
	public void addTestUsingMockObj() {
		UserDaoMock userDaoMock = new UserDaoMock();
		User user = new User("testId","testName","testPassword");
		userDaoMock.add(user);

		Assert.assertTrue(userDaoMock.getIsCalled());
		Assert.assertEquals("testId",userDaoMock.getUserId());
		Assert.assertEquals("testName",userDaoMock.getUserName());
		Assert.assertEquals("testPassword",userDaoMock.getUserPassword());
	}
```
# Stubbing
* Mockito의 Mock(클래스명.class) 메소드로 생성한 Mock 오브젝트의 메소드 호출에 대한 반환값을 미리 정의해두는것
* 어느 한 기능을 테스트하는데에 다른 오브젝트의 메소드를 호출하여 반환받는 값이 필요한 경우, 해당 오브젝트를 Mock 오브젝트로 생성하여 메소드 반환값을 Stubbing 으로 정의해 테스트 진행
* when() thenReturn()/thenThrow() 등을 사용
### Stubbing 메소드 종류
1. thenAnswer(Answer<?> ans) : 원하는 작업을 정의하여 수행
2. thenCallRealMethod() : 메소드가 구현되어있을 경우 실제 구현 메소드 호출
3. thenReturn(T value) : 지정한 값 반환
4. thenReturn(T value, T value...) : 지정한 값들을 순차적으로 반환
5. thenThrow(Throwable e) : 예외 객체 반환