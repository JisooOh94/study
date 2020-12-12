## 코드 작성
* 삼항 연산자 활용
* Object.toString() 보다는 String.valueOf(Object) 를 사용하여 NPE 방지
* String 변수 null check 시 StringUtils.isEmpty() 사용
* 리스트에 추가/삭제 작업이 빈번하면 LinkedList, 조회작업이 빈번하면 ArrayList 사용
* 레퍼런스 타입 변수는 auto boxing을 유발하여 성능을 저하시키므로 사용을 지양하고 반드시 사용해야한다면 null-check 필수 및 primitive 타입과 함께 사용되지 않도록 주의(auto boxing 발생, NPE 우려)
* 레퍼런스 타입 변수간의 비교연산자(>, <, ==, !=) 수행시, 메모리 주소값으로 비교수행
* Controller에 요청파라미터가 많을 시, 별도의 파라미터용 클래스로 정의하여 가독성을 높히고 @valid 어노테이션을 이용하여 validation 수행
* MVC 별 파라미터 validation 정도
    * Controller : 기본적인 null-check, 날짜 포맷 검사 등(@valid 어노테이션 활용)
    * Bo : 데이터 존재 유무, 데이터 형식 검사 등의 상세한 validation
* 의미를 가지고 있는 값들은 모두 enum 객체 나 정적 변수로 만들어 사용(상수로 직접 사용 x)
* Map 에서 데이터 조회시 MapUtils 사용하여 NPE 방지 및 보조 기능 활용(e.g. default value)
* try-catch-finally 보다는 try-with-resources 사용하여 코드 가독성 높히기
* 메서드 파라미터로 동일한 타입의 다수(개수가 고정되지 않은)의 값을 전달할 경우, 메서드 정의부에서 ellipsis(String... args) 활용
* 불변객체, 불변변수는 static 으로 선언하여 재사용
* 예외객체 생성에도 비용이 드므로 Null check 시 Object.requireNonNull 보다는 직접 if문을 통해 Null check
* primitive 타입 변수들은 선언시 default value로 자동 초기화 됨
	* boolean : false
	* char : '\n'
	* byte, short, int, long, float, double : 0(0.0)
* 문자열을 숫자로 변환시, 숫자형 레퍼런스 클래스(Integer, Double)의 parse() 정적 메서드 사용
	* valueOf 메서드 사용시 불필요한 레퍼런스 클래스 객체 생성
* 콜렉션에 들어갈 데이터의 개수가 불변일 경우, ArrayList 대신 array를 사용하는것이 더 효율적
* ArrayList 사용시, 생성시점에 생성자 파라미터로 리스트 크기를 명시해주는것이 더 효율적
* loop 수행시, for > forEach > iterator > lambda순으로 성능이 좋음
	* 10000000 크기의 String list로 테스트 결과 for : 11ms, forEach : 13ms, iterator : 18mss, lambda : 58(ms)
* ArrayList, LinkedList 전체삭제시 clear() 보다 참조변수에 null을 할당 또는 새로운 list 할당 하는것이 더빠름
* 문자열 분할시, split 보다 subString이 더 효율적
	* Split은 Pattern.compile 객체 및 String 배열 객체를 새로 생성하여 성능이 떨어짐
	* SubString은 원본배열 참조변수 및 offset(시작위치), count(개수) 변수만 가지므로 더빠름
* 문자열 결합시 + 연산자보다 StringBuffer/StringBuilder 를 통한 결합이 더 빠름
	* 문자열은 불변객체로서, + 연산시 매번 새로운 문자열 객체 생성
	* StringBuffer, StringBuilder는 기존 String 객체의 값을 수정하는것이므로 불필요한 객체 생성이 없어 더빠름
	* StringBuffer와 StringBuilder의 차이는 동기화 여부(StringBuilder는 동기화해주지않으므로 멀티스레딩환경이 아니거나 경쟁이 발생하지 않는다면 성능이 StringBuffer보다 더 좋음)
	* Java 5 이후부터 String + 연산도 내부적으로 StringBuilder 를 통해 수행
	* but 매번 + 연산시마다 StringBuilder 객체를 생성하므로 반복적으로 String + 연산 수행시 직접 StringBuilder를 생성하여 사용하는것이 더 효율적
* eqauls 메서드 재정의시, 양질의 equals 메서드 재정의 원칙에 맞춰 정의 및 hashCode 메서드도 재정의
* 컬렉션 내 요소들의 1. 순서가 중요하지 않고, 2. 중복되는 데이터가 없으며, 3. 요소 검색(contains, indexOf 등) 이 쓰일경우 List 대신 Set(HashSet) 사용
* 제네릭 사용시, 타입 안전함에도 형변환 경고문구가 사라지지 않는다면 @SuppressWarnings로 경고 무시 후 그 이유 주석으로 달기 
* toString 재정의 할때 모든 멤버필드 반환시 ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE); 사용
* Map의 키값이 Enum 타입일 경우, 무조건 EnumMap 사용(10만번 get 수행 시간 - HashMap : 21ms, EnumMap : 4ms)
* 컬렉션 Raw타입은 컴파일러가 타입체크를 못해주므로 사용 지양
* 메서드 파라미터로 컬렉션 사용시 wildcard ?를 통해 범용성 높일 수 있음
* 가능한한 모든 경고메시지는 해결하고, 문제가 없음에도 발생하는 경고메시지는 @SuppressWarnings 어노테이션으로 생략
* 배열보다 리스트가 컴파일단계에서 타입안전성이 더 높으므로 가급적 리스트 사용(but 배열이 더 속도 빠름)
* 와일드카드보단 타입매개변수 사용(얻는 이점(가독성)에 비해 손해(매개변수로만 사용가능, 데이터수정시 도우미 메서드 필요))가 더 큼
* 가변인수 메서드의 가변인수타입이 제네릭일 경우, 그 값을 받는 객체는 Raw 타입이거나 Object 타입이면 안된다.
* 열거 타입별(혹은 그룹별) 다르게 동작하는 메서드 필요시. 타입별 메서드 구현 사용
* ```List<EnumType>``` 을 사용할 경우, ```Set<EnumType>``` 으로 대체
* 비공개 api(내부적으로만 쓰이는 private 메서드)에서 파라미터 validation 시 assert 이용
* 메서드 파라미터 validation 을 메서드 내부에서 쓰이는 또다른메서드가 수행시, 외부메서드에선 생략
* 재사용성이 높을것이라 예상되는 메서드를 제외하곤 가급적 유틸메서드 추가 지양
* 메서드 매개변수가 4개 이상일경우, 메서드를 분리하거나, 매개변수들을 묶은 정적 멤버클래스로 정의하여 클래스 객체 전달
* 메서드가 boolean 타입 파라미터를 가지고 있을시, 원소 2개짜리 멤버 enum 정의하여 대체(가독성 증가)
* 메서드 반환값이 null 일경우, 가급적 null 대신 Empty Object 반환
* bean 으로 등록되는 객체에선 static 사용 불필요(static final 제외)
* 원소가 1개인 리스트 생성시, Arrays.asList(E) 보다 Collections.singletonList(E) 가 더 메모리 절약 가능
* 지역변수는 사용되는 시점에 선언하고 초괴화까지 함께 해주는것이 좋음
* 코드 가독성 및 실수 방지 측면에서 foreach > for > while 
* 난수 생성시, Random 보다 ThreadLocalRandom 이 더 효율적(Java 7 이상부터 사용 가능)
* 직렬화(Serialization) 가능 클래스 정의시 직렬화 프록시 패턴 사용
* 불변 Collections 사용시 Collections.unmodifiable 사용(final 로 불변 불가능)
* 원소가 1개인 불변 컬렉션 생성시 Collections.singletonCollection API 사용

## 람다
* 람다식 사용시, 람다식내에 코드가 1줄이상이면 안되고, 람다식만으로 식의 동작이 설명이 충분히 되어야함
	* 람다식 코드가 1줄 이상이거나, 식만으로 동작 설명이 한눈에 되지 않으면, 별도의 메서드로 정의하고 메서드 참조 사용
* 스트림 연산은 순수 연산이어야 함(컬렉션 원소들에만 적용되는 연산, 스트림연산 내에서 람다식 외부의 객체 참조하면 안됨)
* 클래스 확장 필요 + 확장된 클래스가 한곳에서만 사용됨(다른곳에 재사용 x) 시 함수형 인터페이스를 통한 확장 고려
* 병렬 스트림은 가급적 사용 지양

## 클래스
* 디폴트 클래스 객체를 정의하거나 재사용될 수 있는 클래스 객체들을 정의할시 정적 팩터리 메서드를 통해 정의하여 재사용성 향상 및 가독성 개선
* 클래스 생성자 파라미터가 4개 이상이거나 불변클래스일시 생성자 대신 빌더패턴 사용
* 상태값(멤버변수)을 가지는 클래스 정의시 toString도 함께 재정의
* 클래스 기능 확장시 확실한 IS-A 관계가 아니면 상속 대신 delegate 패턴(컴포지션) 사용
* 클래스 상속을 통한 기능 확장시 부모 - 자식 클래스 간 의존성 제거
    * 부모클래스의 재정의 가능메서드 자기사용 제거
    * 자식클래스의 부모클래스 메서드 사용 제거
    * 재정의 가능메서드에 @impleSpec 어노테이션을 통해 하위 클래스들이 그 메서드를 상속받아 재정의 할때, 그 메서드가 부모클래스 내부의 어디서 어떻게 동작하는지 상세히 설명
* 추상 클래스 대신 인터페이스 + default 메서드 또는 추상골격구현클래스 사용
* 정적 변수, 메서드로만 이루어진 유틸리티 클래스 구현시 private 생성자를 명시적으로 선언
	* 내부에 AssertionError(); 를 추가하여 리플렉션을 통한 공격방어
* 클래스 정의시 가급적 불변 클래스로 정의(private 클래스, private final 멤버필드, setter 메서드 비제공)
* setter 메서드는 반드시 필요한 경우가 아니면 제공하지 않고 최대한 불변클래스로 설계
* 상속관계의 클래스 구조 설계시, 추상 클래스보단 인터페이스 + default 메서드로 구현
* 불변클래스 멤버필드에 객체(특히 Date)가 존재할 경우, getter 또는 setter 나 생성자를 통해서도 해당 객체의 값을 수정하여 클래스 불변식 침해 가능
	* 객체 멤버필드를 final로 제공하거나 getter에서 방어적복사 수행
	* 객체 생성자나 setter 에서 파라미터로 넘어온 객체를 복사하여 멤버 필드에 할당
* 클래스 객체 참조변수는 범용성을 위해 가급적 인터페이스 변수나 부모클래스 변수를 사용


## 예외처리 및 로깅
* checkedException 은 uncheckedException으로 감싸서 throw 하여 프로세스가 계속 이어지도록 구현
* 로그 메시지 생성시 '+' 연산자 대신 slf4j 의 String Formatter를 사용하여 코드 가독성 향상
    * slf4j logger를 사용하지 않을시 slf4j 의 MessageFormatter.arrayFormat 메서드 대신 사용
* 로그 메시지 생성시 최대한 많은 정보를 포함시켜야 함
    * 어느 메서드의 어느 부분에서 에러가 발생했는지
    * 메서드 파라미터로 어느값이 넘어왔는지
* 발생한 에러에 대한 별도의 복구작없이나 클라이언트 알림 방식이 있을때에만 예외처리하고 그 외에는 에러를 그대로 throw하여 에러창을 통해 클라이언트에게 작업이 실패했음을 알려야함
* debug 로깅은 최대한 하나로 합쳐 코드 가독성 향상
* 프로젝트의 Exception Handler에서 에러에 대해 로깅해주고 있을시, 별도의 에러로깅 불필요
* 

## 주석
* 공개 API의 경우, 매개변수의 valid 조건 및, invalid 시 발생하는 예외를 주석으로 설명
* 부모클래스 : @impleSpec 태그를 이용해 재정의 가능메서드의 자기사용패턴 주석 추가
* 자식클래스 : 재정의한 클래스나 메서드의 기능이 부모클래스와 비슷할 경우, @inheritDoc 태그를 통해 부모클래스 주석 상속
* 제네릭 : 모든 타입 매개변수(T, E...)에 주석 추가
* 열거타입 : 열거타입 내부 모든 상수 원소에도 주석 추가
* 예외 : 메서드에서 발생 가능한 모든 예외를 @throws 어노테이션을 통해 문서화


## 기타
* 람다식은 디버깅이 어려우므로 1회용 함수에만 사용하고 그외에는 가급적 별도의 함수로 정의하여 사용
* 기능 구현시, 코드 작성 전 구조 설계부터 선행
* Objects.requireNonNull
* API Invoker 개발시, Rest API 보단 Reactive API 로 개발하여 비동기적으로 호출하도록 구현

## 데이터베이스
* Table join 시 데이터 중복이 발생한다면 차라리 각각의 테이블을 따로 조회하여 데이터를 가져오는것이 더 좋음

## Spring
* Bean 클래스 정의시 최대한 POJO 로 정의
	* 필드 주입 지양 : 의존성 주입시, 필드 주입보단 수정자 주입이나 생성자주입 사용
	* @Autowire 지양 : 의존성 주입시, * @Autowire 어노테이션을 통한 자동 주입보다는 구성파일을 통한 생성자/수정자 주입 사용
	* @Value 지양 : 값 주입시, @Value 어노테이션을 통한 주입보다는 구성파일을 통한 생성사/수정자 주입 사용
* @ComponentScan을 통한 스캔 패키지 경로 명시시, basePackages 대신 basePackageClass 사용
* Spring 컴포넌트 클래스에 생성, 소멸 콜백메서드 등록시 @PostConstruct/@PreDestroy 어노테이션 사용
	* init-method/destroy-method 지정, InitializingBean/DisposableBean 인터페이스 상속 사용 지양
