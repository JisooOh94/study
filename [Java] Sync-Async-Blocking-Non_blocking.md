# Sync / Async
* 호출함수가 피호출함수 작업 완료를 확인하는지 여부

### Sync
* A 함수에서 B 함수 호출시, A 함수가 B 함수가 완료되었는지 확인후에 후처리 작업 수행하는 방식

### Async
* A 함수에서 B 함수 호출시, A 함수는 B 함수가 완료되었는지 신경쓰지 않고, B 함수 스스로 작업 완료시 후처리 작업(callback 메서드) 수행

<br>

# Blocking / Non-blocking
* 호출함수가 피호출함수 작업 완료를 기다리는지 여부

![image](https://user-images.githubusercontent.com/48702893/109412553-05b62d00-79ec-11eb-90fc-61281cd34277.png)

### Blocking
* A 함수에서 B 함수 호출시, B 함수의 작업이 모두 끝날때까지 A 함수가 대기하는 동작 방식 

### Non-blocking
* A 함수에서 B 함수 호출시, (다른 스레드에 의해)B 함수의 작업이 수행되는동안, (메인스레드가)A 함수도 대기하지 않고 다음 작업 계속 수행하는것
> Spring 에선 비동기 메서드 호출시, Spring Task Executor 가 자신의 스레드 풀에서 스레드 할당하여 수행

> Tip
> * 통상적으로 얘기하는 Sync, Async 에 Blocking, Non-Blocking 개념이 포함되어있음
> * 다만 두 개념의 관심사가 다름 (Sync-Async : 피호출함수 작업완료 확인주체, Blocking-Non Blocking : 피호출함수 작업완료 대기여부)
> * Non-Blocking 과 Async 모두 피호출함수 수행을 기다리지 않고 호출함수가 계속 수행됨
> * 다만 Non-Blocking 은 호출함수 수행 중간중간에 지속적으로 피호출함수의 작업완료를 확인
> * 이렇게 피호출함수 작업완료 체크 시점마다 Blocking 처럼 동작하게됨
> * Async 는 호출함수가 아예 피호출함수의 작업완료 확인하지 않으므로 진정한 의미의 비동기

<br>

# Sync, Asycn + Blocking, Non Blocking

![image](https://user-images.githubusercontent.com/48702893/109412849-ac4efd80-79ed-11eb-97e0-301d16e4e7fb.png)

### Sync - Blocking
* 호출함수가 피호출함수의 작업완료를 대기
* 호출함수가 피호출함수 작업완료를 체크하여 후처리 로직 수행
* 일반적인 메서드 호출

![image](https://user-images.githubusercontent.com/48702893/109412880-e4eed700-79ed-11eb-977e-a9b960318dca.png)


### Sync - Non Blocking
* 호출함수가 피호출함수의 작업완료를 기다리지않고 다음작업 수행
* 호출함수가 피호출함수 작업완료를 체크하여 후처리 로직 수행
* 호출함수가 Non-Blocking 으로 계속 자신의 작업 수행하는 중간중간 Blocking 하고 피호출함수의 작업완료를 확인해야하므로 불필요한 리소스가 낭비되어 완전한 비동기라 할 수 없음
	> cf) 폴링(Polling) : 작업이 완료되었는지 주기적으로 체크하는 방식
* callback 메서드를 등록하지 않고 비동기 메서드 호출 
 
![image](https://user-images.githubusercontent.com/48702893/109412875-ddc7c900-79ed-11eb-89a8-970374a1cc6a.png)

### Async - Blocking
* 호출함수가 피호출함수의 작업완료를 대기
* 호출함수는 피호출함수의 작업완료를 신경쓰지 않고, 피호출함수가 작업완료시 직접 후처리 로직(callback메서드) 수행
* Sync-Blocking 과 거의 동일
 
![image](https://user-images.githubusercontent.com/48702893/109412879-e1f3e680-79ed-11eb-944c-271d7f8332e9.png)

### Async - Non Blocking
* 호출함수가 피호출함수의 작업완료를 기다리지않고 다음작업 수행
* 호출함수는 피호출함수의 작업완료를 신경쓰지 않고, 피호출함수가 작업완료시 직접 후처리 로직(callback메서드) 수행
* 호출함수는 피호출함수의 작업을 기다리거나 확인하지 않고 계속 자신의 작업 수행하므로 완전한 비동기라 할 수 있음
* callback 메서드를 등록한 비동기 메서드 호출

![image](https://user-images.githubusercontent.com/48702893/109412882-e7513100-79ed-11eb-8d43-d18b2786486d.png)

> [이미지출처](http://homoefficio.github.io/2017/02/19/Blocking-NonBlocking-Synchronous-Asynchronous/)