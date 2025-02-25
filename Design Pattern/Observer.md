# Observer
* Publisher(Observable) 와 Subscriber(Observer) 로 구성
* Publisher 는 이벤트 발생시, 자신을 구독하고있는 모든 Subscriber 들에게 데이터 전송
* Subscriber 들은 데이터 수신시, 그에맞는 처리 수행

```java
public interface Publisher
{
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}

public interface Subscriber
{
    void update(int value);
}
```

### 옵저버 패턴의 단점
* Publisher 가 Subscriber 에게 데이터를 밀어넣는 Push 방식이기 때문에 데이터 전송시 Subscriber 상태를 고려하지 않음
* Subscriber 의 처리율을 초과하는 양의 데이터 전송시, Subsciber 의 버퍼에 적체되다 버퍼 용량 초과시 장애 발생
    * 고정길이버퍼 : 신규로 수신되는 데이터 거절, Pubisher 는 재전송 하게되며 이 과정에서 추가 비용(네트워크 트래픽, CPU 연산) 발생
    * 가변길이버퍼 : Out of Memory 에러 발생, 다량의 GC 발생하여 웹어플리케이션 프로세스 사망

### 백프레셔
* Subscriber 가 필요시 Publisher 에게 데이터를 요청하는 Pull 방식
    * 이벤트 발생시 Publisher 는 Subscriber 들에게 이벤트가 발생했다는것 만 알림(데이터 전송x)
    * noti 를 받은 Subscriber 들이 Publisher 에게 데이터 요청
* Subscriber 가 Publisher 에게 데이터 요청시, 자신이 현재 처리 가능한 양만큼만 요청하기때문에 옵저버 패턴의 문제 해결

<img src="https://user-images.githubusercontent.com/48702893/129479369-7a628620-f306-4697-9e23-f503009ac45b.png" width="400" height="400">
