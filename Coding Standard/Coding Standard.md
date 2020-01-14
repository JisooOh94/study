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
### 예외처리 및 로깅
* checkedException 은 uncheckedException으로 감싸서 throw 하여 프로세스가 계속 이어지도록 구현
* 로그 메시지 생성시 '+' 연산자 대신 slf4j 의 String Formatter를 사용하여 코드 가독성 향상
    * slf4j logger를 사용하지 않을시 slf4j 의 MessageFormatter.format 메서드 대신 사용
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