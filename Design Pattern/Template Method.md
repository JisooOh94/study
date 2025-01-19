# Template Method
* 부모 클래스에서 비즈니스 로직의 뼈대를 결정하고, 자식 클래스에서 메인 비즈니스 로직을 구현하는 디자인 패턴
* 부모 클래스에는 추상메서드 + 템플릿 메서드로 구성
    * 추상메서드 : 자식 클래스에서 구현해야하는 메인 비즈니스 로직
    * 템플릿메서드 : 추상 메서드를 사용하는 나머지 비즈니스 로직

### 장점
* 코드 재사용성 증가
  * 알고리즘의 공통 부분을 상위 클래스에 정의하여 코드 중복을 줄이고, 재사용성 향상
* 확장 용이성
  * 하위 클래스에서 특정 단계만 재정의하여 알고리즘을 확장 가능. 이를 통해 새로운 기능을 쉽게 추가

### 단점
* 결합도 증가 
  * 상속을 사용하므로, 클래스 간의 의존성 형성 및 결합도가 높아짐

### 예시

```java
abstract class CaffeineBeverage {

  // 템플릿 메서드
  public final void prepareRecipe() {
    boilWater();
    brew();
    pourInCup();
    addCondiments();
  }

  protected abstract void brew();
  protected abstract void addCondiments();

  private void boilWater() {
    System.out.println("Boiling water");
  }

  private void pourInCup() {
    System.out.println("Pouring into cup");
  }
}

class Coffee extends CaffeineBeverage {
  @Override
  protected void brew() {
    System.out.println("Dripping Coffee through filter");
  }

  @Override
  protected void addCondiments() {
    System.out.println("Adding Sugar and Milk");
  }
}

class Tea extends CaffeineBeverage {
  @Override
  protected void brew() {
    System.out.println("Steeping the tea");
  }

  @Override
  protected void addCondiments() {
    System.out.println("Adding Lemon");
  }
}

public class TemplateMethodPatternDemo {
  public static void main(String[] args) {
    CaffeineBeverage coffee = new Coffee();
    coffee.prepareRecipe();

    System.out.println();

    CaffeineBeverage tea = new Tea();
    tea.prepareRecipe();
  }
}
```
