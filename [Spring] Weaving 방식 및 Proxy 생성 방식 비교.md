# Runtime Weaving
* 프록시 객체를 통한 런타임 시점 위빙
* 런타임 시점에, 프록시 객체 횡단관심사 수행 > 원본 클래스 메서드 호출 이라는 2번의 과정으로 횡단관심사가 수행되므로 성능 떨어짐
* 프록시 객체 생성 방법으로 JDK Proxy, CGLIB Proxy 2가지가 있으나, 제약사항이 더 적고 성능도 뛰어난 CGLIB 프록시 사용이 권장됨 
	* 기존엔 CGLIB 프록시의 몇가지 단점으로 인해 성능이 더 뛰어남에도 JDK Proxy가 spring default proxy로 채택
	* Spring 4.3/SpringBoot 1.4 버전부터 CGLIB 의 단점들이 해결되어 CGLIB 프록시가 spring default proxy 로 채택 

### JDK Proxy
* 인터페이스 기반 프록시 객체 생성, 따라서 인터페이스를 상속받은 클래스에만 사용 가능
* 프록시 객체에서 횡단관심사 수행후, 원본 클래스 메서드 호출시, java relfection 을 이용하여 호출하므로 성능저하 큼

### CGLIB Proxy
* 상속 기반 프록시 객체 생성, 따라서 인터페이스를 상속받지 않은 일반 클래스에도 사용가능하나 상속할 수 없는 final 클래스나 메서드에는 사용 불가능
* 원본 클래스를 프록시 객체가 상속받으므로, 프록시 객체에서 횡단관심사 수행후, 원본 메서드 호출시 super.method만 호출하면되어 성능저하 없음
* [JIT Complier 동작방식](https://github.com/JisooOh94/study/blob/master/JAVA%EC%9D%98%20%EC%A0%95%EC%84%9D/Content/1.%20JVM.md#jit-%EC%BB%B4%ED%8C%8C%EC%9D%BC%EB%9F%AC)에 따라 JVM 이 원본 클래스 메서드 호출시, [클래스 로더](https://github.com/JisooOh94/study/blob/master/%5BJava%5D%20JVM%20Internal.md#%ED%81%B4%EB%9E%98%EC%8A%A4%EB%A1%9C%EB%8D%94)가 원본 클래스의 바이트코드를 읽어 로드 후, 자신의 namespace 에 캐싱
* 이때, GCLIB 을 통해 바이트코드를 조작하여 원본 클래스 바이트코드에 횡단관심사 로직을 위빙한 프록시 객체 바이트코드를 로드 및 캐싱
* 이후, JVM 이 다시 원본클래스 메서드 호출시, 클래스 로더 네임스페이스에 캐싱되어있는 위빙이 수행된 바이트코드를 로드하여 재사용하므로 성능이 뛰어남

<br>

# ComplieTime Weaving
* AspectJ 를 이용한 AOP
* 컴파일단계에서 클래스 메서드 코드에 직접 횡단관심사 로직을 위빙하여 바이트코드를 생성
* 런타임에서의 추가작업(프록시 객체 생성 및 호출, 원본 클래스 메서드 호출등) 없으므로 성능상 가장 뛰어남
* Java 컴파일러가 아닌, AspectJ 에서 제공하는 컴파일러나, 디버거를 사용해야하므로 몇가지 제약사항 존재

***
> Reference
> * https://gmoon92.github.io/spring/aop/2019/04/20/jdk-dynamic-proxy-and-cglib.html
> * https://huisam.tistory.com/entry/springAOP
> * https://www.baeldung.com/spring-aop-vs-aspectj