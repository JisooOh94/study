# Lock 종류
## 용도에 따른 분류
### Shared Lock
* Row-level lock
* Locking SELECT 위한 read lock
* shared lock이 걸려있는 동안 다른 트랜잭션이 해당 row에 대해 write 불가능(Exclusive Lock 획득 불가능), read 가능(Shared Lock 획득 가능)

### Exclusive Lock
* Row-level lock
* UPDATE, DELETE 위한 write lock
* exclusive lock이 걸려있으면 다른 트랜잭션이 해당 row에 대해 read, write 모두 불가능(Shared Lock, Exclusive Lock 모두 획득 불가능)

### Intention Lock
* Table-level lock
* Intention Shared Lock(IS), Intention Exclusive Lock(Ix)
* 트랜잭션에서 Row-level Lock(S Lock, X Lock) 획득시, 그전에 먼저 Table-level Lock(IS Lock, IX Lock) 부터 획득해야함
* 테이블에 Row-level 쿼리가 수행되고 있음을 타 트랜잭션에게 알려주어 테이블을 수정(LOCK TABLES, ALTER TABLE, DROP TABLE)하려는 트랜잭션을 대기시키는 용도
	* 타 트랜잭션에서 테이블 수정을 위해 Table-level X Lock 을 걸려고 할시, 테이블에 IS/IX Lock 이 걸려있는지만 확인하면 됨
	* Intention Lock 이 없다면 테이블 전체 row 를 돌며 Lock(S/X) 이 걸려있는지 확인하는 비효율 발생

## 적용 부분에 따른 분류
### Record Lock
* 하나의 인덱스 레코드에 거는 Lock

### Gap Lock
* 인덱스 레코드 자체가 아닌, 레코드 사이의 Gap에 거는 Lock
* 트랜잭션 처리중, 다른 트랜잭션에 의해 Gap 에 새로운 레코드가 추가되어 트랜잭션내 동일 select 쿼리의 결과가 달라지는 phantom read 현상 방지 
* range scan 일경우에만 Gap Lock 사용(유니크 인덱스를 통한 단일 레코드 select 일 경우 Gap Lock X, Record Lock 만 사용)

### Next-Key Lock
* Record Lock + Gap Lock
* range scan 시 사용(scan되는 레코드에 Record Lock, 그 사이의 Gap 들에 Gap Lock 설정)

### Insert Intention Lock
* Insert 쿼리 수행시 사용되는 Gap Lock
* 동일한 Gap 에 insert 하는 쿼리라도, index key 가 다르다면, Lock 대기 없이 insert 동시 수행
	> e.g. pk 3 과 6 사이에 pk 4 와 5 인 새로운 레코드를 insert 하려는 A,B 쿼리의 동작과정
	> * A 트랜잭션이 인덱스 키 3 과 6 사이의 gap 에 Insert Intention Lock 획득 후 insert 수행
	> * B 트랜잭션이 인덱스 키 3 과 6 사이의 gap 에 Insert Intention Lock 이 걸려있음을 확인, 수행중인 A 트랜잭션 insert 쿼리의 인덱스 키 체크
	> * 인덱스 키가 다름을 확인 후 B 트랜잭션도 Insert Intention Lock 획득 후 insert 수행

### AUTO-INC Lock
* Auto increment 칼럼이 있는 테이블에 insert 쿼리가 동시에 여러개 요청될경우, Auto increment 칼럼값을 일관되게 증가시키기 위한 Lock
 
<br>

# 쿼리별 적용되는 Lock
### Consistent SELECT
* Lock 걸지 않음(일반 Select 쿼리)

### Locking SELECT
* Select 하는 레코드(+Gap) 에 Lock 설정 
* SELECT IN SHARE MODE : Shared Record/Next-Key Lock (읽기 o, 쓰기 x)
* SELECT FOR UPDATE : Exclusive Record/Next-Key Lock(읽기 x, 쓰기 x)
	> Consistent Select 는 모든 Lock 을 무시하므로 Exclusive Lock이 걸려있어도 read 가능

### INSERT
* Insert 하는 Gap 에 Insert Intention Lock 설정
* Insert 하는 레코드에 Exclusive Record Lock 설정

### UPDATE
* Update 대상 레코드에 Exclusive Record/Next-Key Lock 설정

### DELETE
* Delete 대상 레코드에 Exclusive Record/Next-Key Lock 설정

***
> Reference <br>
> * https://www.letmecompile.com/mysql-innodb-lock-deadlock/
> * https://kukuta.tistory.com/215
> * https://cecil1018.wordpress.com/2016/06/18/mysql-innodb-locks/
> * https://singun.github.io/2019/03/10/mysql-innodb-locking/