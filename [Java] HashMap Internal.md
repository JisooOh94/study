# HashMap Internal
### Collision handling
* `put` 수행할때 Collistion 발생시, `equals()` 메서드를 통해 실제 값 비교하여 bucket 내에 이미 존재하는 entry 인지 확인
* `get` 수행할때도 마찬가지로, `hashcode()` 로 bucket 을 찾은후, bucket 내에서 조회할때는 `equals()` 메서드로 실제 값 비교하여 조히
* 따라서 `hashcode()` override 시 `equals()` 까지 함께 override 필요

### Seperate Chaining
* Seperate Chaining 을 이용하여 Collision control
* 데이터 개수가 적을경우 Open Addressing의 성능이 더 좋으나(연속된 공간에 데이터를 저장하기 때문에), 일정 개수 이상 많아지면 Seperate Chaining이 더 효율적
	* 데이터 개수가 많아질수록, 캐시 적중률이 떨어지고, 캐시 탐색의 Worst Case 발생 빈도 증가
	* 연속된 공간에 저장하므로, 데이터 수정 및 삭제 성능 떨어짐

### Tree 구조 chaining
* Java 8 부터, 하나의 해시 버킷 내의 데이터 개수가 일정 개수 이상이 되면, LinkedList 에서 Tree 로 Chainig 구조 변경
	* 데이터 개수가 8개로 증가할 경우 LinkedList > Tree
	* 데이터 개수가 6개로 감소할 경우 Tree > LinkedList
	> Tree 는 LinkedList 에 비해 메모리 사용량도 많고, 데이터 개수가 적을경우 유의미한 성능차이도 없어, 8개 이하일경우 LinkedList 로 Chaining
* 이를 통해, 버킷 내 해시 탐색 성능이 O(n) > O(logn) 으로 향상
* Tree 구현체로 Red-Black Tree 를 사용하고, HashMap 의 Entry 구현체로 Entry 대신 Node 클래스 사용(Tree의 Entry 로도 사용가능)
* Red-Black Tree 탐색시, 데이터 크기 비교는 해시 키 값으로 비교하고, 그로인해 발생하는 Total Ordering 문제를 tieBreakOrder() 메서드로 해결

### 보조 해시 함수
* 데이터 개수가 해시 버킷 개수 임계값에 도달할 경우, 자동으로 해시 버킷 개수를 두배씩 증가
	* 버킷 개수 입계값 : 데이터 개수 * load factor (기본 크기 : 16, load factor : 0.75)
* 해시 버킷 개수가 2의 승수가 되므로, Hashing function 의 uniqueness 가 떨어지고 collision 발생 빈도가 늘어남 (해시 버킷 개수가 소수개 일때 가장 uniquness 좋음)
	* X.hashCode() 가 int 의 32bit 자리를 고루 사용하여 해시코드를 생성하여도, % M 하는 과정에서 하위 몇개의 비트만 사용하게되어 최종적으로 반환하는 해시코드의 uniquness 떨어짐
	* M 이 2의 승수이므로, X.hashCode 를 M 으로 나눈 나머지값, 즉 X.hashCode 의 하위 몇개의 비트로 이루어진 값으로 반환
	```java
	int index = X.hashCode() % M;	//X : 저장할 데이터, M : 버킷 개수 
	```  
* Java 4 부터 이러한 문제를 해결하기 위해 key 의 hashcode 값을 bucket 개수로 나누는것이 아닌, 보조함수를 이용해 처리
* Java 8 부터 Tree Chaining을 도입하면서, hash 충돌로 인한 성능저하가 완화되어 보조함수도 간단해짐
```java
static final int hash(Object key) {
	int h;
	return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
``` 

### 버킷 크기 초기화
* 데이터 개수가 해시 버킷 개수 임계값에 도달할 경우, 자동으로 해시 버킷 개수를 두배씩 증가
* 이때, 기존 HashMap 의 모든 데이터를 읽어 새로운 HashMap 으로 복사하는 높은 부하의 작업 수행됨
* 따라서, HashMap 에 저장될 데이터 개수를 예측할 수 있다면, HashMap 선언시, 초기 크기를 지정하여 버킷 확장이 일어나지 않도록 하는것이 효율적
> 일반적으로 적절한 초기 크기 설정시, 약 2.5배 정도의 성능 향상 가능 

*** 
> Reference
> * https://d2.naver.com/helloworld/831311
> * https://johngrib.github.io/wiki/java8-performance-improvement-for-hashmap/
