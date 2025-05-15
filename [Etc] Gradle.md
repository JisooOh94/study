# Maven vs Gradle
* Gradle 은 Kotlin DSL을 사용하여 작성되며, 프로그래밍 언어 스타일로 더 유연하고 간결하게 작성 가능
### **의존성 설정**
- **Maven (`pom.xml`)**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>2.5.4</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
        <version>2.5.4</version>
    </dependency>
</dependencies>
```

- **Gradle (`build.gradle.kts`)**
```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.5.4")
}
```

### **플러그인 설정**
- **Maven**
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

- **Gradle**
```kotlin
plugins {
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}
```

### **프로파일 설정**
- **Maven**
```xml
<profiles>
    <profile>
        <id>dev</id>
        <properties>
            <env>development</env>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <env>production</env>
        </properties>
    </profile>
</profiles>
```

- **Gradle**
```kotlin
// 프로젝트 속성에서 환경 변수 가져오기
val env: String = when (project.findProperty("env")?.toString()) {
    "development" -> "development"
    "production" -> "production"
    null -> "development" // 기본값 설정
    else -> throw IllegalArgumentException("Invalid environment: ${project.findProperty("env")}. Allowed values are: development, production")
}

// 환경 변수 출력
println("Environment: $env")
```

### **멀티모듈 프로젝트 설정**
- **Maven**
```xml
<modules>
    <module>module-a</module>
    <module>module-b</module>
</modules>
```

- **Gradle**
```kotlin
include("module-a", "module-b")
```

### `pom.xml` vs `build.gradle.kts` 비교

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>example-project</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <!-- 의존성 추가 -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.5.4</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
            <version>2.5.4</version>
        </dependency>
    </dependencies>

    <!-- 플러그인 설정 -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
        <!-- 빌드 출력 디렉토리 변경 -->
        <directory>custom-target</directory>
    </build>

    <!-- 프로파일 설정 -->
    <profiles>
        <profile>
            <id>dev</id>
            <properties>
                <env>development</env>
            </properties>
        </profile>
        <profile>
            <id>prod</id>
            <properties>
                <env>production</env>
            </properties>
        </profile>
    </profiles>

    <!-- 멀티모듈 프로젝트 -->
    <modules>
        <module>module-a</module>
        <module>module-b</module>
    </modules>
</project>
```

```kotlin
plugins {
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.example"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.5.4")
}

tasks.withType<Jar> {
    destinationDirectory.set(file("custom-target"))
}

val env: String by project
println("Environment: $env")

// 멀티모듈 프로젝트 설정은 settings.gradle.kts에서 관리
// settings.gradle.kts 예시:
// include("module-a", "module-b")
```

| **`pom.xml`** 항목                     | **`build.gradle.kts`** 대응 항목                                                                 |
|----------------------------------------|-------------------------------------------------------------------------------------------------|
| `<groupId>com.example</groupId>`       | `group = "com.example"`                                                                         |
| `<artifactId>example-project</artifactId>` | Gradle에서는 별도로 설정하지 않음 (기본적으로 프로젝트 디렉토리 이름 사용)                          |
| `<version>1.0.0</version>`             | `version = "1.0.0"`                                                                             |
| `<packaging>jar</packaging>`           | Gradle에서는 기본적으로 `jar`로 설정됨                                                          |
| `<dependencies>`                       | `dependencies` 블록                                                                             |
| `<dependency>`                         | `implementation("org.springframework.boot:spring-boot-starter-web:2.5.4")` 등                   |
| `<build><plugins>`                     | `plugins` 블록                                                                                  |
| `<plugin>`                             | `id("org.springframework.boot") version "2.5.4"`                                               |
| `<directory>custom-target</directory>` | `tasks.withType<Jar> { destinationDirectory.set(file("custom-target")) }`                       |
| `<profiles>`                           | `val env: String by project` 및 `println("Environment: $env")`                                  |
| `<modules>`                            | `settings.gradle.kts`에서 `include("module-a", "module-b")`                                     |


# Gradle 동적 빌드 스크립트 작성
* Gradle은 Kotlin DSL을 사용하므로 조건문, 함수, 변수 등을 활용해 동적으로 빌드 스크립트를 작성할 수 있음

```kotlin
// 변수 정의
val isProduction: Boolean = project.hasProperty("prod") && project.property("prod") == "true"
val appVersion: String = project.findProperty("appVersion")?.toString() ?: "1.0.0"

// 조건문 활용
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    if (isProduction) {
        implementation("com.example:production-library:1.0.0")
    } else {
        implementation("com.example:development-library:1.0.0")
    }
}

// 함수 정의
fun configureLogging(level: String) {
    println("Configuring logging level: $level")
    tasks.register("configureLogging") {
        doLast {
            println("Logging level set to $level")
        }
    }
}

// 함수 호출
configureLogging(if (isProduction) "ERROR" else "DEBUG")
```


# Gradle 커스텀 빌드 태스크
* Gradle은 커스텀 태스크를 정의할 수 있음
* 커스텀 태크스란 mvn install, clean, package 등과 같은 기본 태스크 외에 사용자가 추가로 정의한 태스크를 의미함

```kotlin
// 동적 태스크 생성
tasks.register("printAppInfo") {
    doLast {
        println("App Version: $appVersion")
        println("Environment: ${if (isProduction) "Production" else "Development"}")
    }
}

// 빌드 시 실행
tasks.build {
    dependsOn("printAppInfo")
```
```shell
// 쉘을 통해서도 커스텀 빌드 태스크 수행 가능
./gradlew hello
```