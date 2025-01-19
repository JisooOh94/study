# Adapter
* 이미 개발되어있는 클래스를 이용하여 다른 기능을 하는 새로운 인터페이스를 구현하는것
```java
public class PreciseCalculator {
    public BigDecimal sum(String source, String target) { ... }
    public BigDecimal sub(String source, String target) { ... }
    public BigDecimal mult(String source, String target) { ... }
    public BigDecimal divide(String source, String target) { ... }
}

public interface TaxCalculator {
    BigDecimal getPrivateBusinessTax(String price);
    BigDecimal getCorporateBusinessTax(String price);
}
```
* 기존 클래스를 이용하여 새로운 인터페이스를 구현한 구현체가 adapter
* 기존 클래스 사용시, 상속을 이용한 방법 / HAS-A 를 이용한 방법 2가지 존재

### 장점
* 재사용성 증가
  * 버그 발생시, 기존 클래스 사용부를 제외한 나머지 부분만 확인하면 되므로 디버깅에 용이
  * 처음부터 모두 새로 개발하는것이 아닌, 기존에 존재하는 클래스를 그대로 활용하여 개발하므로 개발 리소스 절약 가능

### 단점
* 복잡성 증가
  * 시스템의 복잡성이 증가. 특히, 많은 수의 Adapter가 필요한 경우 관리가 어려워짐
* 성능 오버헤드
  * 추가적인 메서드 호출이 필요하므로, 성능 오버헤드가 발생 가능

```java
//상속을 이용한 방법
public class TaxCalculatorImpl extends PreciseCalculator implements TaxCalculator {
    private static final String PRIVATE_BUSINESS_TAX_RATE = "0.033";
    private static final String CORPORATE_BUSINESS_TAX_RATE = "0.01";
    
    public BigDecimal getPrivateBusinessTax(String price) {
        return mult(price, PRIVATE_BUSINESS_TAX_RATE);    
    };
    
    public BigDecimal getCorporateBusinessTax(String price) {
        return mult(price, CORPORATE_BUSINESS_TAX_RATE);
    }
}

//HAS-A를 이용한 방법
public class TaxCalculatorImpl implements TaxCalculator {
    private static final String PRIVATE_BUSINESS_TAX_RATE = "0.033";
    private static final String CORPORATE_BUSINESS_TAX_RATE = "0.01";
    private PreciseCalculator calculator;
    
    public TaxCalculatorImpl() {
        this.calculator = new PreciseCalculaotr();
    }

    public BigDecimal getPrivateBusinessTax(String price) {
        return calculator.mult(price, PRIVATE_BUSINESS_TAX_RATE);
    };

    public BigDecimal getCorporateBusinessTax(String price) {
        return calculator.mult(price, CORPORATE_BUSINESS_TAX_RATE);
    }
}
```
