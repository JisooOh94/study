# Random Access
* 한번의 Disk I/O 작업에, 하나의 디스크 블록만 read 하는것
* 비용이 비싼 Disk I/O 작업으로 여러개의 디스크 블록이 아닌, 하나의 디스크 블록만 읽어오므로 비효율적인 동작 방식
	> 테이블 full scan 의 경우, 한번의 Disk I/O 로 여러개의 디스크블록을 read(리드 어헤드)하여 처리하므로 인덱스 스캔방식에 비해 더 효율적인 경우가 있음
* 인덱스 scan 후 RowId 값으로 테이블 access시 random access 로 동작함
* 따라서 인덱스 생성 및 쿼리 튜닝시, Random Access 를 줄이는것이 중요

<br>

# Random Access 발생 경우
### 확인 랜덤 엑세스
* WHERE 절 칼럼중 인덱스에 없는 칼럼이 존재하는 경우
* 인덱스 칼럼 조건으로 필터링 후, 인덱스 RowId 로 Random Access 하여 테이블 데이터 확인해 나머지 칼럼 필터링
* Random Access 수행 횟수보다 쿼리 결과 row 수가 더 작을 수 있음
	* Random Access 를 통해 테이블 데이터 확인 후, WHERE 조건에 부합되지 않으면 펄티링되므로 Random Access 비용 낭비
* 쿼리 성능에 치명적이므로 최소화 필요

### 추출 랜덤 엑세스
* SELECT 절 칼럼중 인덱스에 없는 칼럼이 존재하는 경우
* 인덱스 RowId 로 Random Access하여 인덱스에 없는 칼럼 데이터 SELECT
* Random Access 수행 횟수 == 쿼리 결과 row 수

### 정렬 랜덤 엑세스 
* ORDER BY, GROUP BY 에 사용된 컬럼중 인덱스에 없는 칼럼이 존재하는 경우
* 인덱스 RowId 로 Random Access 하여 ORDER BY, GROUP BY 칼럼 데이터 select 하여 정렬 수행
* Random Access 수행 횟수 == 쿼리 결과 row 수

<br>

# Random Access 완화 방법
* 쿼리의 모든 칼럼이 포함된 새로운 인덱스 생성
* 랜덤 엑세스로 조회되는 칼럼을 기존 인덱스의 마지막에 추가
***
> Reference
> * http://www.gurubee.net/lecture/2230
> * http://www.gurubee.net/lecture/2235