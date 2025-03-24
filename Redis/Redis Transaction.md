# Redis transaction
LettuceConnectionFactory에 보면 shareNativeConnection옵션이 있는데, 커넥션 공유 여부에 대한 설정이며 기본값은 true

shareNativeConnection=false로 주면 커넥션을 공유하지 않는 걸 확인 할 수 있다.

따라서 레디스 트랜잭션 실행 시, shareNativeConnection=false로 커넥션을 공유하지 않도록 할 수 있다. 그런데 lettuceConnectionFactory의 옵션값을 변경하면 비-트랜잭션 명령어도 커넥션을 공유하지 않게 되어 커넥션을 공유하는 레터스의 장점을 얻을 수 없게 된다. 만약 비-트랜잭션 명령어는 커넥션을 공유하게 하려면 lettuceConnectionFactory를 2개를 만들어야 한다.
그럼 어떻게 깔끔하게 스프링에서 레디스 트랜잭션 명령어를 전용 커넥션에서 실행하게 할 수 있을까?
사실 트랜잭션 명령의 경우, 스프링부트에서 알아서 전용 커넥션을 획득한다.

레디스 multi명령어를 호출하면 shareNativeConnection옵션과 무관하게 무조건 전용 커넥션을 획득하도록 해놨다.


레디스 database를 사용하는 경우에는 커넥션을 공유할 수 없습니다. 따라서 shareNativeConnection를 false로 줘야 합니다. 만약 레디스 database명령어를 (select) 공유 커넥션에서 사용하면 UnsupportedOperationException가 발생합니다.

5. 레터스 커넥션 풀

전용 커넥션을 획득하기 위해 매번 커넥션을 획득.반환하게 되면 비용이 많이 발생하기 때문에 커넥션 풀을 사용하여 비용을 절감할 수 있다.
스프링부트에서 레터스 커넥션 풀을 위한 옵션을 제공한다.

```java
spring.redis.lettuce.pool.max-active
spring.redis.lettuce.pool.max-idle
spring.redis.lettuce.pool.max-wait
spring.redis.lettuce.pool.min-idle
spring.redis.lettuce.pool.time-between-eviction-runs
```

lettuce.pool 옵션이 있으면 커넥션 풀이 생성되며, 전용 커넥션이 필요할 때 커넥션 풀이 사용된다.

!! 트랜잭션 명령어를 수행하기 위해서 커넥션 풀을 이용하는 것이기 때문에 pool의 max가 클 필요는 없다. 트랜잭션 명령어 수행 빈도를 따져서 적당한 풀 크기를 설정하자.!! (물론 shareNativeConnection을 false로 주면 모든 커넥션은 커넥션 풀에서 획득합니다.)




> Reference
> * https://jronin.tistory.com/126
