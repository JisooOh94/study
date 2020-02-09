### 코드 작성
* 삼항 연산자 활용
* Object.toString() 보다는 String.valueOf(Object) 를 사용하여 NPE 방지
* String 변수 null check 시 StringUtils.isEmpty() 사용
* 리스트에 추가/삭제 작업이 빈번하면 LinkedList, 조회작업이 빈번하면 ArrayList 사용
* 레퍼런스 타입 변수는 auto boxing을 유발하여 성능을 저하시키므로 사용을 지양하고 반드시 사용해야한다면 null-check 필수 및 primitive 타입과 함께 사용되지 않도록 주의(auto boxing 발생, NPE 우려)
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
* String 분할시, split 보다 subString이 더 효율적
	* Split은 Pattern.compile 객체 및 String 배열 객체를 새로 생성하여 성능이 떨어짐
	* SubString은 원본배열 참조변수 및 offset(시작위치), count(개수) 변수만 가지므로 더빠름
* 문자열 결합시 + 연산자보다 StringBuffer/StringBuilder 를 통한 결합이 더 빠름
	* 문자열은 불변객체로서, + 연산시 매번 새로운 문자열 객체 생성
	* StringBuffer, StringBuilder는 기존 String 객체의 값을 수정하는것이므로 불필요한 객체 생성이 없어 더빠름
	* StringBuffer와 StringBuilder의 차이는 동기화 여부(StringBuilder는 동기화해주지않으므로 멀티스레딩환경이 아니거나 경쟁이 발생하지 않는다면 성능이 StringBuffer보다 더 좋음)
* eqauls 메서드 재정의시, 양질의 equals 메서드 재정의 원칙에 맞춰 정의 및 hashCode 메서드도 재정의
* 컬렉션 내 요소들의 1. 순서가 중요하지 않고, 2. 중복되는 데이터가 없으며, 3. 요소 검색(contains, indexOf 등) 이 쓰일경우 List 대신 Set(HashSet) 사용

### 클래스
* 디폴트 클래스 객체를 정의하거나 재사용될 수 있는 클래스 객체들을 정의할시 정적 팩터리 메서드를 통해 정의하여 재사용성 향상 및 가독성 개선
* 클래스 생성자 파라미터가 4개 이상이거나 불변클래스일시 생성자 대신 빌더패턴 사용
* 상태값(멤버변수)을 가지는 클래스 정의시 toString도 함께 재정의
* 클래스 기능 확장시 확실한 IS-A 관계가 아니면 상속 대신 delegate 패턴(컴포지션) 사용
* 클래스 상속을 통한 기능 확장시 부모 - 자식 클래스 간 의존성 제거
    * 부모클래스의 재정의 가능메서드 자기사용 제거
    * 자식클래스의 부모클래스 메서드 사용 제거
    * 재정의 가능메서드에 @impleSec 어노테이션을 통해 하위 클래스들이 그 메서드를 상속받아 재정의 할때, 그 메서드가 부모클래스 내부의 어디서 어떻게 동작하는지 상세히 설명
* 추상 클래스 대신 인터페이스 + default 메서드 또는 추상골격구현클래스 사용
* 정적 변수, 메서드로만 이루어진 유틸리티 클래스 구현시 private 생성자를 명시적으로 선언
	* 내부에 AssertionError(); 를 추가하여 리플렉션을 통한 공격방어
* 클래스 정의시 가급적 불변 클래스로 정의(private 클래스, private final 멤버필드, setter 메서드 비제공)

### 예외처리 및 로깅
* checkedException 은 uncheckedException으로 감싸서 throw 하여 프로세스가 계속 이어지도록 구현
* 로그 메시지 생성시 '+' 연산자 대신 slf4j 의 String Formatter를 사용하여 코드 가독성 향상
    * slf4j logger를 사용하지 않을시 slf4j 의 MessageFormatter.arrayFormat 메서드 대신 사용
* 로그 메시지 생성시 최대한 많은 정보를 포함시켜야 함
    * 어느 메서드의 어느 부분에서 에러가 발생했는지
    * 메서드 파라미터로 어느값이 넘어왔는지
* 발생한 에러에 대한 별도의 복구작없이나 클라이언트 알림 방식이 있을때에만 예외처리하고 그 외에는 에러를 그대로 throw하여 에러창을 통해 클라이언트에게 작업이 실패했음을 알려야함
* debug 로깅은 최대한 하나로 합쳐 코드 가독성 향상
* 프로젝트의 Exception Handler에서 에러에 대해 로깅해주고 있을시, 별도의 에러로깅 불필요

### 기타
* 람다식은 디버깅이 어려우므로 1회용 함수에만 사용하고 그외에는 가급적 별도의 함수로 정의하여 사용
* 기능 구현시, 코드 작성 전 구조 설계부터 선행
* Objects.requireNonNull

### 데이터베이스
* Table join 시 데이터 중복이 발생한다면 차라리 각각의 테이블을 따로 조회하여 데이터를 가져오는것이 더 좋음