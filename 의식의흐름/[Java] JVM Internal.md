# JVM 내부구조

![image](https://user-images.githubusercontent.com/48702893/108217098-908a6280-7176-11eb-83ea-e1585e5eea08.png)

1. 자바 컴파일러(javac)가 자바코드를 자바 바이트코드(반기계어)로 컴파일
2. 클래스로더(Class Loader)가 자바 바이트코드를 런타임 데이터 영역(Runtime Data Area)에 로드
3. 실행엔진(Execution Engine)이 자바 바이트코드 실행

### JVM 특징
* 스택기반 가상머신
	* 레지스터 기반으로 연산을 수행하는 기존의 하드웨어와 달리 스택기반으로 연산 수행
* 심볼릭 레퍼런스
	* Primitive Type을 제외한 모든 타입(클래스, 인터페이스)정보를 명시적 메모리 주소 기반 레퍼런스가 아닌, 심볼릭 레퍼런스를 통해 참조
* 기본자료형의 플랫폼 독립성 보장
	* OS 종속적인 언어들의 경우 OS 에 따라 Primitive Type의 크기가 달라지나 JVM 은 OS 에 상관없이 동일

<br>

# 스택기반 가상머신 vs 레지스터기반 가상머신
### 스택기반 가상머신

![image](https://user-images.githubusercontent.com/48702893/108601418-c8e0a980-73df-11eb-84c3-b3f18c1cf17f.png)

* 피연산자와 연산후 결과 값을 피연산자 스택(연속되어있는 메모리 공간)에 저장하는 구조
* Stack Pointer 레지스터에 피연산자 스택의 top 에 해당하는 메모리 공간 주소 저장 (피연산자 PUSH/POP 될떄마다 이동)
* 장점
	* 피연산자 사용시, 코드에 피연산자가 저장되어있는 메모리 주소 기입 필요 없이, PUSH/ POP 명령만으로 피연산자 호출/추가 가능
* 단점
	* 피연산자를 이용한 연산 수행시, PUSH/POP 이 함께 수행되어야 하므로 연산 속도가 느림

### 레지스터기반 가상머신

![image](https://user-images.githubusercontent.com/48702893/108601430-d6962f00-73df-11eb-89f0-aad14f3e1324.png)

* 피연산자와 연사후 결과 값을 CPU 의 레지스터에 저장
* 피연산자 사용시, 코드에 직접 피연산자가 저장되어있는 메모리(레지스터) 주소 명시
* 장점
	* PUSH/POP 의 과정 없이 피연산자에 Direct Access 하여 read/write 할 수 있으므로 연산 속도가 빠름
* 단점
	* 코드에 피연산자 메모리 주소가 포함되므로 코드 길이가 길어짐
	
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
* 현재 스레드가 수행중인 코드를 가리키는 런타임 상수풀의 인덱스 저장 공간
* 런타임상수풀엔 바이트코드가 실제저장되어있는 메모리 공간(Method area) 주소가 저장되어있음
* PC Register 에 저장되어있는 런타임상수풀 인덱스 정보를 통해 바이트코드를 메모리로부터 읽어와 실행엔진이 실행

![KakaoTalk_20210220_154125777](https://user-images.githubusercontent.com/48702893/108586527-74fba380-7392-11eb-9795-223d41417cc4.jpg)

### JVM Stack
* 스택 프레임을 저장하는 스택
* printStackTrace() 를 통해 출력되는 로그는 스레드의 Jvm Stack 에 저장되어있던 스택프레임들에 대한 정보

> 스택 프레임
> * 지역변수 배열, 피연산자 스택, 클래스 런타임 상수풀 레퍼런스로 구성
> * 지역변수 배열 : 현재 스레드가 실행중인 메서드 클래스의 인스턴스 참조(this), 파라미터, 지역변수 저장
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
* JIT 컴파일러에 의해 컴파일된 Natice 코드가 캐싱되는 공간

### Runtime Constant Pool
* 클래스의 모든 상수, 정적 변수 및 Class 객체, 바이트 코드가 저장되어있는 메모리 주소가 기록되어있는 테이블
* 실행엔진은 메서드 수행시, 바이트코드에 기록되어있는 런타임 상수 풀 인덱스를 기반으로 메모리에서 데이터를 찾아 연산 수행
```java
public void add(java.lang.String);  
Code:  
0: aload_0  
1: getfield #15;  //#15 : 런타임상수풀 인덱스
4: aload_1  
5: invokevirtual #23;
8: pop  
9: return  
```

* 각 클래스마다 개별적으로 생성됨

<br>

# 실행 엔진
* PC Register 를 참조하여 바이트 코드를 읽어와 명령어 단위로 수행
* 바이트 코드 = OpCode + 피연산자
	* OpCode : 바이트코드 명령어 (e.g. aload_0(데이터 로드), istore(데이터 저장), getfield(클래스 필드 getter), putfield(클래스 필드 setter))
	* 피연산자 : 런타임 상수풀 인덱스 (e.g. #15)
	```getfield #15;```
* 바이트코드를 읽어와 기계어로 변환하여 수행하며 기계어로 변환 방식으로 인터프리터 / JIT 컴파일러 존재 [[참고]](https://github.com/JisooOh94/study/blob/master/JAVA%EC%9D%98%20%EC%A0%95%EC%84%9D/Content/1.%20JAVA%20%EA%B8%B0%EC%B4%88.md#jit-%EC%BB%B4%ED%8C%8C%EC%9D%BC%EB%9F%AC)

### JIT 컴파일러 동작 과정

![image](https://user-images.githubusercontent.com/48702893/108588477-c7da5880-739c-11eb-8fcd-bb4ffabbaa33.png)

* 코드 최적화를 위해 바이트코드를 바이트코드와 네이티브코드의 중간단계 표현인 IR(Intermediate Representation) 로 변환
* Optimizer 에서 코드 최적화 수행
* Code Generator 에서 네이티브 코드로 변환하여 수행
* 코드 수행 횟수를 Profiler 에 기록 > 수행횟수가 많은 코드(Hotspot) 를 네이티브 코드로 컴파일하여 Method Area 에 캐싱