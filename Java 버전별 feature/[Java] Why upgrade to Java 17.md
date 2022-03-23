# Why upgrade to Java 17
* 이전 LTS 버전인 Java 11은 2024/10 에 지원이 종료되는 반면, Java 17 은 2026년까지 지원 예정 
* Java 15 에 release 된 차세대 GC 알고리즘(ZGC) 사용 가능
* 프레임워크 및 써드파티 라이브러리 최신버전으로 업그레이드 하기에도 용이 (e.g. SPringboot3의 최소 요구사항은 Java 17 이상버전)

<br>

# 고려사항
### System clock has changed to nanosecond precision
* Java 에서 사용하는 System clokc 정밀도가 ms 에서 ns 로 정밀화
* 이에따라, 시간 관련 라이브러리 및 데이터 타입의 표현범위도 ns 까지 늘어났으므로 타 라이브러리나 db 의 시간 데이터 조회하여 사용시 주의 필요
> e.g) db 의 시간 타입 칼럼이 ns 단위 정밀도를 지원하지 않는경우, java 에서 db 에 시간 데이터 insert 후, 다시 조회하여 비교시 값이 달라질 수 있음

### MaxPermSize' option is removed in Java 17
* Jvm 옵션중 하나인 MaxPermSize 가 삭제되어, 기존 실행 옵션에 포함되어있었다면 Jvm 실행 실패 

# Removal of Old Features from javadoc Tool
* Javadoc tool 에서 몇가지 기능이 삭제됨
1. HTML 4 를 이용한 문서 생성
2. HTML frames 를 이용한 문서 생성
3. --no-module-directories 옵션

### Remove the Concurrent Mark Sweep (CMS) Garbage Collector
* CMS GC 삭제

### Removal of Pack200 Tools and API
* java 11 에서 derprecated 되었떤 Pack200 tool 이 java 14 에서 삭제됨
* 그에따라 java.util.jar.Pack200 패키지 및 그 하위 클래스, 기능 사용 불가

### 그외 기타
* Removal of RMI Static Stub Compiler (rmic) Tool
* Removal of Nashorn JavaScript Engine
* Removal of Legacy Elliptic Curves
* Removed Root Certificates with 1024-bit Keys
* Removed RMI Activation

***

> Reference
> https://wiki.linecorp.com/display/EC/Upgrade+to+Java+17
> https://docs.oracle.com/en/java/javase/17/migrate/migrating-jdk-8-later-jdk-releases.html#GUID-7744EF96-5899-4FB2-B34E-86D49B2E89B6