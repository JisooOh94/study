# Template Method
* 부모 클래스에서 비즈니스 로직의 뼈대를 결정하고, 자식 클래스에서 메인 비즈니스 로직을 구현하는 디자인 패턴
* 부모 클래스에는 추상메서드 + 템플릿 메서드로 구성
    * 추상메서드 : 자식 클래스에서 구현해야하는 메인 비즈니스 로직
    * 템플릿메서드 : 추상 메서드를 사용하는 나머지 비즈니스 로직

```java
public abstract class TaxCalculator {
    private static final BigDecimal VAT_RATE = new BigDecimal(0.01);
    
    protected abstract BigDecimal getTax(BigDecimal price);

    public BigDecimal getTaxDeductedPrice(String price) {
        BigDecimal priceInBigDecimal = new BigDecimal(price);
        BigDecimal tax = getTax(priceInBigDecimal);
        BigDecimal vat = priceInBigDecimal.multiply(VAT_RATE);
        
        tax.add(vat);
        
        return priceInBigDecimal.subtract(tax);
    }
}

public class PassengerCarTaxCalculator extends TaxCalculator {
    private static final BigDecimal ICT_RATE = new BigDecimal(0.01);
    private static final BigDecimal ACQUISITION_TAX_RATE = new BigDecimal(0.01);
    private static final BigDecimal REGISTRATION_TAX_RATE = new BigDecimal(0.01);
    
    protected BigDecimal getTax(BigDecimal price) {
        BigDecimal ict = price.multiply(ICT_RATE);
        BigDecimal acquisitionTax = price.multiply(ACQUISITION_TAX_RATE);
        BigDecimal registrationTax = price.multiply(REGISTRATION_TAX_RATE);
        
        return ict.add(acquisitionTax.add(registrationTax));
    }
}

public class CorporateCarTaxCalculator extends TaxCalculator {
    private static final BigDecimal VAT_RATE = new BigDecimal(0.01);
    
    protected BigDecimal getTax(BigDecimal price) {
        
    }
}
```
* 하위 클래스의 추상메서드는 부모클래스의 템플릿 메서드에 의존성이 있으므로 구현/수정시 템플릿 메서드 비즈니스 로직에 유의
* 공통 로직을 부모클래스에 정의해둠으로서 중복 코드 제거 및 관리 용이성 증대