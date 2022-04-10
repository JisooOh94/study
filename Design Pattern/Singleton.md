# Singleton
* 하나의 인스턴스만을 생성 후, 프로그램 전반걸쳐 이를 공유하여 사용하는 패턴
* 클래스 정의시, 생성자를 private으로 감춰 하나의 인스턴스만이 생성되도록 보장

### 정의
1. 클래스 밖에서는 오브젝트를 생성하지 못하도록 생성자를 private으로 선언.
2. 생성된 싱글톤 인스턴스를 저장할 static 멤버필드 선언
3. static 팩토리 메소드인 getInstance() 정의
* getInstance 메소드가 최초로 호출되는 시점에 싱글톤 인스턴스 생성하여 static 멤버필드에 저장(클래스 초기화와 함께 static 멤버필드에 인스턴스 생성 및 할당 가능하나 lazy loading 방식이 더 효율적)
* 최초 호출 이우  getInstance 에서 static 멤버필드에 저장되어있는 인스턴스 반환
* 호출부에선 getInstance 를 통해 인스턴스 획득 후 사용

```java
public class Foo {
	private static Foo foo;
	
	private Foo() {}
	
	public static Foo getInstance() {
		if(foo == null) {
			foo = new Foo();
		}
		return foo;
	}
}
```

### 장점
* 전역 객체이므로 코드 어느곳에서나 간편하게 접근하여 사용 가능
* 인스턴스를 한번만 생성후 재사용하므로, 객체 사용시 생성시간이 들지 않아 시간 절약 가능
* 인스턴스를 하나만 생성후 재사용하므로, 메모리 및 리소스 절약 가능

### 단점
* 객체지향 설계 원칙 위배
    * 생성자의 접근제한자가 private 이기때문에 상속 불가능
        * 클래스 확장을 통한 다형성 제공 불가능. 개방폐쇄원칙의 개방원칙 위배
    * 싱글톤 객체 사용코드와 싱글톤 클래스사이에 의존성 생성
        * 사용코드에서 싱글톤 클래스의 getInstance 메서드를 직접 호출하여 싱글톤 객체 획득
        * 싱글톤 클래스 내부 코드 수정시, 싱글톤 객체를 사용한 모든 코드가 영향 받음
            * 싱글톤 객체는 전역 객체이므로 영향받는 코드의 범위가 광범위
        * 따라서 개방폐쇄원칙의 폐쇄원칙 위배
* 테스트 코드 작성이 어려움
    * 싱글톤 클래스 내부에서 생성된 객체를 getInstance static 메서드로 받아쓰는 형식이기때문에 싱글톤 객체를 Mock 객체로 교체할 수 없음 (PowerMock 등을 통한 getInstace 메서드 Mocking시 가능)
* 싱글톤 객체의 단일 생성을 보장해주지 못함
    * reflection 을 통한 private 생성자 호출등의 방법으로 1개 이상의 객체 생성 가능
    * lazy loading 사용시, 싱글턴 객체가 생성되지 않은 상태에서 여러 스레드가 동시에 getInstance 메서드 호출하면 여러 인스턴스가 생성됨
        * getInstance 내의 객체 생성 여부 판단 조건문을 임계영역으로 설정하여 해결 가능(Double Check Locking)
        * 싱글턴 필드를 volatile 로 선언하여 배타적 실행 및 코드 재배치 방지
```java
//Double Check Locking
public static Foo getInstance() {     
	if(foo == null) {                  
		synchronized (this.getClass()) {    
			if(foo == null) 
				foo = new Foo();
		}
	}
	return foo;                       
}
```
* 동기화 이슈
    * 싱글톤 클래스에 멤버필드 선언시(상태), 전역객체이므로 여러 스레드간 동기화 이슈 발생
* 메모리 낭비
    * 싱글톤 객체는 static 객체이므로 GC 되지 않고, 프로그램이 종료될떄까지 메모리에 계속 상주
    * 더이상 사용되지 않는 싱글턴 객체는 메모리 낭비 유발

### Spring 싱글톤
* 객체 재사용을 통한 공간적(메모리), 시간적(응답시간) 리소스를 절약하기 위해 Spring 에서도 객체 생성시, 싱글톤으로 생성
    * 서버로 클라이언트 요청이 들어올때마다 요청 처리 객체들(Controller, Bo, Dao 등) 을 새로 생성하여 처리할시, 서버 시스템 및 메모리에 큰 부하 발생, 장애 유발
* 여러 단점들로 인해 안티패턴으로 지정된 싱글톤 패턴 대신 Spring 에서 자체적으로 정의한 Spring Registry 를 이용해 싱글톤 구현

#### Spring Registry
* Ioc 컨테이너에서 클래스 인스턴스 생성 후, 필요한곳에 주입하여 재사용함으로서 단일 인스턴스 생성을 보장하는것
* private 생성자 + getInstance static 메소드의 싱글톤 클래스가 아닌, public 생성자를 가지는 일반 클래스를 싱글톤으로 활용할 수 있게 해줌
* 클래스 객체 생성 및 관리를 Ioc 컨테이너가 담당하므로 public 생성자를 가지고있어도 인스턴스가 1개만 생성됨을 보장
* 기존방식
    * 의존객체에서 협력객체 인스턴스를 직접 생성하여 사용
    * 따라서 인스턴스가 1개만 생성됨을 보장하기 위해선 private 생성자등의 제한장치 필요
* Spring 방식
    * Spring에서 협력객체 인스턴스들을 생성하여 Bean Conainer 에 모아두고 의존객체에서 요구하는 협력객체 인스턴스를 주입해줌
    * 따라서 의존객체에서 협력객체 생성자를 호출할 일이 없으므로 public 생성자임에도 단일 인스턴스 보장 가능