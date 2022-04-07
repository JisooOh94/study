# Facade
* according to GOF - 복잡하게 얽혀있는것을 정리해서 높은 레벨의 인터페이스, 단순한 인터페이스를 외부에 제공하는 패턴
* In my opinion - 클래스의 덩치가 커져 api 가 많아졋을때, 함께 호출되는 api 들을 묶어 별도 caller 클래스로 분리함으로서 클래스와 호출부의 결합도와 복잡도를 낮춰 유지보수에 용이하게 만들어주는 패턴
```java
//AS-IS
public void doSomethingWithUserInfo(String userId) {
    User userInfo = localCache.get(userId);
    if(userInfo == null) {
        userInfo =  redisTemplate.opsForValue().get(KEY_PREFIX + userId);
        if(userInfo == null) {
            userInfo = userDao.getUserInfo(userId);

            redisTemplate.opsForValue().set(KEY_PREFIX + userId, userInfo);
            localCache.set(userId, userInfo);
        }
    }
    
    ... do something with userInfo
}

//TO-BE
public class UserRepository {
    public UserInfo getUserInfo(String userId) {
        User userInfo = localCache.get(userId);
        if(userInfo == null) {
            userInfo =  redisTemplate.opsForValue().get(KEY_PREFIX + userId);
            if(userInfo == null) {
                userInfo = userDao.getUserInfo(userId);
                redisTemplate.opsForValue().set(KEY_PREFIX + userId, userInfo);
                localCache.set(userId, userInfo);
                }
            }
        return userInfo;
    }
}

// use class
public void doSomethingWithUserInfo(String userId) {
    UserInfo userInfo = userRepository.getUserInfo(userId);

    ... do something with userInfo
}
```
* 사용부 입장에서 기존에 호출해야했던 여러개의 api 에서, caller 클래스의 api 하나만 호출하면 되므로 사용하기 더 쉬워지고 버그 발생 확률도 줄어듬
    * 사용부에선 UserRepository 의 getUserInfo 만 호출하면 되므로 사용이 쉬워지고, 버그 발생 확률이 줄어듬
* 사용부에서 기존에 호출하던 여러개의 api를, 동일한 기능을 하기 위해 caller 클래스의 api 하나만 호출하면 되므로, 결합이 느슨해지고 의존성이 약해져 유지보수에 용이해짐
    * 사용부에선 UserRepository에만 의존성을 가지게되므로, UserInfo 조회 로직 수정 및 유지보수에 더 용이해짐 