# Nested Loop Join
* 선행테이블(Driving Table) 의 처리범위를 하나씩 액세스하면서 그 추출된 값으로 연결할 테이블을 조인하는 방식

### 사용
* 데이터 처리량이 적고, 빠른 응답성이 필요한 OLTP 용 쿼리
	> 조인 성공시, 바로 조인결과를 사용자에게 보여줌

### 처리 과정

![image](https://user-images.githubusercontent.com/48702893/138546659-8684b339-b824-4d7b-9623-5cbeae3aca14.png)

1. 드라이빙 테이블에서 조건을 만족하는 row 탐색
2. 탐색된 드라이빙 테이블 row의 조인키 칼럼값으로 드리븐 테이블 탐색(인덱스 scan/테이블 full scan)
	* 인덱스를 통해 드리븐 테이블 탐색시, 탐색된 인덱스의 rowId 를 통해 드리븐 테이블 Random Access하여 row select
	* 드리븐 테이블에 사용가능한 인덱스가 없을경우, 테이블 full scan으로 동작
3. 드라이빙 테이블 selected row 와 드리븐 테이블 selected row Join 하여 추출버퍼에 삽입
4. 드리븐 테이블에 해당하는 row가 존재하지 않으면 드라이빙 테이블 row 생략한 후 다음 드라이빙 테이블 row 탐색 

### 성능 포인트
* 드라이빙 테이블에서 select 되는 row 수
	* 드라이빙 테이블의 조인 대상 row 수가 많을경우, 그만큼 드리븐 테이블 scan 횟수도 많아지므로 성능 저하 발생
* 드리븐 테이블의 조인 키 칼럼 인덱스 유무
	* 드리븐 테이블에 조인 키 칼럼 인덱스가 없을경우, 드리븐 테이블 full scan으로 동작하므로 성능 저하 발생
* 드리븐 테이블의 조인 대상 row 수
	* 드리븐 테이블의 조인 대상 row 수가 많을경우, 드리븐 테이블 랜덤엑세스 횟수도 많아지므로 성능저하 발생

### 성능 개선 방법
* 조인 테이블중, select 되는 row 수가 더 적은 테이블을 드라이빙 테이블로 설정
* 드리븐 테이블에 조인 키 칼럼 인덱스가 없을경우, 인덱스 추가
* 드라이빙 테이블/드리븐 테이블에서 조인 대상 row 수가 너무 많거나, 드리븐 테이블에 조인 키 칼럼 인덱스가 없을경우, Merge/Hash Join 검토 
* 드리븐 테이블로의 Random Access 를 줄이는 BKA(Batched Key Access)을 사용하거나 join 의 대상을 작은 block 으로 나누어 block 하나씩 join 하는 BNL(Block Nested Loop) 방식 으로 성능 개선 가능

<br>

# sorted Merge Join
* 양쪽 테이블의 처리범위를 각자 액세스하여 정렬한 결과를 차례로 스캔하면서 Join 칼럼 값으로 머지해 가는 방식
* PGA 영역에 저장하여 정렬을 수행하므로 경합 발생하지 않아 효율적

### 사용
* 조인 키 칼럼에 인덱스가 없는경우
* 조인 조건이 비 동등 조건(범위연산)인 경우
* 대용량의 데이터를 조인해야함으로서 인덱스 사용에 따른 랜덤엑세스의 오버헤드가 많은 경우
* 데이터 처리량이 많은 Batch 용 쿼리

### 처리과정

![image](https://user-images.githubusercontent.com/48702893/138583908-2d887c63-1af3-4127-beca-63386be6a5e0.png)

1. 선행 테이블에서 조건을 만족하는 row 탐색
2. 탐색된 선행 테이블 row들을 조인키 칼럼 기준으로 정렬
3. 후행 테이블에서 조건을 만족하는 row 탐색
4. 탐색된 후행 테이블 row들을 조인키 칼럼 기준으로 정렬
> 선행 테이블 scan,정렬 과 후행 테이블 scan,정렬은 동시에 독립적으로 수행됨
5. 정렬된 선행 후행 테이블의 row join 하여 추출버퍼에 삽입

### 성능 포인트
* 각 테이블 scan 속도
	* 테이블 scan에 사용가능한 인덱스가 없을경우 성능 저하 발생
* 각 테이블 정렬 속도
	* 테이블에서 select 된 row 전체 크기가 sort 메모리 크기보다 클 경우, 임시 파일을 만들어 정렬수행하므로 성능 저하 발생 
* 각 테이블에서 select 된 row 수의 차이
	* 정렬작업 수행시, 더 적게 select 된 테이블은 정렬이 먼저 끝나버려 더 많이 select 된 테이블의 정렬이 완료될떄까지 대기하는 비효율 발생 

### 성능 개선 방법
* 테이블 scan시, 인덱스가 없을 경우 인덱스 추가
* select 된 rows 정렬시, 임시파일에서 수행된다면 DMBS 의 SORT_AREA_SIZE 값을 조정하여 정렬이 메모리에서 수행되도록 튜닝
* 각 테이블에서 select 된 row 수의 차이가 크거나 select 된 row 수가 너무 많아 메모리에서 정렬이 불가능할 경우 Hash join 검토

<br>

# Hash Join
* 테이블의 조인 키 칼럼값을 기준으로 해쉬함수 적용하여 서로 동일한 해쉬값을 갖는것들 사이에서 실제 값이 같은지를 비교하면서 조인 수행
* NL Join 의 랜덤엑세스 부하 문제와 Sort Merge Join 의 정렬 부하 문제 해결
* PGA 영역에 해쉬테이블 저장하므로 경합 발생하지 않아 효율적
* 조인조건이 동등조건(=)인 경우에만 사용 가능
* 옵티마이저가 Cost Based Optimizer 인 경우에만 사용 가능

### 사용
* 조인 키 칼럼에 인덱스가 없는경우
* 대용량의 데이터를 조인해야함으로서 인덱스 사용에 따른 랜덤엑세스의 오버헤드가 큰 경우
* 대용량의 데이터를 조인해야함으로서 정렬 작업으로 인한 오버헤드가 큰 경우
* 데이터 처리량이 많은 Batch 용 쿼리

### 처리과정

![image](https://user-images.githubusercontent.com/48702893/138591168-b82e3d04-142f-425c-83b4-1130c5e379c7.png)

1. 조인 테이블에서 조건을 만족하는 row 탐색
> 선행 테이블 scan 과 후행 테이블 scan 은 동시에 독립적으로 수행
2. 탐색된 두 테이블의 rowSet 중 크기가 더 작은 rowSet을 선행테이블(Build Input)로 하여 조인키 칼럼값으로 해시테이블 생성 (Partitioning phase) 
3. 후행테이블(Prove Input)에서 조건을 만족하는 row 탐색
4. 후행테이블의 row 가 탐색되면 조인키 칼럼값을 해싱하여 선행테이블의 해시테이블에서 탐색 (Probing phase)
> 해쉬 테이블 생성 이후 NL 조인과 동일하게 순차적으로 처리(Driving : 후행테이블, Driven : 선행테이블)
5. 선행테이블 탐색 성공시 조인하여 추출버퍼에 삽입 후 다음 후행테이블 row 탐색 

### 성능 포인트
* 대용량 데이터 처리시 해쉬테이블의 크기가 메모리 사이즈(hash_area_size)보다 커질경우 임시영역에 저장하여 성능 저하 발생
* hash_area_size 를 지나치게 크게 할 경우, 메모리의 지나친 사용으로 오버헤드 발생, 다른 프로세스에게 악영향
* 선행 테이블의 조인키 킬럼값에 중복값이 많을경우, Probing phase 의 효율성 떨어짐  

### 성능 개선 방법
* 조인 대상 row 수가 적은 테이블을 선행 테이블로 설정
* hash_area_size 조정하여 해쉬테이블 메모리에 저장
* 조인 수행시 병렬 수행하여 조인 속도 향상

***

> Reference
> * http://www.jidum.com/jidums/view.do?jidumId=167
> * http://www.gurubee.net/lecture/2388
> * https://myjamong.tistory.com/238
> * https://www.youtube.com/watch?v=j1wi23tcGpU
> * https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=zx7024&logNo=60175836404
> * https://hoon93.tistory.com/46
> * https://needjarvis.tistory.com/162
> * https://hoing.io/archives/14457