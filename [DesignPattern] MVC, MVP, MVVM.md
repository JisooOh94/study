# 1. MVC (Model-View-Controller)

### 구조
- **Model**:
    - 데이터와 그 데이터를 처리하는 비즈니스 로직을 포함.
    - 데이터베이스와의 직접적인 상호작용 담당.

- **View**:
    - 사용자에게 데이터를 표시하는 부분.
    - 사용자 인터페이스를 구성하며, 사용자의 입력을 받음.

- **Controller**:
    - 사용자의 요청을 수신하고, 해당 요청을 처리할 모델과 뷰를 결정.
    - 모델로부터 데이터를 가져와 뷰에 전달하거나, 뷰로부터 입력을 받고 모델을 업데이트.

### 장점
- 명확한 역할 분리로 인해 코드의 유지보수와 테스트가 용이.
- 뷰와 모델의 재사용성이 높음.
- 다양한 뷰를 통해 동일한 모델을 사용할 수 있음.

### 단점
- 복잡한 애플리케이션에서는 컨트롤러가 비대해질 수 있음.
- 모델과 뷰 사이의 의존성이 생길 수 있음.

### 예시

```java
//model
@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String email;

  // Getters and Setters
}

//Repository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

//Service
@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public User getUserById(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }
}

//Controller
@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.getUserById(id);
    return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    User savedUser = userService.saveUser(user);
    return ResponseEntity.ok(savedUser);
  }
}
```

# 2. MVP (Model-View-Presenter)

### 구조
- **Model**:
    - MVC의 모델과 유사하게 데이터와 비즈니스 로직 포함.
    - 데이터베이스와의 상호작용 담당.
    - Presenter에 데이터를 제공하고, 요청에 따라 데이터를 갱신.

- **View**:
    - 사용자 인터페이스를 담당하며, Presenter와 상호작용.
    - 사용자 입력을 받아 Presenter에 전달.

- **Presenter**:
    - 뷰와 모델 간의 중재자 역할.
    - 뷰의 상태를 업데이트하고, 사용자 입력을 처리.
    - 모든 UI 로직을 처리하고, 뷰를 업데이트.
    - 모델에서 데이터를 가져와 뷰에 적용.

### 장점
- 뷰와 모델 사이의 낮은 결합도.
- Presenter를 통해 테스트가 용이하며, 특히 단위 테스트가 쉬움.

### 단점
- Presenter가 비대해질 수 있음.
- 복잡한 UI 로직에서는 관리가 어려울 수 있음.

### 예시

```java
//model
public class UserModel {
    private Long id;
    private String name;
    private String email;

    // Getters and Setters
}

//presenter
public class UserPresenter {
  private UserService userService;
  private UserView view;

  public UserPresenter(UserService userService, UserView view) {
    this.userService = userService;
    this.view = view;
  }

  public void loadUser(Long id) {
    User user = userService.getUserById(id);
    if (user != null) {
      view.displayUser(user);
    } else {
      view.showError("User not found");
    }
  }

  public void saveUser(UserModel userModel) {
    User user = new User();
    user.setName(userModel.getName());
    user.setEmail(userModel.getEmail());
    userService.saveUser(user);
    view.showSuccess("User saved successfully");
  }
}

//view
public interface UserView {
  void displayUser(User user);
  void showError(String message);
  void showSuccess(String message);
}
```


# 3. MVVM (Model-View-ViewModel)

### 구조
- **Model**:
    - 데이터와 비즈니스 로직 포함.
    - 데이터베이스와의 상호작용 담당.

- **View**:
    - 사용자 인터페이스 담당.
    - ViewModel과 데이터 바인딩을 통해 상호작용.
    - 사용자 입력을 받아 ViewModel에 전달.

- **ViewModel**:
    - 뷰의 상태와 행동을 관리.
    - 모델에서 데이터를 가져와 뷰에 바인딩.
    - 데이터 바인딩을 통해 뷰와 상호작용하며, UI 로직 처리.

### 장점
- 데이터 바인딩을 통해 UI 업데이트가 간편.
- 뷰와 모델 간의 낮은 결합도.

### 단점
- 데이터 바인딩 사용 시 디버깅 어려움.
- 복잡한 애플리케이션에서는 ViewModel이 비대해질 수 있음.

### 예시

```java
//model
public class UserModel {
    private Long id;
    private String name;
    private String email;

    // Getters and Setters
}

//viewmodel
public class UserViewModel {
  private StringProperty name = new SimpleStringProperty();
  private StringProperty email = new SimpleStringProperty();
  private UserService userService;

  public UserViewModel(UserService userService) {
    this.userService = userService;
  }

  public StringProperty nameProperty() {
    return name;
  }

  public StringProperty emailProperty() {
    return email;
  }

  public void loadUser(Long id) {
    User user = userService.getUserById(id);
    if (user != null) {
      name.set(user.getName());
      email.set(user.getEmail());
    }
  }

  public void saveUser() {
    User user = new User();
    user.setName(name.get());
    user.setEmail(email.get());
    userService.saveUser(user);
  }
}

//View
public class UserView {
  private TextField nameField;
  private TextField emailField;
  private Button saveButton;
  private UserViewModel userViewModel;

  public UserView(UserViewModel userViewModel) {
    this.userViewModel = userViewModel;
    nameField.textProperty().bindBidirectional(userViewModel.nameProperty());
    emailField.textProperty().bindBidirectional(userViewModel.emailProperty());

    saveButton.setOnAction(e -> userViewModel.saveUser());
  }
}
```

# 비교 요약

| 패턴  | 장점                                                         | 단점                                                      |
|-------|-------------------------------------------------------------|-----------------------------------------------------------|
| MVC   | 명확한 역할 분리, 재사용성 및 확장성 증대                   | 컨트롤러 비대화, 모델과 뷰 의존성                          |
| MVP   | 낮은 결합도, 테스트 용이성 증가                             | Presenter 비대화, 복잡한 UI 로직 관리 어려움                |
| MVVM  | 간편한 UI 업데이트, 낮은 결합도                             | 데이터 바인딩 디버깅 어려움, ViewModel 비대화               |
