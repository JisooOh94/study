# 단일 책임 원칙(Single Responsibility Principle)
* 하나의 모듈, 클래스, 메서드는 하나의 책임만 가져야한다는 원칙
* 클래스를 수정해야하는 이유는 오직 그 클래스가 담당하는 기능(책임) 변경뿐이어야 하며 다른 기능(책임) 변경에 영향 받지 않아야 함
* 각각의 모듈, 클래스, 메서드를 변경해야하는 경우를 생각하고 그것디 2개 이상일 경우 분리 수행
* 예시
```java
public class Employee
{
	public Money calculatePay(){ ...};	//급여 계산 함수
	public String reportExtraHours(){...};	//야근 보고 함수
	public void save(){...};		//데이터 저장 함수
}
```
	* caculatePay : 급여 체계 변경시 수정 필요
	* reportExtraHours : 보고 양식 변경시 수정 필요
	* save : 테이블 스키마 변경시 수정 필요
	* 하나의 클래스를 수정해야하는 이유가 2개 이상이므로 분리 필요

### 효과
* 한 책임의 변경으로 다른 책임의 변경까지 초래하는 연쇄작용 해소
* 코드 가독성 향상 및 유지보수 용이

### 적용방법
* 하나의 클래스에 혼재된 각 책임을 각각의 개별 클래스로 분할

```java
public class Employee
{
	public Money calculatePay() ...
}
 
public class EmployeeReporter
{
	public String reportHours(Employee e) ...
}
 
public class EmployeeRepository
{
	public void save(Employee e) ...
}
```

* 분할한 클래스들이 비슷한 책임을 중복해서 가지고있을경우 부모클래스로 분할
	* 동일한 기능을 부모클래스에 정의하고 확장/변화가 필요한 부분만 자식클래스에서 정의
	> 일종의 템플릿 메서드 패턴
```java
public abstract class Calculator {
	public int calculate(int param_1, int param_2) {
		int result = param_1 + param_2;
		postProcess(result);
		return result;
	}
	
	public abstract void postProcess(int result);
}

public class LoggerCalculator extends Calculator{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void postProcess(int result) {
		logger.info("result : {}", result);
	}
}

public class DBCalculator extends Calculator{
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void postProcess(int result) {
		jdbcTemplate.update("insert into calcHistory (date, result) values(?, ?)", LocalDateTime.now(), result);
	}
}
```

<br>

# 개방폐쇄원칙
* 소프트웨어의 구성요소(컴포넌트, 클래스, 모듈, 함수)는 확장에는 열려있고, 변경에는 닫혀있어야 한다는 원칙
	* 확장에 열려있다 : 요구사항이 변경되었을때, 새로운 행위를 추가하여 모듈을 확장함으로서 변경되 요구사항에 대응할 수 있어야 함
	* 변경에 닫혀있다 : 요구사항이 변경되었을때, 기존 모듈의 코드를 수정하면 안됨. 새로운 행위를 추가하여 모듈을 확장할때, 기존 모듈의 코드나 그 사용부의 코드가 수정되면 안됨  
* 요구사항의 변경이나 추가가 발생해도, 기존 구성요소를 수정하면 안되며, 기존 구성요소를 확장하여 대응
* 즉, 요구사항의 변경에 따라 프로그램 기능을 수정해야할때, 해당 기능을 담당하던 클래스를 수정하는것이 아닌, 확장(상속)하여 새로운 코드를 덧붙이는 방식으로 기능 수정

### 추상화
* 추상화(인터페이스)를 통해 개방폐쇄적 클래스 설계 가능
	* 할 수 있는 행위(api = 메서드) 를 고정함 > 수정에 닫혀있음
	* 상속을 받아 행위의 내부 동작을 자유롭게 구현할 수 있음 > 확장에 열려있음
* 기존 구성요소 확장시, 폐쇄 원칙에 따라 기존 구성요소 코드 뿐만 아니라, 이를 사용하던 사용부 코드도 수정되면 안됨
```java
public class NormalFoo {
	public void doSomething() { ... }
}

public void useFoo(NormalFoo foo) {
	foo.doSomething();
}

public class UpgradedFoo extends NormalFoo {
	@Override
	public void doSomething() { ... }
}

public void useFoo(UpgradedFoo foo) {		//폐쇄원칙 위배, UpgradedFoo 로 기능 교체시 기존 코드도 수정 필요
	foo.doSomething();
}
```
* 인터페이스를 통해 기능 수행을 위해 필수인 동작들을 추상화하여 메서드로 정의
```java
public interface Foo {
	void doSomething();
}

public class NomalFoo implements Foo {
	@Override
	public void doSomething() { ... };
} 

public class UpgradedFoo extends NormalFoo {
	@Override
	public void doSomething() { ... }
}
```
* 사용부에선 인터페이스를 참조변수로 가짐으로서 변경되지 않는 인터페이스에 의존성을 가지고 기존 코드 수정 없이 기능 수정 가능
	> 일종의 스트레티지(Strategy) 패턴
```java
public void useFoo(Foo foo) {
	foo.doSomething();
}
```

### 단점
* 클래스를 아무리 수정에 닫혀있도록 추상화하여 설계하여도, 닫혀있지 않는 요소에 대한 수정 발생 가능 > 완벽한 폐쇄 불가능
* 추상화 설계는 개발 리소스도 많이 들고, 설계의 복잡성을 높임
> 따라서 클래스 설계시 변경이 일어날 가능성이 가장 높은 요소에만 OCP 를 적용하는것이 효율적이며 그에따라 앞으로 일어날 변경의 확률을 예측하는것이 중요
> 어설픈 추상화를 피하는 일은 추상화를 하는것 만큼 중요

<br>

# 리스코프 치환원칙
* 베이스 클래스가 서브 클래스로 치환되어도 동일한 동작을 보장해야 한다는 원칙
* 부모 클래스 타입인 A를 사용하는 기존의 프로그램 코드가 자식 클래스 B로 대입 시켰을 때도 문제 없이 작동하도록 하기 위해서, 자식 클래스는 부모 클래스가 따르던 계약 사항을 자식도 따라야한다.
* 자식 클래스로 부모 클래스의 내용을 상속하는데, 기존 코드에서 보장하던 조건을 수정하거나 적용시키지 않아서, 또는 엉뚱한 자식 클래스를 구현해서, 기존 부모 클래스를 사용하는 코드에서 예상하지 않은 오류를 발생시킨 것이다.
* 자식 클래스를 구현하는 개발자가 기존 프로그램이 문제없이 안정적으로 작동할 수 있도록 가이드라인을 알려주는 원칙
* 콘크리트 클래스를 상속받아 재정의 하거나, 추상클래스의 일반 메서드를 재정의 하는경우 발생 가능

### 리스코프 치환원칙 위반 예시
1. width, height 멤버필드 및 getArea(넓이계산메서드) 를 가지는 직사각형 클래스 정의
```java
class Rectangle {		//직사각형
	private int width, height;
	
	public void setWidth(int width) { this.width = width; }
	public void setHeight(int height) { this.height = height; }
	public int getArea() { return width * height; }
}
```

2. 직사각형 클래스를 상속받는 정사각형 클래스 정의. 정사각형의 속성에 맞춰 setWidth, setHeight 메서드 오버라이드 > 부모클래스 구현원칙 위반
```java
class Square extends Rectangle{		//정사각형 (정사각형은 직사각형이다 > 성립)
	@Override
	public void setWidth(int width) { this.width = this.height = width; }
	@Override
	public void setHeight(int height) { this.height = this.width = height; }
}
```

3. Rectangle 객체를 사용한 메서드 수행 결과와, 자식클래스인 Square 객체를 사용한 메서드 수행 결과가 달라짐 > 리스코프 치환원칙 위배
```java
public int getTileSize(Rectangle areaCalculator, int tileWidth, int tileHeight) {
	areaCalculator.setHeight(tileHeight);
	areaCalculator.setWidth(tileWidth);
	return areaCalculator.getArea();
}

@Test
public void test() {
	int rectangleSize = getTileSize(new Rectangle(), 2, 3);		//6
	int squareSize = getTileSize(new Square(), 2, 3);		//9
	assertEquals(recnagleSize, squareSize);		//false
}
```

### 리스코프 치환원칙 위반 예시_2
1. content 멤버필드, getContent() 메서드를 가지는 Parent 클래스 정의
	* getContent 메서드 명세로 content 필드가 null 일경우 default value 를 반환하는 제약사항 정의  
```java
public class Parent {
	private Content content;
	
	public void setContent(Content content) { this.content = content; }
	
	public Content getContent() {
		if(content == null) return new Content();
		return this.content;
	}
}
```

2. 추후, 스펙이 변경되어 Parent 클래스 기능 수정 필요 > 개방폐쇄원칙에 따라, Parent 클래스를 상속받아 확장하여 기능 수정

3. 이때, getContent 메서드 확장시, default value 반환 제약사항을 망각한채로 오버라이드
```java
public class Child {
	private Content content;
	
	public void setContent(Content content) { this.content = content; }
	
	public Content getContent() {
		return this.content;
	}
}
```

4. 기존, Parent 객체 사용 코드에 Child 객체 전달시 NPE 발생 > 리스코프 치환원칙 위배 
```java
public void writeContentLog(Parent parent) {
	Content content = parent.getContent();
	content.writeLog();		//NPE 발생
}
```

### 리스코프 치환원칙 준수를 위한 클래스 확장 제약사항
1. 하위형에서 메서드 인수의 반공변성
2. 하위형에서 반환형의 공변성
3. 하위형에서 메서드는 상위형 메서드에서 던져진 예외의 하위형을 제외하고 새로운 예외를 던지면 안 됨
	* 자식클래스 메서드에서 부모클래스 메서드에서 던지는 예외 이외나 그 하위타입 예외 이외에 아예 다른 예외를 던지면 안됨
	```java
	//Parent
	public void doSomething(int param) {
		if(param < 0) throw new InvalidParameterException();
		...
	}

	//Child
	@Override
	public void doSomething(int param) {
		if(param < 0) throw new InvalidParameterException(); 
		...
		if(result == null) throw new RuntimeException();	//새로운 타입 예외 throw 불가
	}
	```
4. 하위형에서 선행 조건은 강화될 수 없다.
	* 메서드 수행전, 정상적인 메서드 수행을 위해 실행하는 파라미터, 객체 필드 유효성 검사 로직을 자식 클래스 메서드에서 더 추가하여 강화할 수 없음
	```java
	//Parent
	public void doSomething(int param) {
		if(param == 0) {
			logger.warn("Invalid parameter");
			return;
		}
	}
	
	//Child
	@Override
	public void doSomething(int param) {
		if(param == 0 || param < 0) {		//선행조건 강화 불가, 리스코프 치환원칙 위배
			logger.warn("Invalid parameter");
 			return;
		}
	}
	```
5. 하위형에서 후행 조건은 약화될 수 없다.
	* 메서드 수행 후, 결과값이나 사용된 파라미터, 필드가 유효한지 검사하는 로직을 자식클래스 메서드에서 생략하거나 조건을 완화할 수 없음
	```java
	//Parent
	public List<Integer> doSomething(List<Integer> param) {
		List<Integer> result;
		...
		if(ListUtils.isEmpty(result)) {	throw new ServiceException("result should not be empty list"); }
		if(ListUtils.isNotEmpty(param)) { throw new ServiceException("param list should be all consumed"); }
		return result;
	}
	
	//Child
	public List<Integer> doSomething(List<Integer> param) {
		List<Integer> result;
		...
		if(result == null) { throw new ServiceException("result should not be empty list"); }		//후행조건 약화 불가, 리스코프 치환원칙 위배
		return result;
	}
	```
6. 하위형에서 상위형의 불변 조건은 반드시 유지되어야 한다.
	* 부모클래스의 모든 메서드에서 지켜지고있는 멤버필드의 불변조건은 자식클래스에서 확장된 메서드에서도 지켜져야함
	```java
	//Parent
	private String date;		//yyyy-MM-dd

	public void calcDate(Date date, int modifyDays) {
		Date modifiedDate = DateUtils.addDay(date, modifyDays);
		this.date = new SimpleDateFormat("yyyy-MM-dd").format(modifiedDate);
	}

	//Child
	public void calcDate(Date date, int modifyDays) {
		Date modifiedDate = DateUtils.addDay(date, modifyDays);
		this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(modifiedDate);	//부모클래스 불변조건(date 멤버필드 포맷) 위배.
	}
	```
	
<br>

# 인터페이스 분리 원칙
* 클라이언트는 자신이 사용하지 않는 불필요한 인터페이스 멤버에 의존성을 가지면 안된다는 원칙
* 하나의 인터페이스는 하나의 동작만 하도록 인터페이스를 분리 (인터페이스 버전 단일책임원칙 느낌)

### 인터페이스 분리 원칙 위반 예시
1. 데이터 read, write 관련 기능을 선언한 DataManager 인터페이스 정의 
```java
public interface DataManager {
	int load();
	void prepare();
	void save(int data);
}
```

2. DataManager 인터페이스를 구현하여 데이터 read 기능을 수행하는 DataLoader 클래스 정의
```java
public class DataLoader implements DataManager {
	public int load() {
		//do Something
	}
	
	public void prepare(){}
	
	public void save(int data){}
}
```

3. DataLoader 클래스는 데이터 read 기능만을 담당하므로 load 메서드만 필요하나, DataManager 인터페이스에 여러 책임이 혼재되어있어 필요하지 않은 나머지 메서드도 구현 강제 
	* 불필요한 코드 발생, 코드 복잡도 증가, 불필요한 인터페이스 멤버에 의존성 생성
	* DataManager 의 save 인터페이스 수정시, 저장 기능과는 관련없는 DataLoader 클래스도 수정 강제

### 인터페이스 분리를 통한 수정
1. 하나의 인터페이스가 하나의 기능만 담당하도록 인터페이스 분리
```java
public interface DataLoader {
	int load();
}

public interface DataPreparer {
	void prepare();
}

public interface DataSaver {
	void save(int data);
}
```

2. 분리한 인터페이스 각각을 상속받아 구현하는 구현체 정의
```java
public class DataLoaderImpl implements DataLoader {
	public int load() { ... }
}

public class DataPreparerImpl implements DataPreparer {
	public void prepare() { ... }
}

public class DataSaverImpl implements DataSaver {
	public void save(int data) { ... }
}
```

3. 필요에 따라, 분리된 2개 이상의 인터페이스가 하나의 기능을 위해 필요할시, 다중 구현을 통해 해결 
```java
public class DataIncrementWriter implements DataLoader, DataSaver {
	public int load() { ... }
	
	public void save(int data) { ... }
	
	public void monthlyDataIncrese() {
		if(LocalDate.now().getDayOfMonth() == 1) {
			int data = load();
			save(data + 1);
		}
	}
}
```

### 단점
* 인터페이스를 너무 작은 기능단위까지 분리할경우, 코드 가독성도 떨어지고 복잡성이 올라가며, 구현하는데에도 어려움
	> 인터페이스 분리 원칙도 최대한 준수하면서 코드 복잡도도 올라가지 않는 적당한 인터페이스 분리 정도 설정하는것이 중요
	

<br>

# 의존관계역전원칙
* 다음 설명으로 대체한다.
1. [제어 역전](https://github.com/JisooOh94/study/blob/master/%EC%A0%84%EB%AC%B8%EA%B0%80%EB%A5%BC%20%EC%9C%84%ED%95%9C%20%EC%8A%A4%ED%94%84%EB%A7%815/1.%20Spring%20%EA%B0%9C%EC%9A%94.md#%EC%A0%9C%EC%96%B4-%EC%97%AD%EC%A0%84inversion-of-control)
2. [Spring IoC](https://github.com/JisooOh94/study/blob/master/%EC%A0%84%EB%AC%B8%EA%B0%80%EB%A5%BC%20%EC%9C%84%ED%95%9C%20%EC%8A%A4%ED%94%84%EB%A7%815/3.%20Spring%20IoC.md)
3. [Spring 싱글톤 패턴](https://github.com/JisooOh94/study/blob/master/%EC%A0%84%EB%AC%B8%EA%B0%80%EB%A5%BC%20%EC%9C%84%ED%95%9C%20%EC%8A%A4%ED%94%84%EB%A7%815/3.6%20%EC%8B%B1%EA%B8%80%ED%86%A4%20%ED%8C%A8%ED%84%B4.md)