# 상속(IS-A, Inheritance)
### 장점
* 상위 클래스의 기능을 하위 클래스가 물려받아 사용하므로 코드 재사용이 용이
* 부모-자식 클래스 간 강결합을 통해 때에따라 단단한 클래스 구조 구축 및 다형성 구현

### 단점
- 부모-자식 클래스 간 강결합 초래
  - 부모클래스 변경 시 자식클래스에도 영향을 주어 의도치 않은 버그 발생 가능
  - 부모클래스의 기능을 자식클래스에서 불필요하게 상속해야 할 수 있음

### Use-case
* is 관계가 확실할때 사용
  - 사람은 인간이다.
  - 고양이는 동물이다.
  - 게임 캐릭터는 엔티티이다.

```java
abstract class Animal {
  // 추상 메서드를 사용하여 서브클래스에서 구현하도록 강제
  public abstract void emitSound();
}

class Cat extends Animal {
  // Cat 클래스에서 emitSound 메서드를 오버라이드
  @Override
  public void emitSound() {
    System.out.println("Meow");
  }
}

class Dog extends Animal {
  // Dog 클래스에서 emitSound 메서드를 오버라이드
  @Override
  public void emitSound() {
    System.out.println("The dog emitted a sound");
  }
}

public class Main {
  public static void main(String[] args) {
    Animal cat = new Cat(); // Cat 객체 생성
    Animal dog = new Dog(); // Dog 객체 생성

    cat.emitSound();   // Outputs: Meow
    dog.emitSound();   // Outputs: The dog emitted a sound
  }
}
```

# 구성(HAS-A, Composition)
### 장점
- 클래스간 느슨한 결합을 통해 높은 유연성 제공
  - 한 클래스의 구현을 변경해도 다른 클래스에 영향 없음. 그에따라 유지보수에 용이
  - 런타임에도 유연하게 구현체 변경 가능
  - 다중 기능 조합에 용이

### 단점
- 상속에 비해 초기 구현 복잡.

### Use-case
- IS-A 관계가 불확실할 때 HAS-A 관계를 고려. (가급적 has-a 사용)
- 하나의 객체가 다른 객체를 "(부분으로써) 포함" 하는 경우에 사용
  - 자동차는 배터리를 가지고 있다.
  - 사람은 심장을 가지고 있다.
  - 전투기는 HUD를 가지고 있다.

```java
class Engine {
  public void start() {
    System.out.println("Engine started");
  }
}

class Car {
  private Engine engine; // Car has-a Engine

  public Car() {
    this.engine = new Engine();
  }

  public void startCar() {
    engine.start();
    System.out.println("Car started");
  }
}

public class Main {
  public static void main(String[] args) {
    Car car = new Car(); // Car 객체 생성
    car.startCar();      // Outputs: Engine started, Car started
  }
}
```

# HAS-A > IS-A
* 유연성: HAS-A은 객체 간의 결합도를 낮추어 더 유연한 설계 가능
* 변경 용이성: HAS-A을 사용하면 클래스의 내부 구현을 변경해도 외부에 영향을 미치지 않으므로, 유지보수에 용이해짐
* 다중 기능 조합: 여러 객체를 조합하여 새로운 기능을 만들 수 있으며, 이는 IS-A보다 더 자연스럽고 직관적

### 예시
* IS-A 로 작성된 기존 코드 
```java
// Superclass
class Vehicle {
    private int speed;

    public void accelerate() {
        speed += 10;
        System.out.println("Accelerating vehicle. Speed is now: " + speed);
    }

    public void brake() {
        speed -= 10;
        System.out.println("Braking vehicle. Speed is now: " + speed);
    }
}

// Subclass
class ElectricCar extends Vehicle {
    private int batteryLevel = 100;

    public void chargeBattery() {
        batteryLevel = 100;
        System.out.println("Battery fully charged.");
    }
}

public class Main {
    public static void main(String[] args) {
        ElectricCar tesla = new ElectricCar();
        tesla.accelerate();  // Outputs: Accelerating vehicle. Speed is now: 10
        tesla.brake();       // Outputs: Braking vehicle. Speed is now: 0
        tesla.chargeBattery(); // Outputs: Battery fully charged.
    }
}
```

* 문제 발생
  * Vehicle 클래스의 변경: Vehicle 클래스에 새로운 기능이나 수정이 발생하면, ElectricCar 클래스에도 영향을 미침. 예를 들어, accelerate 메서드에 연료 소모 로직이 추가되면 전기차에는 맞지 않는 로직이 적용
  * 부적절한 기능 상속: ElectricCar는 fuelLevel을 사용하지 않지만, Vehicle의 변경으로 인해 연료 관련 로직이 상속

```java

// Superclass with a change
class Vehicle {
    private int speed;
    private int fuelLevel = 50; // New attribute for fuel level

    public void accelerate() {
        if (fuelLevel > 0) {
            speed += 10;
            fuelLevel -= 5; // Decrease fuel level
            System.out.println("Accelerating vehicle. Speed is now: " + speed);
        } else {
            System.out.println("Cannot accelerate, fuel is empty.");
        }
    }

    public void brake() {
        speed -= 10;
        System.out.println("Braking vehicle. Speed is now: " + speed);
    }
}
```

* HAS-A 으로 개선 
  * 유연성: ElectricCar 클래스는 Vehicle의 변경에 영향을 받지 않음. 필요한 기능만을 선택적으로 포함 가능
  * 명확한 책임 분리: Engine 인터페이스를 사용하여 엔진 동작을 분리함으로써, 각 클래스의 책임이 명확해짐 
  * 확장 용이성: ElectricCar의 기능을 확장하거나 변경할 때, 다른 클래스에 영향을 주지 않고 변경 용이

```java
// Interface for engine behavior
interface Engine {
    void accelerate();
    void brake();
}

// Implementation for electric engine behavior
class ElectricEngine implements Engine {
    private int speed = 0;

    @Override
    public void accelerate() {
        speed += 10;
        System.out.println("Accelerating electric car. Speed is now: " + speed);
    }

    @Override
    public void brake() {
        speed -= 10;
        System.out.println("Braking electric car. Speed is now: " + speed);
    }
}

// Class that represents an electric car
class ElectricCar {
    private Engine engine;
    private int batteryLevel = 100;

    public ElectricCar(Engine engine) {
        this.engine = engine;
    }

    public void chargeBattery() {
        batteryLevel = 100;
        System.out.println("Battery fully charged.");
    }

    public void accelerate() {
        engine.accelerate();
    }

    public void brake() {
        engine.brake();
    }
}

public class Main {
    public static void main(String[] args) {
        ElectricCar tesla = new ElectricCar(new ElectricEngine());
        tesla.accelerate();  // Outputs: Accelerating electric car. Speed is now: 10
        tesla.brake();       // Outputs: Braking electric car. Speed is now: 0
        tesla.chargeBattery(); // Outputs: Battery fully charged.
    }
}
```

> Reference
> * https://minusi.tistory.com/entry/%EA%B0%9D%EC%B2%B4-%EC%A7%80%ED%96%A5%EC%A0%81-%EA%B4%80%EC%A0%90%EC%97%90%EC%84%9C%EC%9D%98-has-a%EC%99%80-is-a-%EC%B0%A8%EC%9D%B4%EC%A0%90
