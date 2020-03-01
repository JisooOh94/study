# 설치 이슈
* redis-server로 서버 실행 후 ctrl-c 로 서버 종료시 다음과 같은 권한 오류 발생

```
failed opening the rdb file dump.rdb (in server root /hoem1/irteam/apps/redis-stable/src) for saving permission denied
```

* key-value 값을 저장할 데이터 파일에 대한 생성 및 쓰기 권한이 없다는 오류같아 명시한 경로에 dump.rdb 파일 생성후 권한을 755로 변경
* 해결 실패 후 파일의 소유자 및 소유자 그룹을 root로 변경하였으나 다시 실패
* redis-stable 디렉토리 전체의 권한을 755로 변경하여 해결
* redis 모든 파일들에 대한 읽기및쓰기권한이 완전히 열려있는 상태는 좋지 않아 추후 근본적인 해결 및 권한 수정 필요

# redis server 백그라운드 실행
* 명령행 파라미터 설정을 통해 실행 : ```redis-server --daemonize yes```
* 백그라운드 실행 확인 : ```ps aux | grep redis-server```

# redis.config 설정
* bind 파라미터를 통해 redis 서버 접근허용 ip 설정
* requirepass 파라미터를 통해 서버 접근 비밀번호 설정
[참고](https://jeong-pro.tistory.com/139)

# redis 보안설정
* 로컬 pc의 webapp 에서 다른 ip의 redis 서버에 접속시 permission denied 에러 발생
* redis 3.2 버전부터 default로 localhost를 제외한 다른 ip의 접근을 차단
* redis-cli에서 ```CONFIG SET protected-mode no```를 통해 외부접근 허용
* 반드시 bind 파라미터를 통해 접근허용 ip 설정

# redis template을 이용한 key value 저장 이슈
* redis template을 이용해 원격 redis 서버에 값 저장시 "\xac\xed\x00\x05t\x00\x0" 같은 값 함께 저장됨
* 직렬화 메소드 추가해주어야함(setKeySerializer(), setValueSerializer())