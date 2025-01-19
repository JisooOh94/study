# Factory Method
* 객체 Factory 클래스에 template method 패턴을 적용한것
* 객체 생성을 위한 템플릿은 부모 클래스에 정의해두고, 구체적은 생성 로직은 자식 클래스에서 상속받아 구현하는것

### 장점
* 코드의 확장성 증가
  * 객체 클래스가 추가되거나 삭제되어도 호출부나 부모클래스에 영향을 주지 않음. 자식클래스의 객체 생성 로직만 수정
* 코드의 결합도 감소 
  * 호출부에서 객체의 구체 클래스에 의존하지 않으므로, 결합도가 낮아짐
* 단일 책임 원칙(SRP) 준수 
  * 객체 생성 로직을 별도의 클래스나 메서드로 분리하여, 단일 책임 원칙 준수

### 단점
* 복잡성 증가

### 예시

```java
//부모클래스
abstract class PizzaStore {
    public Pizza orderPizza(String type) {
        Pizza pizza = createPizza(type);
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }

    protected abstract Pizza createPizza(String type);
}

//자식클래스
class SimplePizzaStore extends PizzaStore {
    protected Pizza createPizza(String type) {
        if (type.equals("cheese")) {
            return new CheesePizza();
        } else if (type.equals("pepperoni")) {
            return new PepperoniPizza();
        } else {
            return null;
        }
    }
}

//호출부
public static void main(String[] args) {
    PizzaStore pizzaStore = new SimplePizzaStore();
    
    Pizza cheesePizza = pizzaStore.orderPizza("cheese");
    Pizza pepperoniPizza = pizzaStore.orderPizza("pepperoni");
}
```

* 새로운 피자 종류 추가시, 부모클래스나 호출부의 수정 없이 자식클래스의 객체 생성 로직만 수정하면 됌

```java
class SimplePizzaStore extends PizzaStore {
    protected Pizza createPizza(String type) {
        if (type.equals("cheese")) {
            return new CheesePizza();
        } else if (type.equals("pepperoni")) {
            return new PepperoniPizza();
        } else if (type.equals("combination")) {
            return new CombinationPizza();
        } else {
            return null;
        }
    }
}
```
