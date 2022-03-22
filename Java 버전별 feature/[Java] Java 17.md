# Java 17
```
Restore Always-Strict Floating-Point Semantics
Enhanced Pseudo-Random Number Generators
New macOS Rendering Pipeline
macOS/AArch64 Port
Deprecate the Applet API for Removal
Strongly Encapsulate JDK Internals
Pattern Matching for switch (Preview)
Remove RMI Activation
Sealed Classes
Remove the Experimental AOT and JIT Compiler
Deprecate the Security Manager for Removal
Foreign Function & Memory API (Incubator)
Vector API (Second Incubator)
Context-Specific Deserialization Filters
```

### Restore Always-Strict Floating-Point Semantics [[JEP 306]](https://openjdk.java.net/jeps/306)
* 항상 엄격한 부동 소수점 체계 복원
* 엄격한 부동 소수점 체계(strict floating-point semantic)와 기본 부동 소수점 체계(default floating-point semantic)를 병행 사용하지 않고 Java VM에서 일관되게 엄격한 부동 소수점 체계를 사용하도록 변경

> 1990년대 후반에 Java SE 1.2 에서 default floating-point semantic을 변경하게 된 동기는 원래 java language와 JVM semantic 간 잘못된 상호 작용과 인기 있는 x86 아키텍처의 x87 floting-point co-processor instruction set의 일부 특성에서 비롯되었다.
> 비정상 피연산자 및 결과를 포함하여 모든 경우에 정확한 floating-point semantic을 일치시키려면 추가 명령어의 큰 overhead가 필요하였다.
> overflow 또는 underflow가 없을 때 결과를 일치시키는 것은 더 적은 overhead로 수행될 수 있으며 이는 java SE 1.2에 도입된 default floating-point semantic에서 허용하는 것이다.
> 하지만 2001년경부터 펜티엄 4 이상 프로세서에 탑재된 SSE2(Streaming SIME Extension 2) 확장은 과도한 overhead 없이 간단한 방식으로 strict JVM floating-point 연산을 지원할 수 있게 되었다.

### Enhanced Pseudo-Random Number Generators [[JEP 356]](https://openjdk.java.net/jeps/356)
* 기존의 의사 난수 생성 클래스들 (Random, ThreadLocalRandom, SplittableRandom, SecureRandom)은 기능이 거의 비슷함에도, 인터페이스가 통일되지 않아 유연하게 구현체를 선택할 수 없었음.
  ![image](https://user-images.githubusercontent.com/48702893/159305958-5ee66d54-5fc9-4a4b-9dda-50b31528a9b6.png)
* 의사 난수 생성 클래스들의 인터페이스를 통일하여(java.util.random.RandomGenerator) 유연하게 인터페이스 구현체 선택 가능
  ![image](https://user-images.githubusercontent.com/48702893/159307827-1d144be1-e457-4bd1-a7ca-c08fa07e2c58.png)
```java
RandomGenerator generator;
generator = RandomGenerator.of("L128X1024MixRandom");
generator = RandomGenerator.of("SecureRandom");
```

* 의사 난수 생성 구현체 추가하여 용도에 맡게 선택 가능 (jumpable, leapable, stremable 등 속성에 따라..)
> Xoroshiro128PlusPlus
> Xoshiro256PlusPlus
> L128X1024MixRandom
> L128X128MixRandom
> L128X256MixRandom
> L32X64MixRandom
> L64X1024MixRandom
> L64X128MixRandom
> L64X128StarStarRandom
> L64X256MixRandom

> 추가된 구현체 성능 벤치마크
![image](https://user-images.githubusercontent.com/48702893/159309644-ea180249-fec8-451a-b26a-035009acced5.png)

### Strongly Encapsulate JDK Internals [[JEP 403]](https://openjdk.java.net/jeps/403)
* sun.misc.Unsafe와 같은 중요한 내부 API를 제외하고 JDK의 모든 내부 요소를 강력하게 캡슐화
* 이로 인해 reflection을 통한 private field 접근이 불가능해지므로, JDK 버전 변경 시 확인 필요
* 부득이하게 JDK 내부 private field 및 method 사용이 필요한경우, --add-opens VM option 을 통해, 사용할 모듈 패키지 명시
* 내부적으로 reflection 을 사용하는 라이브러리 및 기능(e.g. JDK Proxy, ReflectionTestUtils 등) 확인 필요 할듯 [[cglib jdk 17 compatibility issue]](https://github.com/cglib/cglib/issues/191)

### Sealed Classes [[JEP 409]](https://openjdk.java.net/jeps/409)
* 상속을 허용할 자식클래스를 직접 명시하는 클래스, 인터페이스
* 기존의 final 키워드, private 생성자등을 이용한 상속 제한 방법에 비해, 명시적으로 상속 범위를 지정할 수 있어 의도치 않은 상속을 통한 버그 방지 가능
* 'sealed' 를 통해 Sealed Classes 로 선언하고, 'permits' 에 상속을 허용할 자식 클래스 명시
 ```java
public sealed interface CarBrand permits Hyundai, Kia{ ... }
```

### Context-Specific Deserialization Filters [[JEP 415]](https://openjdk.java.net/jeps/415)
* Java 9 에 추가된, 역직렬화 허용/차단 클래스 필터의 사용성 개선