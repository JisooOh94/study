# Objects.requireNonNull
* Object의 null check 수행하여 NPE 던짐
```java
public class Foo {
  private final Bar bar;

  public Foo(Bar bar) {
    if(bar == null) {
	    throw new InvalidArgumentException();
    }
    this.bar = bar;
  }
}

public class Foo {
  private final Bar bar;

  public Foo(Bar bar) {
    this.bar = Objects.requireNonNull(bar);	//bar==null 이면 NPE 발생
  }
```
* 코드 가독성 향상 외에는 ```if(obj==null)```을 통한 check 와 비교하여 큰 장점이 없는듯 보이나 그외에 몇가지 장점 더 존재.

### 1. 다양한 확장 메서드 제공
* 단순히 NPE 예외를 던지는 것 외에 보조적인 기능을 추가한 오버로딩 메서드 제공
	1) Objects.requireNonNull(Object obj, String debugMessage);
	NPE throw 시 NPE에 debugMessage 설정하여 throw (Stacktrace에 출력)
	2) Objects.requireNonNull(Object obj, Object defaultValue);
	obj 가 null 일시 defaultValue 반환
	3) Objects.requireNonNull(Object obj, Supplier<T> supplier);
	obj 가 null 일시 lamda 식 수행 결과 반환

### 2. 빠른 실패(Fail-Fast)
* NPE 가 null 객체 할당시에 바로 발생하지 않고 객체가 사용될때 발생되면 그 사이에 null 객체가 시스템의 다른부분에 어떤 영향을 끼칠지 알 수 없어 불변성을 깨뜨릴 위험 존재
* null 객체 할당시에 바로 NPE 를 발생시켜 시스템이 비정상적인 상태에 빠진채로 있는 시간을 없애야 함

### 3. 디버깅에 용이
* NPE 를 객체가 사용될때가 아닌, 할당 or 생성할때 즉시 발생하므로 에러 로그를 통해 NPE 가 발생한 위치를 찾기가 더 쉽다.
```java
public class Dictionary {
    private final List<String> words;
    private final LookupService lookupService;

    public Dictionary(List<String> words) {
        this.words = this.words;
        this.lookupService = new LookupService(words);
    }

    public boolean isFirstElement(String userData) {
        return lookupService.isFirstElement(userData);
    }        
}

public class LookupService {
    List<String> words;

    public LookupService(List<String> words) {
        this.words = words;
    }

    public boolean isFirstElement(String userData) {
        return words.get(0).contains(userData);
    }
}

public static void main(String[] args) {
	Dictionary dictionary = new Dictionary(null); 
	boolean isFirstElement = dictionary.isFirstElement("anyThing");
}

//에러로그
//실제 NPE가 발생한 원인인 Dictionary 생성자가 아닌, 엉뚱한 메서드가 에러로그에 표시됨
Exception in thread "main" java.lang.NullPointerException
    at LookupService.isFirstElement(LookupService.java:5)
    at Dictionary.isFirstElement(Dictionary.java:15)
    at Dictionary.main(Dictionary.java:22)
//Dictionary 의 word 멤버변수 할당시 Objects.requireNonNull 사용하면 Dictionary 생성자가 에러로그에 남음
Exception in thread "main" java.lang.NullPointerException
    at java.util.Objects.requireNonNull(Objects.java:203)
    at com.Dictionary.(Dictionary.java:15)
    at com.Dictionary.main(Dictionary.java:24)
```
### 4. Not Null 보장
* 객체 할당시 null-check 를 함께 수행하면 해당 객체가 not null 임이 보장되어 동일한 객체를 사용하는 다른 코드에서 더이상 null-check을 수행할 필요가 없어짐