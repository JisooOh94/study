# 인스턴스가 갑자기 죽었을때
### hs_err file 을 통해 JVM Crash 확인
* HotSpot Error의 약자로 자바 프로그램이 강제 종료되거나 시작되지 못하는 등의 치명적인 오류(JVM Crash)가 발생했을 때 그 원인을 알려주기 위해 생성
* 파일명 형태는 hs_err_pidXXXX.log와 같은데 XXXX에는 프로세스의 PID가 들어갑니다
* 해당 로그 파일은 애플리케이션이 설치된 경로에 생성
* 스레드 덤프, 메모리 사용량, 로드된 클래스, 라이브러리 목록 등을 포함해서 충돌에 대한 정보가 담겨있습니다. 예를 들어 자바 프로그램이 OutOfMemoryError로 강제 종료되는 경우 hs_err 파일에는 문제 발생 당시의 프로그램 메모리 사용량에 대한 정보가 기록됩니다. 또 이 문제가 메모리 누수로 발생했는지, 프로그램을 실행할 만큼 메모리가 충분치 않았는지 등을 알려줍니다.

> Ref
> * https://change-words.tistory.com/entry/hserr-log
> * https://www.whatap.io/ko/blog/28/

# dmesg 피일을 통해 커널 로그 확인
* dmesg(diagnostic message) 는 kernel 의 ring buffer(커널의 로그 메시지를 보관하는 물리적 메모리의 일부) 를 출력하는 명령어로 부팅시에 인식한 장치등 시스템 진단에 필요한 유용한 정보를 제공
* 시스템 부팅 메세지를 확인하는 명령어이다. 또한 커널에서 출력되는 메세지를 일정 수준 기록하는 버퍼 역할을 수행하며, 커널 부팅 중에 에러가 났다면 어느 단계에서 에러가 났는지 범위를 좁히고 찾아내는데 도움이 된다.
* 커널 부팅 메시지를 검사하고 하드웨어 관련 문제를 디버깅하는 데 유용
* dmesg 로그 파일 위치 : /var/log/dmesg
```
dmesg
dmesg -H    //human readable 하게 출력
dmesg -T    //사람이 읽을 수 있는 timestamp 출력
dmesg | less       //페이징
dmesg --level err     //레벨별 출력
dmesg -f auth      //facility 별 출력
```

> Ref
> * https://www.lesstif.com/lpt/log-dmesg-98926711.html


# /var/log/messages 파일을 통해 시스템 로그 확인
* 부팅시의 메시지를 포함해 전체 시스템의 로그를 기록합니다. 이 로그의 내용은 mail, cron, daemon, kern, auth 등의 시작, 종료, 엑티브 같은 것 입니다.
* 보안 문제 발생 시 가장 먼저 확인해 봐야하는 파일입니다. 예를 들어 버퍼 오버플로우 공격의 경우 타임스탬프와 함께 깨진 문자의 나열로 기록이 됩니다.

# 기타 시스템 로그 파일 및 분석 방법
https://blog.naver.com/kdi0373/220522832069
https://sisiblog.tistory.com/24