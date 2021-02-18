# JVM 내부구조

![image](https://user-images.githubusercontent.com/48702893/108217098-908a6280-7176-11eb-83ea-e1585e5eea08.png)

1. 자바 컴파일러(javac)가 자바코드를 자바 바이트코드(반기계어)로 컴파일
2. 클래스로더(Class Loader)가 자바 바이트코드를 런타임 데이터 영역(Runtime Data Area)에 로드
3. 실행엔진(Execution Engine)이 자바 바이트코드 실행 

# 클래스로더
* 자바 어플리케이션 실행시, 자바 바이트코드를 JVM 의 런타임 데이터 영역에 로드해주는 장치

### 특징
* 동적 로드
	* 일종의 lazy-load
	* 클래스 코드가 실행엔진에 의해 최초로 참조(사용요청)되었을때 런타임 데이터 영역으로 로드
* 계층 구조
	* 로드 하는 바이트 코드에 따라 하나의 JVM 내에 여러 클래스 로더가 계층적으로 존재
* 캐싱
	* 한번 로드한 클래스 바이트코드는 각 클래스로더의 '네임스페이스' 라는 캐시메모리에 캐싱
	* 이후 같은 같은 코드 로드 요청시, 네임스페이스에서 탐색하여 로드

### 클래스 로더 원칙
* 위임 원칙(Delegation Principle)
	* 바이트코드 로드 요청시, 요청을 받은 클래스로더는 자신의 클래스로더 캐시 탐색하여 요청된 바이트코드가 캐싱되어있는지 확인
	* 캐시데이터가 없다면, 계층구조에 따라 상위 클래스로더에게 로드 위임 (위임받은 상위 클래스 로더는 같은 과정 반복)
* 가시성 원칙(Visible Principle)
	* 하위 클래스로더는 위임 원칙을 통해 상위 클래스 로더의 클래스를 찾을 수 있지만, 상위 클래스 로더는 하위 클래스 로더의 클래스를 찾을 수 없음
* 유일성 원칙(Uniqueness Principle)
	* 하위 클래스로더가 상위 클래스로더에서 로드한 클래스를 다시 로드하지 않아야 함

### 클래스로더 계층 구조

![image](https://user-images.githubusercontent.com/48702893/108221368-0f819a00-717b-11eb-816d-0fd179053a1a.png)

1. 부트스트랩 클래스 로더
	* JRE 에서 제공하는 JVM 실행에 필요한 가장 기본적인 클래스(Java API)들 로딩
	* $JAVA_HOME\jre\lib 하위 라이브러리(e.g rt.jar)
2. 익스텐션 클래스 로더
	* JRE 에서 제공하는 여러 자바 확장 기능(e.g Java 보안 확장 기능)관련 클래스 로드
	* $JAVA_HOME/jre/lib/ext 하위 라이브러리 
3. 시스템 클래스 로더
	* 사용자가 개발한 애플리케이션 클래스(classpath 내의 클래스)들 로드
4. 사용자 정의 클래스 로더
	* 사용자가 직접 생성해서 사용하는 클래스 로더

### 위임 모델(Delegation Model)

![image](https://user-images.githubusercontent.com/48702893/108247983-b0318300-7196-11eb-90a1-9ea24769aec6.png)

1. 캐시 탐색
* findLoadedClass()
* 클래스 로드 요청(loadClass()) 수신시, 클래스로더는 자신의 네임스페이스에 클래스코드가 캐싱되어있는지 확인

2. 로드 요청 위임
* 캐싱되어있지 않을경우, 상위 클래스 로더에게 클래스 로드 위임 (상위 클래스로더는 1번 부터 다시 반복)

3. 파일 탐색
* findClass() 
* 최상위 클래스 로드에 까지 위임되었으나 캐싱되어있지 않을경우, 하위 클래스로더로 내려가며 로딩을 담당하는 바이트코드 파일(.class) 에서 클래스 탐색 

4. 로드 및 캐싱
* 클래스 코드 탐색 성공시, 해당 클래스 로더의 네임스페이스에 캐싱 후, 자바 런타임 데이터 영역에 로드

```java
public abstract class ClassLoader { 
	private ClassLoader parent; 
	
	protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException { 
		Class c = findLoadClass(name); 
		if (c == null) { 
			try { 
				if (parent != null) c = parent.loadClass(name, false);
				else c = findBootstrapClass0(name); 
			} catch (ClassNotFoundException e) { 
				c = findClass(name); 
			} 
		}
		if (resolve) resolveClass(c); 
		return c; 
	} 
}
```

### 바이트 코드 클래스 로딩 과정

![image](https://user-images.githubusercontent.com/48702893/108252481-0bb23f80-719c-11eb-979b-563b5b191202.png)

1. 로드
	* .class 파일에서 읽은 클래스 바이트 코드를 JVM 런타임 데이터 영역에 로드 
2. 검증
	* 로드된 클래스 바이트 코드 유효성 검증
	* 클래스가 자바 언어 명세 및 JVM 명세에 맞춰 정의되어있는지 검사
3. 준비
	* 클래스 객체에 필요한 메모리 할당 후 클래스 객체 생성
	> 클래스 객체 : 클래스 인스턴스가 아닌, 클래스 인스턴스 생성시에 필요한 메타 데이터(클래스 명, 필드 명, 메서드, 생성자, 부모클래스 등)를 가지고있는 Class 타입 객체
4. 분석
	* 런타임 데이터 영역내 상수풀에서 클래스 메타데이터를 모두 찾아 심볼릭 레퍼런스 값을 다이렉트 레퍼런스로 수정 (Dynamic Linking)
	* 심볼릭 레퍼런스 : 참조하는 데이터의 이름
	* 다이렉트 레퍼런스 : 참조하는 데이터가 실제 메모리에 저장되어있는 주소 
	* Dynamic Linking : 심볼릭 레퍼런스값(데이터의 이름)을 통해 그 데이터가 실제로 메모리에 저장되어있는 주소 값을 찾아 심볼릭 레퍼런스값을 대체하는것
5. 초기화
	* 클래스의 정적필드들을 설정된 값으로 초기화
	
<br>
	
# 런타임 데이터 영역

![image](https://user-images.githubusercontent.com/48702893/108376531-6e601580-7246-11eb-85bf-32a2fb0ad32f.png)

* 각 스레드별로 생성되는 PC Register, JVM Stack, Native Method Stack 과 전체 스레드가 공유하는 Heap, Method Area, Runtime Constant Pool 로 구성

## Thread Scope
### PC Register
* 현재 스레드가 수행중인 메서드 코드가 저장되어있는 런타임 상수풀 인덱스 저장
* 런타임 상수풀에는 Method Area 에 저장되어있는 메서드 바이트 코드의 주소 저장되어있음 

### JVM Stack
* 스택 프레임을 저장하는 스택
* printStackTrace() 를 통해 출력되는 로그의 각 줄은 printStackTrace를 수행한 스레드의 Jvm Stack 에 저장되어있던 스택프레임을 표현

> 스택 프레임
> * 지역변수 배열, 피연산자 스택, 런타임 상수풀 참조변수 로 구성
> * 지역변수 배열 : 현재 스레드가 실행중인 메서드의 클래스 인스턴스 this 레퍼런스, 파라미터, 지역변수 저장
> * 피연산자 스택 : 현재 실행중인 명령어 연산에 필요한 피연산자 데이터들을 저장하는 스택

### Native Method Stack
* Java 가 아닌 다른 언어로 작성된 외부 라이브러리 코드를 수행할떄 사용되는 스택

<br>

## Process Scope
### Heap
* 클래스 인스턴스들이 저장되는 영역

### Method Area
* Permanent generation 영역(Java8 부터 Metaspace 로 대체)
* Class 객체(클래스 메타데이터) 및 클래스 정적 변수, 클래스 바이트코드, 클래스 런타임 상수 풀 저장
* 클래스 인스턴스 생성 및 메서드 수행을 위한 모든 정보가 저장되는 영역

### Runtime Constant Pool
* 클래스의 모든 상수, 정적 변수 및 Class 객체, 바이트 코드가 저장되어있는 메모리 주소가 기록되어있는 테이블
* 실행엔진은 메서드 수행시, 바이트코드에 기록되어있는 런타임 상수 풀 인덱스를 기반으로 메모리에서 데이터를 찾아 연산 수행
* 각 클래스마다 개별적으로 생성됨
