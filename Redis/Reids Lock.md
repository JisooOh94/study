# Redis lock
https://mangkyu.tistory.com/311


* Hot key 문제 및 해결책
  Hot Keys: 하나의 키에 읽기가 집중될 때도 성능이 떨어질 수 있습니다. 위 글에서는 그 대책으로 키 이름 앞에 Prefix를 붙여 여러 복제본을 만든 뒤, 그 Prefix가 붙은 복제본에 랜덤으로 읽기를 분산시키는 방법을 소개하고 있습니다.

https://news.hada.io/topic?id=2777


# Redis 사용에 주의할 점
* 서버에 장애가 발생했을 경우 그에 대한 운영 플랜이 꼭 필요합니다.
    * 메모리 데이터 저장소의 특성상, 서버에 장애가 발생했을 경우 데이터 유실이 발생할 수 있기 때문입니다.
* 메모리 관리가 중요합니다.
* 싱글 스레드의 특성상, 한 번에 하나의 명령만 처리할 수 있습니다. 처리하는데 시간이 오래 걸리는 요청, 명령은 피해야 합니다.



https://velog.io/@injoon2019/Redis-%EB%A0%88%EB%94%94%EC%8A%A4-%ED%81%B4%EB%9D%BC%EC%9D%B4%EC%96%B8%ED%8A%B8%EC%99%80-%EB%B6%84%EC%82%B0%EB%9D%BD-6qg6tcwa
https://digitalbourgeois.tistory.com/m/266
https://velog.io/@pjh612/%EB%9D%BD%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%A0%9C%EC%96%B4%EC%97%90%EC%84%9C-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98-%EC%A0%84%ED%8C%8C-%EC%86%8D%EC%84%B1-%EC%A3%BC%EC%9D%98%EC%A0%90

https://esperer.tistory.com/65
