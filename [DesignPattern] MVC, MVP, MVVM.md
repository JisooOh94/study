# 1. MVC (Model-View-Controller)

### 구성 요소
- **Model**: 데이터와 비즈니스 로직 관리.
- **View**: 사용자 인터페이스 담당.
- **Controller**: 사용자 입력 처리, Model과 View 업데이트.

### 작동 방식
- 사용자 입력이 View에서 Controller로 전달됨.
- Controller가 입력 처리, Model 업데이트, View 변경.
- View가 Model로부터 데이터 받아 표시.

### 데이터 흐름
- Controller가 입력 받아 Model과 View 업데이트.
- View가 Model의 변경 사항 감시하거나 Controller 통해 업데이트.

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

### 구성 요소
- **Model**: 데이터와 비즈니스 로직 포함.
- **View**: 사용자 인터페이스, UI 로직 없음.
- **Presenter**: View와 Model 중재자, 모든 UI 로직 처리.

### 작동 방식
- 사용자 입력이 View에서 Presenter로 전달됨.
- Presenter가 입력 처리, Model 업데이트, View에 결과 표시.

### 데이터 흐름
- View는 Presenter와만 상호작용.
- Presenter가 Model 데이터 가져와 View에 전달.

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

### 구성 요소
- **Model**: 데이터와 비즈니스 로직.
- **View**: 사용자 인터페이스.
- **ViewModel**: View의 상태와 행동 관리, 데이터 바인딩으로 View와 상호작용.

### 작동 방식
- View와 ViewModel 간 양방향 데이터 바인딩.
- 사용자 입력이 ViewModel로 전달, Model 업데이트.
- Model 데이터 변경 시 ViewModel 통해 View에 자동 반영.

### 데이터 흐름
- View와 ViewModel 간 데이터 바인딩으로 상호작용.
- ViewModel이 Model 업데이트, View 자동 반영.

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

### View와 Model의 상호작용
- **MVC**: View가 Model과 직접 상호작용 가능.
- **MVP**: View는 Model과 직접 상호작용 안 함. Presenter가 중재.
- **MVVM**: View와 ViewModel 간 데이터 바인딩으로 상호작용.

### 비즈니스 로직의 위치
- **MVC**: Model과 Controller에 분산.
- **MVP**: Presenter에 집중.
- **MVVM**: ViewModel에 집중.

### 적용 분야
- **MVC**: 웹 애플리케이션.
- **MVP**: 데스크톱 및 모바일 애플리케이션.
- **MVVM**: 데이터 바인딩 용이한 프레임워크.
