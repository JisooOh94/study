# GC 튜닝
* stop-the-world : JVM이 GC를 수행하기 위해 애플리케이션을 잠시 멈추는것
* GC 튜닝 : GC가 효율적으로 수행되게 하여 stop-the-world 시간을 최소화 하는것
> cf. 아무리 좋은 GC 알고리즘을 사용해도 stop-the-world는 무조건 발생

# Generation 기반 GC
* Java에서는 'weak generational hypotheses' 에 기초하여 세대 기반 GC를 기본 GC의 원리로 채택
* 메모리 공간을 객체 나이에 따른 young 영역과 old 영역으로 구분

### weak generational hypotheses(약한 세대 가설)
1. 대부분의 객체는 생성된 후 금방 접근 불가 상태(GC 대상)가 된다.
    > 생성된지 얼마 안된 젊은 객체들을 대상으로만 GC 를 더 자주 수행해주면 매번 모든 객체를 조사하여 GC 하는것보다 더 효율적
2. 오래된 객체에서 젊은 객체로의 참조는 거의 존재하지 않는다.
    > 젊은 객체간의 참조만 조사하여 GC 대상을 찾아냄으로서 매번 모든 객체들간의 참조를 조사하는것보다 더 효율적
  
    > 드물게 존재하는 오래된 객체에서 젊은 객체로의 참조는 따로 기록해둠으로서 모든 오래된 객체의 참조를 조사해야하는 비효율 제거

### young generation 영역
* 생성된지 얼마 안된 객체들이 저장되는 메모리 영역
* 약한세대가설의 1번 가설원칙에 따라 대부분의 젊은 객체들이 GC 대상이 되므로 GC 또한 빈번히 수행
* young generation 영역에서 수행되는 GC를 minor GC 라 명칭

### old generation 영역
* 생성된 후 참조가 계속해서 유지되어 접근 가능하다면 일정 시간 후에 young generation 영역에서 old generation 영역으로 옮겨짐
* young generation 영역보다 더 큰 메모리 공간을 가지고 있으며, 그에 따라 GC 는 적게 수행됨(GC 수행시간이 메모리 공간 크기에 비례하므로)
* old generation 영역에서 수행되는 GC 를 major GC 라 명칭

### (기타) permanent generation 영역
* 런타임에 참조되는 클래스의 메타데이터 저장 영역
* 런타임에 참조되는 새로운 클래스의 메타데이터를 저장할 영역이 부족하다면 Permanent Generation 영역에 대한 GC 수행(더이상 참조되고 있지 않은 클래스 메타데이터 삭제)
* permanent generation 영역에 대한 GC 는 Full GC (Major GC 를 포함한 Heap 영역 전체에 대한 GC) 에 포함되어있음.
* Java 8 부터 Metaspace로 대체됨

### white barrier
* 약산세대가설 2번 가설에 따라 old 영역에서 young 영역을 참조할경우 이를 기록해두어, minor gc의 성능을 개선하는 기법
* minor gc 수행시, young generation 영역에 저장되어있는 young 객체들의 참조가 살아있는지 확인하기 위해 이를 참조하고있는 다른 객체가 있는지 scan
* young generation 영역의 경우 크기가 작아 저장되어있느 모든 객체의 참조를 조사하는데에 오래걸리지 않으나, old generation 영역의 경우 매우 오래걸림
* 따라서 old 객체 > young 객체의 참조가 생성될경우, 이를 512 바이트의 chunk로 구성되어있는 카드테이블에 기록
* minor gc 시 모든 old 영역을 조사하는것이 아닌, 카드 테이블만 조사하여 young 객체 참조 유지 여부 확인

