# Rolling
* 서버를 한 대씩 구 버전에서 새 버전으로 교체해가는 전략

### 장점
* 서버 수의 제약이 있을 경우에 유용

### 단점
* 배포 중 인스턴스의 수가 감소 되기 때문에 서버 처리 용량을 미리 고려

<img src="https://user-images.githubusercontent.com/48702893/140928627-f0552f72-7c0e-4d56-9043-0a999b9f1980.png" width="400" height="200" />

<br>

# Blue-Green
* 구 버전에서 신 버전으로 일제히 전환하는 전략
* 구 버전, 신 버전 서버를 동시에 나란히 구성, 배포 시점에 트래픽이 일제히 전환
* 구 버전 : 블루, 신 버전 : 그린

### 장점
* 운영 환경에 영향을 주지 않고 실제 리얼 환경으로 새 버전 테스트 가능
* 하나의 버전만 프로덕션 되어 버전 관리 문제 방지
* 빠른 롤백

### 단점
* 구 버전, 신 버전 두 개의 환경 구성하여야하므로 시스템 자원 두 배로 필요

<img src="https://user-images.githubusercontent.com/48702893/140928687-0a373be3-1255-43b2-8242-92896a4e949a.png" width="400" height="200" />

<br>

# Canary
* 가동 중인 서버들중 일부에만 신버전 배포, 일부 트래픽을 신 버전의 환경으로 분산, 오류 여부 확인 (정식 출시에 앞서 신규 서비스를 검증)
* 일정기간 테스트 후, 신 버전 환경에서 문제 없을시, 나머지 서버로 신버전 배포 서서히 늘려감(문제 발생시 롤백)  

### 장점
* 리얼 환경에서 A/B 테스트 가능(버그 및 병목현상 검출)
* 오류 검출 및 성능 모니터링에 용이

### 단점
* 동시에 여러개의 어플리케이션 버전 관리로 인한 복잡도 증가

<img src="https://user-images.githubusercontent.com/48702893/140928716-d32fc2f2-45f7-452e-a41c-25f198a7c2db.png" width="400" height="200" />

***
> Reference
> * https://poikilo.github.io/2020/03/03/deployment-strategy/
> * https://www.ciokorea.com/t/544/bi/157642
> * https://dev.classmethod.jp/articles/ci-cd-deployment-strategies-kr/
> * https://reference-m1.tistory.com/211