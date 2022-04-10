# Factory Method
* 객체 Factory 클래스에 template method 패턴을 적용한것
* 객체 생성을 위한 템플릿은 부모 클래스에 정의해두고, 구체적은 생성 로직은 자식 클래스에서 상속받아 구현하는것
```java
public abstract class NumberPlateFactory {
    public final NumberPlate create(String address) {
        NumberPlate numberPlate = createNumberPlate(address);
        validateNumberPlate(numberPlate);
        registNumberPlate(numberPlate);
        return numberPlate;
    }
    
    protected abstract NumberPlate createNumberPlate(String address);
    protected abstract void registNumberPlate(NumberPlate numberPlate);

    private void validateNumberPlate(NumberPlate numberPlate) {
        //validate numberplate duplication, owner, etc
    }
}

public class PassengerCarNumberPlateFactory extends NumberPlateFactory {
    protected NumberPlate createNumberPlate(String address) { ... }
    protected registNumberPlate(NumberPlate numberPlate) { ... }
}

public class ElectricCarNumberPlateFactory extends NumberPlateFactory {
    protected NumberPlate createNumberPlate(String address) { ... }
    protected registNumberPlate(NumberPlate numberPlate) { ... }
}
```
* 기존 Factory 클래스와 다르게 new 를 통한 Conreate class 생성 코드를 없앰으로서, Factory 클래스의 생성 클래스에 대한 의존성을 해소하고 자유롭게 확장 가능
```java
//AS-IS
public class NumberPlateFactory {
    public NumberPlate create(String address) {
        NumberPlate numberPlate = new PassengerCarNumberPlate(address);
        ...
    }
}
```