# Collection

## List
### ArrayList
* 내부적으로 Object 배열 이용
```java
transient Object[] elementData; // non-private to simplify nested class access
```
* 배열을 사용하므로 조회 성능 좋음(O(1))
* 배열을 사용하므로 추가, 삽입, 삭제 성능 떨어짐 (배열 복사 수행됨)
```java
//추가
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  //현재 Object 배열에 빈공간이 존재하는지 확인
    elementData[size++] = e;
    return true;
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;
    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);		//Object 배열에 빈공간이 없을경우 배열크기 늘리기 수행 
}

private void grow(int minCapacity) {
	// overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);		//배열 크기를 50% 만큼 늘린다.(4 > 6, 7 > 10)
    if (newCapacity - minCapacity < 0)
    	newCapacity = minCapacity;
	if (newCapacity - MAX_ARRAY_SIZE > 0)
    	newCapacity = hugeCapacity(minCapacity);
    
	elementData = Arrays.copyOf(elementData, newCapacity);		//새로운 크기의 배열에 데이터 복사 수행(성능저하 원인)
}

//삽입
public void add(int index, E element) {
    rangeCheckForAdd(index);
    ensureCapacityInternal(size + 1);  //현재 Object 배열에 빈공간이 존재하는지 확인후, 없다면 배열 크기 증대 수행(배열 복사 수행되어 성능 저하)
    System.arraycopy(elementData, index, elementData, index + 1, size - index);	//삽입을 위해 배열 복사가 한번 더 수행되어 성능 더 안좋아짐
    elementData[index] = element;
    size++;
}

```
* thread safe 하지 않음
* 시간 복잡도 : 추가 O(n), 삽입 O(n), 삭제 O(n), 조회 O(1),  

### Vector
* 대용량 처리를 위해 구버전 Java 에서 사용되던 Collection
* List 보다 성능이 떨어지므로 사용 지양
* 동기화처리가 되어있어 thread safe 보장
```java
//추가, synchronized 처리 로 동시성 보장
public synchronized void addElement(E obj) {
        modCount++;
        ensureCapacityHelper(elementCount + 1);
        elementData[elementCount++] = obj;
    }
```

### LinkedList
* 내부적으로 노드와 next 포인터 이용
* 포인터만 수정해주면 되어 데이터 추가, 삽입, 삭제 성능 좋음
```java
public boolean add(E e) {
    linkLast(e);
    return true;
}

void linkLast(E e) {
    final Node<E> l = last;
    final Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    if (l == null)
        first = newNode;
    else
        l.next = newNode;		//마지막 노드의 next 를 새로 생성한 node 로 지정
}
```

* 데이터 조회시 head 노드 부터 순차적으로 접근하여 조회하므로 조회 성능 떨어짐
```java
public E get(int index) {
    checkElementIndex(index);
    return node(index).item;
}

Node<E> node(int index) {
    // assert isElementIndex(index);
    if (index < (size >> 1)) {		//조회하고자 하는 index 가 리스트의 중간 index 보다 작으면, first 노드에서부터, 크면 last 노드에서부터 역순으로 탐색
        Node<E> x = first;
        for (int i = 0; i < index; i++) //first 노드부터 순차적으로 노드 조회
            x = x.next;
        return x;
    } else {
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)	////last노드부터 역순으로 노드 조회
            x = x.prev;
        return x;
    }
}
```
* thread safe 하지 않음
* 시간복잡도 : 추가 O(1), 삽입 O(1), 삭제 O(1), 조회 O(n)

## Set
### HashSet
* 내부적으로 HashMap 사용
```java
public HashSet() {
    map = new HashMap<>();
}
```
* thread safe 하지 않음
* 시간복잡도 : 추가 O(1), 조회 O(1)

### LinkedHashSet
* 순서보장 HashSet
* 내부적으로 LinkedHashMap 이용
```java
HashSet(int initialCapacity, float loadFactor, boolean dummy) {
    map = new LinkedHashMap<>(initialCapacity, loadFactor);
}
```
* thread safe 하지 않음

### TreeSet
* 정렬 보장 HashSet
* 이진 탐색 트리 구조
* 내부적으로 TreeMap 이용
```java
public TreeSet() {
    this(new TreeMap<E,Object>());
}
```
* thread-safe 하지 않음
* 시간복잡도 : 추가 O(log n), 조회 O(log n) 

### EnumSet
* Enum 타입 데이터를 저장하는 Set
* Enum 은 signleton 객체 이므로 해싱 수행하지 않아 성능 좋음

## Map
### HashMap
* 내부적으로 chaining 방식으로 collision 을 처리하므로 동일한 키값에 대해선 hashCode()와 equlas() 결과값이 모두 동일해야함

![image](https://user-images.githubusercontent.com/48702893/148090992-d9c709f7-9aad-4460-99d3-a1ab229ce7aa.png)

* thread-safe 하지 않음
* 시간복잡도 : 조회 O(1), 추가 O(1)
 
### LinkedHashMap
* 순서보장 HashMap

### HashTable
* HashMap과 동일한 구조를 가지나 동기화 처리 되어있어 성능 떨어짐
* 동기화 처리 되어있어 thread safe 보장
```java
public synchronized V get(Object key) {
    Entry<?,?> tab[] = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    for (Entry<?,?> e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && e.key.equals(key)) {
            return (V)e.value;
        }
    }
    return null;
}
```

### TreeMap
* 정렬 보장 HashMap
* 이진탐색 트리 구조로 key값으로 오름차순 정렬되어 저장

![image](https://user-images.githubusercontent.com/48702893/148091027-f6502b67-57f0-4109-a8eb-4a584990850b.png) 

* thread-safe 하지 않음
* 시간복잡도 : 조회 O(log n), 추가 O(log n)

***
> Reference
> * https://bangu4.tistory.com/205?category=1003336