# Annotaions[[참고]](https://www.baeldung.com/javax-validation)
| Annotation | Description | 
|:----------:|:------------|
| @AssertFalse | 값이 false 이어야 함 ||
| @AssertTrue | 값이 true 이어야 함 |
| @DecimalMax(value=) | 지정된 값 이하인 실수값 이어야 함 |
| @DecimalMin(value=) | 지정된 값 이상인 실수값 이어야 함 |
| @Digits(integer=,fraction=) | 지정된 정수와 소수 자리수보다 적어야 함 |
| @Future | 날짜가 현재보다 미래이어야 함 |
| @FutureOrPresent | 날짜가 현재이거나 미래이어야 함|
| @Past | 날짜가 현재보다 과거이어야 함 |
| @PastOrPresent | 날짜가 현재이거나 과거이어야 함|
| @Max(value=) | 지정된 값보다 이하이어야 함 |
| @Min(value=) | 지정된 값보다 이상이어야 함 |
| @NotEmpty |null 이거나 Empty Object가 아니어야 한다(String, Collections)|
| @NotBlank |문자열이 null이거나 emtpy거나 공백(' ')이 아니어야 한다.|
| @NotNull | null이 아니어야 함 |
| @Null | null이어야 함 |
| @Pattern(regex=, flag=) | 해당 정규식을 만족할 경우만 통과 가능 |
| @Size(min=, max=) | 문자열 또는 배열의 길이가 지정된 min, max 값을 만족해야 함 |
| @Positive | 숫자값이 양수이어야 함 |
| @PositiveOrZero | 숫자값이 0이거나 양수이어야 함 |
| @Negative | 숫자값이 음수이어야 함 |
| @NegativeOrZero | 숫자값이 0이거나 음수이어야 함 |

### 예시
```java
@AssertFalse
public boolean booleanVar_False;

@AssertTrue
public boolean booleanVar_True;

@DecimalMin(value = "5")
public int intVar_DecimalMin;

@DecimalMax(value = "10")
public int intVar_DecimalMax;

@Digits(integer = 2, fraction = 2)
public double doubleVar_Digits;

@Future
public Date date_Future;

@Past
public Date date_Past;

@Min(value = 5)
public int intVar_Min;

@Max(value = 5)
public int intVar_Max;

@NotNull
public String strVar_NotNull;

@Null
public String strVar_Null;

@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
public String strVar_Pattern;

@Size(min = 5, max = 10)
public int[] arrVar_Size;
```

### Collection의 element나, Optinal 의 element에도 적용 가능
```java
@NotEmpty
public List<@NotBlank String> userIdList;

//Optional<String> 값이 null이 아닐경우 그 내부 String 값도 Blank가 아니어야 한다.
public Optional<@NotBlank String> userId;
```

<br>

# validation 적용
### Request Parameter[[참고]](https://cornswrold.tistory.com/378)
1. Servlet-Context.xml 에 LocalValidatorFactoryBean 생성
2. ConfigurableWebBindingInitailizer bean 생성 후, LocalValidatorFactoryBean 주입
3. RequestMappingHandlerAdapter bean 생성 후, ConfigurableWebBindingInitailizer bean 주입 
```java
<bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean" />

<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
	<property name="webBindingInitializer">
		<bean class="org.springframework.web.bind.support.ConfigurableWebBindingInitializer">
			<property name="validator" ref="validator" />
			<property name="conversionService" ref="conversion-service"/>
		</bean>
	</property>
</bean>
...
```

4. Controller 메서드의 request parameters 에 @Valid 어노테이션 추가
```java
@RequestMapping("/test")
public void testMethod(@RequestParam @Valid Obj param) { }
```

* Spring에서 자동으로 Request parameter값들을 Model 객체로 binding 및 validation 수행해줌

### Method Parameter
```java
ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
Validator validator = factory.getValidator();

//validation 위반 내용은 Set<ConstraintViolation<T>> 으로 반환 
Set<ConstraintViolation<T>> violations = validator.validate(T);
```

