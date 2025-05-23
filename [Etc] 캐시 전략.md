# 캐시 읽기 전략
## Look-aside

![image](https://github.com/user-attachments/assets/941ef030-2d30-40d5-ba07-4a2340a8f7d7)

### 동작 방식
- 애플리케이션이 캐시를 먼저 확인. 캐시에 없으면 애플리케이션이 데이터베이스에서 데이터를 가져와 캐시에 쓰기 후 응답

### 장점
- 캐시에 장애가 발생하더라도 서비스엔 영향 없음

### 단점
- 캐시의 데이터와 데이터베이스의 데이터간 정합성이 일시적으로 불일치할 수 있음
- 캐싱되어있지 않은 데이터에 대한 다량의 조회요청으로 cache stempede 문제 발생 가능

### use-case
- 동일한 데이터에 대한 조회 요청이 많은 서비스
  - 조회될 확률이 높은 데이터를 미리 캐시에 쓰기하는 cache warming 을 통해 효율 향상 가능 
- 데이터의 strict 한 일관성 보장이 중요하지 않은 서비스

## Read-through

![image](https://github.com/user-attachments/assets/1f616398-2906-4064-99b2-8ea2d9532c4b)

### 동작 방식
* 애플리케이션이 데이터 조회시, 캐시 먼저 조회. 캐시에 없으면 캐시가 데이터 쓰기소에서 데이터를 가져와 캐시에 쓰기하고 애플리케이션에 반환.

### 장점
* Look aside 에 비해 데이터베이스와 캐시간 데이터 일관성을 더 잘 보장해줌(cache-control 헤더를 통해...)
* 데이터베이스 조회를 캐시 서비스에서 대신하므로 어플리케이션 코드가 간단해짐

### 단점
* 캐시 서비스에 장애 발생시, 서비스 불가능
* 캐시 서비스에서 데이터베이스를 조회하므로 캐시 서비스에따라 성능이 떨어질 수 있음

### use-case
- 동일한 데이터에 대한 조회 요청이 많은 서비스
  - 조회될 확률이 높은 데이터를 미리 캐시에 쓰기하는 cache warming 을 통해 효율 향상 가능
- 어플리케이션 코드를 간단하게 작성하고자 할 경우


# 캐시 쓰기 전략

## Write-through

![image](https://github.com/user-attachments/assets/0bc585a1-c979-4801-b200-1142bcbf3b08)

### 동작 방식
- 애플리케이션이 데이터를 캐시에 쓰기, 캐시가 데이터 쓰기소에 데이터를 전달하여 쓰기.

### 장점
- 데이터베이스와 캐시간 데이터 일관성을 항상 보장

### 단점
- 데이터 쓰기 시 캐시와 데이터 쓰기소에 모두 쓰기 때문에 지연 발생 및 부하 증가

### 사용 사례
- 데이터의 일관성이 매우 중요한 서비스
- 쓰기 요청이 많지 않은 서비스


## Write-back

![image](https://github.com/user-attachments/assets/2fe5bf81-83d7-4905-a0d6-6dc9b9e7d9d7)

### 동작 방식
- 애플리케이션은 데이터를 캐시에만 쓰기 수행, 캐시가 데이터배이스에 저장해야할 데이터를 모아뒀다가 주기적으로 bulk 로 쓰기 수행

### 장점
- bulk 로 데이터베이스에 쓰기 수행하므로 쓰기 성능 뛰어남

### 단점
- 캐시 장애 발생시, 미쳐 데이터베이스에 쓰여지지 않은 데이터들 유실 가능

### 사용 사례
- 쓰기 작업이 빈번한 서비스

## Write-around

![image](https://github.com/user-attachments/assets/7ec50dae-9f3c-413a-811b-9c18199ce417)

### 동작 방식
- 애플리케이션이 데이터를 데이터베이스에만 쓰기 수행, 캐시엔 저장하지 않음
  - 데이터 조회시 cache-miss 인 경우에만 캐시에 저장됨

### 장점
- 데이터베이스에만 쓰기를 수행하므로 쓰기 속도가 가장 빠름

### 단점
- 데이터베이스의 데이터와 캐시 데이터간 불일치 확률이 높음
    - 캐시의 ttl 을 짧게 설정하는 등의 조치 필요 

### 사용 사례
- 주로 Look aside, Read through 등의 캐시 읽기 전략과 결합되어 사용됨
- 한번 저장되고, 조회될 일이 거의 없는 불필요한 리소스 저장등에 주로 사용
- 데이터의 strict 한 일관성 보장이 중요하지 않은 서비스


> Reference
> * https://inpa.tistory.com/entry/REDIS-%F0%9F%93%9A-%EC%BA%90%EC%8B%9CCache-%EC%84%A4%EA%B3%84-%EC%A0%84%EB%9E%B5-%EC%A7%80%EC%B9%A8-%EC%B4%9D%EC%A0%95%EB%A6%AC
