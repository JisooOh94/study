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

# Argument Matchers
* 테스트 하고자 하는 기능에 전달할 파라미터 타입을 검증하는 기능
* anyInt(), anydouble()...
* Stubbing 의 when- then 과 함께 사용
```
when(mockObj.getSum(anyInt())).thenReturn(10);
```

# 메소드 실행 여부, 횟수 확인
* verify 이용
* 테스트하고자 하는 메소드가 특정조건으로 몇회 실행되었는지 검증
* verify(T mock, VerificationMode mode).method()
### VerificationMode
| Mode | Description |
|:----:|:------------|
|atLeastOnce|1번 이상 수행했는지 검증|
|atLeast(int n)|n 번 이상 수행했는지 검증|
|times(int n)|n 번 수행했는지 검증|
|atMost(int n)|최대 n번 수행했는지 검증|
|never()|수행되지 않았는지 검증|

# 메소드 실행 순서 확인
* inOrder 객체를 사용하여 메소드들이 의도한 순서대로 수행되었는지 확인
```
public class Multiplyer{
   public int getMult(int a, int b){
      return a*b;
   }
}

public void test(){
   Multiplyer firstMultiplyer = mock(Multiplyer.class);
   Multiplyer SecondMultiplyer = mock(Multiplyer.class);

   // mock이 순서대로 실행되는지 확인하기 위해 inOrder 객체에 mock을 전달
   InOrder inOrder = inOrder(firstMock, secondMock);

   // firstMock이 secondMock 보다 먼저 실행되는 것을 확인
   inOrder.verify(firstMultiplyer).getMult(1,2);
   inOrder.verify(SecondMultiplyer).getMult(3,4);
}
```
# injectMocks
* 한 클래스의 목 객체 생성시, 해당 클래스 멤버변수 오브젝트가 있을경우 목 객체 리스트에서 찾아 자동으로 주입
* @Mock 어노테이션 대신에 @InjectMocks 어노테이션 사용