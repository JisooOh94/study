# BO(Business Object)
* 비즈니스 로직을 구현하는 객체, 비즈니스 규칙을 캡슐화

```java
public class OrderBO {
    public boolean processOrder(Order order) {
        // 비즈니스 로직 구현
        if (order != null && order.isValid()) {
            // 주문 처리 로직
            return true;
        }
        return false;
    }
}
```

# DAO
* 데이터베이스나 다른 영구 저장소에 대한 접근을 추상화하는 객체
* CRUD(Create, Read, Update, Delete) 작업을 처리하는 메서드를 제공
* 데이터베이스와의 상호작용을 캡슐화하여 비즈니스 로직과 데이터 접근 로직을 분리

```java
@Repository
public class UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());
    }

    public void saveUser(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getEmail());
    }

    // UserRowMapper는 ResultSet을 User 객체로 매핑하는 클래스입니다.
}
```

# VO(Value Object)

### 정의 및 목적
- 특정 값의 집합을 나타냄.
- 값의 동일성 기준으로 비교.

### 특징
- 불변 객체로 설계.
- 값 자체가 동일성 판단 기준.
- 주로 주소, 날짜, 통화 표현.

### 장점
- 스레드 안전성 확보.
- 값 비교 용이.
- 가독성과 유지보수성 높음.

```java
public final class AddressVO {
    private final String street;
    private final String city;
    private final String postalCode;

    public AddressVO(String street, String city, String postalCode) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressVO addressVO = (AddressVO) o;
        return street.equals(addressVO.street) && city.equals(addressVO.city) && postalCode.equals(addressVO.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, postalCode);
    }
}

```

# DTO(Data Transfer Object)
### 정의 및 목적
- 계층 간 데이터 전송 목적.
- 네트워크 전송 효율성 높임.

### 특징
- 가변 객체로 설계.
- 클라이언트-서버 통신에 사용.
- 직렬화 및 역직렬화 용이.

### 장점
- 네트워크 대역폭 절약.
- 비즈니스 로직 없음.
- 데이터 전송에 집중.

```java
public class UserDTO {
    private String name;
    private String email;

    // 기본 생성자와 getter, setter 포함
    public UserDTO() {}

    public UserDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

# VO vs DTD 비교 요약

|구분        |VO (Value Object)|DTO (Data Transfer Object)|
|----------|---------------|-----------------------|
|목적        |값 표현          |데이터 전송                |
|불변성       |불변            |가변                     |
|동일성 판단  |값 기반          |객체 참조                  |
|비즈니스 로직|포함 가능         |없음                     |
|사용 사례     |주소, 날짜        |API, 네트워크              |
|직렬화       |필요 시          |기본적                    |
|설계 초점     |값의 의미         |전송 효율                  |
