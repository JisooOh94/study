# Proxy
* 주체 클래스의 일부 기능을 대신 수행해주거나 보조하는(AOP) 프록시 클래스를 두어 주체클래스의 기능을 확장하거나 효율성을 증대시키는 패턴
* 프록시 클래스는 주체 클래스의 인터페이스를 구현하여 사용부에서 주체클래스 필드에 대신 주입되어 사용
    * 클라이언트는 기능이 프록시 객체에서 수행된건지, 주체 클래스 객체에서 수행된건지 관심을 가지지 않음
* 프록시 클래스는 주체 클래스 필드를 가지어, 대신 수행할 수 없는 기능 호출시, 주체 클래스에게 수행 위임
* 주체 클래스 기능 호출 전후에 별도의 보조 로직 추가 가능

### Proxy 패턴 종류
* 가상 프록시
    * 주체 클래스 객체 생성 비용이 클 경우, 프록시 클래스에서 대신 수행할 수 있는 기능을 수행하고, 주체 클래스에서 수행해야하는 기능이 호출된 시점에 주체 클래스 객체를 생성하여 수행 위임
    * 주체 클래스 객체 lazy-initailizing 을 통해 어플리케이션 구동 비용 절감
  > 서비스 장비가 여러대이고, 배포시점의 트래픽이 전체 장비로 감당하지 못할 수준이 아니라면 런타임시점에 initailizing 을 하기보다 서버 구동시점에 하는것이 더 효율적으로 보임
```java
public interface Foo {
    void doHeavy();
    void doLight();
}

public class FooImpl implements Foo {
    public void doHeavy() {
        //do something heavy...        
    }
    
    public void doLight() {
        //do something light...
    }
}

public class FooProxy implements Foo {
    private FooImpl fooImpl;
    
    public void doHeavy() {
        generate();
        fooImpl.doHeavy();
    }
  
    public void doLight() {
        //do somehting light....
    }
    
    private synchronized void generate() {
        if(fooImpl == null) {
            fooImpl = new FooImpl();
        }
    }
}

public static void main(String[] args) {
    Foo foo = new FooProxy();
    foo.doLight();
    foo.doHeavy();
}
```
* 원격 프록시
    * 분산 컴퓨팅 환경에서 타 장비에 로드되어있는 객체의 api 를 원격으로 호출해주는 프록시 클래스 (e.g. Java RMI)
* Access 프록시
    * 호출자별로 주체클래스 기능 호출 제한을 적용할 수 있는 프록시 클래스 (일종의 포인트컷과 유사)