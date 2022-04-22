# 성능 지표 명령어
# top
* 리눅스 시스템의 전반적인 상황을 모니터링

![image](https://user-images.githubusercontent.com/48702893/164175176-47972c5a-8e1e-4b9b-a641-9edbce3fa8bc.png)
 
```shell
$ top
top - 00:15:40 up 7 days, 21:56,  1 user,  load average: 31.09, 29.87, 29.92
Tasks: 871 total,   1 running, 868 sleeping,   0 stopped,   2 zombie
%Cpu(s): 96.8 us,  0.4 sy,  0.0 ni,  2.7 id,  0.1 wa,  0.0 hi,  0.0 si,  0.0 st
KiB Mem:  25190241+total, 24921688 used, 22698073+free,    60448 buffers
KiB Swap:        0 total,        0 used,        0 free.   554208 cached

   PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND
 20248 root      20   0  0.227t 0.012t  18748 S  3090  5.2  29812:58 java
  4213 root      20   0 2722544  64640  44232 S  23.5  0.0 233:35.37 mesos-slave
 66128 titancl+  20   0   24344   2332   1172 R   1.0  0.0   0:00.07 top
  5235 root      20   0 38.227g 547004  49996 S   0.7  0.2   2:02.74 java
  4299 root      20   0 20.015g 2.682g  16836 S   0.3  1.1  33:14.42 java
     1 root      20   0   33620   2920   1496 S   0.0  0.0   0:03.82 init
     2 root      20   0       0      0      0 S   0.0  0.0   0:00.02 kthreadd
     3 root      20   0       0      0      0 S   0.0  0.0   0:05.35 ksoftirqd/0
     5 root       0 -20       0      0      0 S   0.0  0.0   0:00.00 kworker/0:0H
     6 root      20   0       0      0      0 S   0.0  0.0   0:06.94 kworker/u256:0
     8 root      20   0       0      0      0 S   0.0  0.0   2:38.05 rcu_sched
```

### Columns
* top : 현재 서버의 시간 및 서버가 구동된 이후 경과 시간 (7일 21시간 56분 경과)
* user : 접속해있는 사용자 수
* load average
  * 부하율. 1분, 5분, 15분 간의 평균 CPU 사용률
  * 1.0 이 100% CPU 를 사용중이라는 의미이며, 멀티코어일경우, 1.0 * 코어수 가 전체 CPU 가용량 (e.g. 4코어 일경우 4.0 이 모든 CPU 를 100% 사용하고있다는 의미)
  * load average 가 최대 CPU 가용량(1.0 * 코어수) 보다 큰경우, 실행되지 못하고 대기중인 프로세스가 있다는 의미
  * 1분 평균 CPU 사용률이 15분 평균 값보다 크다면, 부하가 진행중이라는 의미
* Task : 전체 가동 중인 프로세스 개수
  * running : 실행중인 프로세스
  * sleeping : 대기중인 프로세스
  * stopped : 종료된 프레세스
  * zombie : 좀비상태인 프로세스 (부모 프로세스가 종료된 자식 프로세스)
* CPU
    * us : 유저 모드에서 사용되는 cpu 비중 (%)
    * sy
      * 커널 모드에서 사용되는 cpu 비중 (%)
      * 커널 모드 부하가 높은경우, strace 를 통해 애플리케이션의 systemcall 호출 분석, 어플리케이션을 수정하거나 커널 파라미터 튜닝을 통해 부하 완화
    * id : 유휴상태의 cpu 비중 (%)
    * wa
        * I/O 요청 후 blocking 되어 응답을 기다리는 cpu 비중
        * iowait가 높은 경우, iostat 명령어를 통해 디스크 I/O 상황 분석
    * hi : 하드웨어 인터럽트에 사용되는 cpu 비중
    * si : 소프트웨어 인터럽트에 사용되는 cpu 비중
    * st : cpu 를 vm 에서 사용하여 대기하는 cpu 비중, 가상 CPU를 서비스 하는 동안 실제 CPU를 차지한 시간
* KiB Mem, KiB Swap
    * total : 전체 물리 메모리
    * free : 사용되지 않은 여유 메모리
    * used : 사용중인 메모리
    * buffers : I/O 작업에 사용되는 커널 버퍼로 쓰는 메모리
    * cached : disk의 페이지 캐시에 사용되는 메모리
* Process  정보
    * PID : 프로세스 ID
    * USER : 프로세스를 실행시킨 사용자 ID
    * PR : 커널에 의해 스케쥴링 되는 우선순위
    * NI : PR 값 산출 파라미터(NICE 값, 마이너스면 우선순위높은 일)
    * VIRT : 현재 프로세스가 사용하고 있는 가상 메모리 양으로서 소비하고있는 총 메모리 크기
    * RES : 현재 프로세스가 사용하고 있는 물리 메모리 양
    * SHR : 다른 프로세스와 공유하고 있는 공유 메모리 양
    * S : 프로세스의 상태
      * S : sleeping (요청한 리소스를 즉시 사용 가능)
      * R : running (실행 중)
      * W : swapped out process
      * Z : zombies (부모 프로세스가 죽은 자식 프로세스)
      * T : traced or stopped
      * D : Uninterruptiable sleep (디스크 혹은 네트워크 I/O를 대기) 
    * %CPU : 프로세스가 사용하는 CPU 사용률
    * %MEM : 프로세스가 사용하는 메모리 사용률
    * TIME+ : 프로세스가 사용한 토탈 CPU 시간 
    * COMMAND : 해당 프로세스를 실행한 커맨드

### Options
* Sorting : 프로세스 리스트를 특정 칼럼값을 기준으로 정렬하여 출력
  * M : 메모리 사용량 (RES)
  * P : CPU 사용량 (%CPU)
  * N : PID
  * T : running time (TIME+)
* Filtering : 프로세스 리스트를 특정 조건으로 필터링
  * o 또는 O 로 필터링 추가 가능
  * e.g. COMMAND 에 JAVA 가 포함되는 프로세스 필터링
  ![image](https://user-images.githubusercontent.com/48702893/164478048-ea2f8c37-bee0-4239-b8b3-2c05bc2f2d15.png)
  * e.g. 메모리 사용률이 3% 이상인 프로세스 필터링
  ![image](https://user-images.githubusercontent.com/48702893/164478085-88099622-f180-4022-a4cd-ded187730d0b.png)

ps 와 top의 차이점
    * ps는 ps한 시점에 proc에서 검색한 cpu 사용량이다.
    * top은 proc에서 일정 주기로 합산해서 cpu 사용율을 출력한다.

<br>

# vmstat
* 프로세스/메모리/입출력/시스템/CPU 활동상황에 대한 정보 확인
* vmstat <delay> <count>
  * delay : 결과가 출력되는 주기, 이 값을 지정하지 않으면 한 번만 수행하고 종료
  * count : 데이터를 출력할 횟수. 이 옵션을 지정하지 않으면 계속 수행
* vmstat 명령의 첫번째 줄은 이전의 vmstat 명령어로 수집된 정도에 대한 평균치, 즉 현재의 정보가 아닌 이전에 누적된 정보이므로 의미가 없는 데이터

```shell
# vmstat 1 10
procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
 r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
 1  0      0 2669616 403776 24131636    0    0     0     3    0    0  0  0 100  0  0
 0  0      0 2669848 403776 24131636    0    0     0     0  370  411  0  0 100  0  0
 0  0      0 2669816 403776 24131640    0    0     0     0  164  249  0  0 100  0  0
 0  0      0 2669848 403776 24131640    0    0     0     4  437  469  0  0 100  0  0
 0  0      0 2669848 403776 24131644    0    0     0    16  717  746  0  0 100  0  0
 0  0      0 2669880 403776 24131644    0    0     0     0  400  388  0  0 100  0  0
 0  0      0 2669848 403776 24131648    0    0     0     0  154  233  0  0 100  0  0
 0  0      0 2669880 403776 24131648    0    0     0    96  191  274  0  0 100  0  0
 0  0      0 2669880 403776 24131648    0    0     0     0  205  288  0  0 100  0  0
```
### process 관련 Columns
* r,b 를 통해 CPU 부하가 많은 지, I/O 부하가 많은 지 파악
* 특히 r 값이 클수록 OS 자체의 bottleneck 이 존재함을 의미
* r : 실행순서를 기다리고있는 프로세스 수 (CPU 부하 프로세스 수) 
  * CPU 자원이 포화상태인지 확인 가능(r 값이 CPU 수보다 큰 경우 포화된걸로 판단) 
* b : interrupt 안되는 sleep 프로세스 수 (I/O 부하 프로세스 수) 
  * I/O 작업 요청 후, blocking 되어 sleep 상태로 대기중인 프로세스 수

### memory 관련 Columns(kb)
* memory 항목에서 주요한 것은 free항목으로 free는 해당 OS의 실제 남은 메모리를 의미한다.
* free의 수가 현저히 적다면 실제 OS의 물리적인 메모리가 부족하다고 판단하여도 큰 무리가 없다.
* swpd : 사용된 가상 메모리 용량
* free : 여유 메모리 용량
* buff : 버퍼에 사용된 메모리 용량
* cache : 페이지 캐시에 사용된 메모리 용량

### swap 관련 Columns(kb)
* si(swap in) : 디스크(swap 영역)에서 메모리로 스왑된 용량
* so(swap out) : 메모리에서 디스크(swap 영역)로 스왑된 용량
* 물리 메모리가 부족할경우, swap out, swap in 이 발생하므로 이 값이 지속적으로 0 이 아닐시 현재 시스템에 메모리가 부족한것

### io 관련 Columns(blocks/s)
* bi : device에서 받은 입력 블록 수 
* bo : device에 쓰기 블록 수
* 실제로 프로세스에서 얼마나 I/O 가 발생중인지 확인 가능

### system 관련 Columns
* 주로 이전일의 값과 비교하여 CPU 사용률 변화 확인
* in : 초당 발생한 interrupts 수
* cs : 초당 발생한 context switches 수

### cpu 관련 Columns
* us가 높은지, sy 가 높은지에 따라 CPU 사용률 원인분석을 위한 명령어가 달라짐
  * us가 높을경우 : kill -3, ti
  * sy가 높을경우 : truss
* us : user 영역 명령 수행 시간
* sy : kernel 영역 명령 수행 시간
* id : cpu 가 유휴 상태(idle) 인 시간
* wa
  * I/O 요청 후 blocking 되어 응답을 기다리는 cpu 시간
* st
  * stolen time, 가상 CPU를 서비스 하는 동안 실제 CPU를 차지한 시간

### Options
* a : memory 영역 정보를 buff와 cache대신 active/inactive (사용중 메모리 양/미사용 메모리 양)로 출력
* d : 디스크 사용량 출력
  * reads, writes 
    * total : 성공한 모든 읽기/쓰기 작업 개수
    * merged : 하나의 I/O로 묶은 읽기/쓰기 작업 수
    * sectors : 성공적으로 읽은/쓰기 섹터 수
    * ms : 읽기/쓰기 작업을 하는데 소요된 시간(밀리초)
  * I/O (입출력)
    * cur : 현재 수행 중인 I/O 수 
    * sec : I/O를 수행하는데 소요된 시간(초)
```shell
disk- ------------reads------------ ------------writes----------- -----IO------
total merged sectors ms total merged sectors ms cur sec
ram0 0 0 0 0 0 0 0 0 0 0
sda 10864 5487 541394 55154 6140 14695 166258 47768 0 29
sdb 874 941 4334 785 24 17 200 47 0 0
```
* n : 헤더를 반복해서 출력하지 않도록 설정
* S : 출력되는 데이터 단위 지정 (k(1000), K(1024), m(1000000), M(1048576) 으로 나누값 출력)

[[참고. vmstat 분석 방법]](https://waspro.tistory.com/155)

<br>

# free
* 메모리 사용량, 여유량, 캐싱메모리 출력
* swap 영역의 크기도 출력해주며, 값이 0 이 아닐경우, 시스템 메모리가 부족하다는 의미이므로 원인 분석 필요
  * 특정 프로세스가 사용하는 전체 swap 영역에 대한 정보 확인시 /proc/< pid >/status 파일을 통해 확인 가능
* shared, buffers, cached 에는 swap 영역도 포함되어있음
  * 따라서 단순히 메모리 사용 용량 = 메모리 전체 - free - buff/cache 식으로 계산하면 메모리 사용 용량을 과소 평가하게 된다
* 리눅스는 유휴 공간을 캐시 영역으로 잡아두는 습성이 있어 실질적인 의미의 메모리사용률을 구하려면 캐시 영역도 유휴 메모리로 봐야 한다
  * 명목메모리 사용률 = used / total = ( total - free ) / total
  * 실질메모리 사용률 = used2 / total = ( total - free2[2] ) / total = ( total - free - buffers - cached) / total
  * free 열의 두번째 행이 실질적인 유휴메모리 용량이다.
  ![image](https://user-images.githubusercontent.com/48702893/164437435-c1e1350d-5b23-400f-8462-c07ee5de56d9.png)

```shell
$ free -m
             total       used       free     shared    buffers     cached
Mem:        245998      24545     221453         83         59        541
-/+ buffers/cache:      23944     222053
Swap:            0          0          0
```

### Columns
* Mem
  * total: 총 메모리
  * used: 사용 된 메모리
  * free: 사용되지 않은 메모리
  * shared: 여러 프로세스에 공유중인 메모리
  * buffer: 커널 버퍼가 사용중인 메모리
  * cache: 페이지 cache와 slab으로 사용중인 메모리
  * buff/cache: buffer와 chche의 합
  * available: 스와핑없이 새로운 프로세스에 할당 가능한 메모리 예상 크기
* -/+ buffers/cache
  * used : (캐시메모리가 제외된)사용된 메모리
  * free : (캐시메모리가 포함된)사용되지 않은 메모리
* Swap
  * total: 총 스왑 메모리
  * used: 사용중인 스왑 메모리
  * free: 사용 가능한 스왑 메모리

### Options
* 단위 변경 : 바이트(b), 키비바이트(k), 메비바이트(m), 기비바이트(g), 테비바이트(tebi), 페비바이트(pebi), 킬로바이트(kilo), 메가바이트(mega), 기가바이트(giga), 페타바이트(peta)
* t : 합계 칼럼 추가
* c : 지정한 반복 횟수만큼 free 연속해서 재실행
* s : 지정한 초만큼 딜레이를 주고 반복 실행

[[참고. linux 메모리 구조]](https://www.whatap.io/ko/blog/37/)

<br>

# sar
* cpu, memory, network, disk io 등의 지표 정보를 수집하여 sar command을 통해 실시간으로 지표를 보여 주며 파일로 저장
* 옵션에 따라 모니터링 대상이 상당히 넓은 편이며 기본값은 cpu 사용량 통계
* sar <interval> <count>

### sar -u ALL
* CPU 사용량 지표

![image](https://user-images.githubusercontent.com/48702893/164507344-e81d21d9-9852-4e06-a53d-f0ea26a38e39.png)

* %user : 사용자 레벨application level)에서 실행 중일 때의 CPU 사용률
* %nice : 사용자 레벨appliaction level)에서 nice 가중치를 준 CPU 사용률
* %system : 시스템 레벨kernel에서 실행 중인 CPU 사용률
* %iowait : Disk I/O 처리가 늦어서 프로세스가 idle 상태가 되는 비율
* %steal : virtual processer에 의한 작업이 진행되는 동안 virtual CPU에 의해 뜻하지 않는 대기시간이 생기는 시간의 비율
* %idle : CPU의 idle 상태의 비율 (disk I/O는 제외된 지표
* %usr : %user 은 virtual processer 가 포함된 지표이고 %usr 은 virtual processer 가 제외된 지표
* %sys : %system 은 H/W S/W 인터럽트가 포함된 지표이고 %sys 는 인터럽트가 제외된 지표
* %irq : H/W 인터럽트 사용률
* %soft : S/W 인터럽트 사용률
* %guest : virtual processor 사용률
* %gnice : virtual processor에서 nice 가중치를 준 사용률

### sar -d
* 디바이스의 I/O 작업 지표

![image](https://user-images.githubusercontent.com/48702893/164510346-66b3362c-1b04-433e-9ea7-3d6e75437ba5.png)

* DEV : disk device 종류
* tps : 초당 I/O 전체 IOPS
* rkB/s : 초당 disk에 read 된 kbyte 크기 (ex. 1024kB/s == 초당 1MB을 disk read 했다)
* wrB/s : 초당 disk에 write 된 kbyte 크기 (ex. 2048kB/s == 초당 2MB을 disk write 했다)
* dkB/s : 초당 disk에 discard 된 크기
* areq-sz : 해당 Device에 발생된 request의 평균 size ( 1 == 1kb )
* aqu-sz : 해당 Device에 발생된 request들의 queue의 평균 length
* await : 발생된 io의 평균 처리 시간 ms, 보통 1ms 이하로 나와야 정상이며 1ms 이상이 나올 경우는 squ-sz length 가 늘어나면서 한계치에 근접하거나 넘었다고 판단
* %util : 디스크의 idle 한계치 지표 ( 100% == 한계치 이상 부하 진행 중)

### sar -b
* 시간당 I/O 작업 지표

![image](https://user-images.githubusercontent.com/48702893/164510834-7303d436-f66c-4720-a8d9-5b4841c4000e.png)

* tps : 초당 전송 양이며 IOPS 값으로 보면 된다 (ex. 4k 섹터로 100 tps = 초당 400kb)
* rtps : 읽기 iops
* wtps : 쓰기 iops
* dtps : discard iops
* bread/s : 초당 읽은 블록 수
* bwrtn/s : 초당 쓰여진 블록 수
* bdscd/s : 초당 discard 된 블록 수

### sar -q
* cpu load average 지표

![image](https://user-images.githubusercontent.com/48702893/164511279-c6b4dc56-1e75-4cc6-a0fc-bdd2051ab26f.png)

* runq-sz : 실행을 위해 CPU를 대기 중인 메모리의 커널 스레드 수, 일반적으로 이 값은 2보다 작으며 지속적으로 높은 값일경우 시스템이 CPU 제한적임을 의미
* plist-sz : 프로세스와 스레드의 개수
* ldavg-1, 5, 15 : 1분, 5분, 15분 간의 시스템의 Load Average 값
* blocked : 현재 blocking 되어 대기상태인 I/O job 수

### sar -r
* 메모리 사용 지표

![image](https://user-images.githubusercontent.com/48702893/164511858-7d95928a-f1f3-4735-ba83-1b01ba10b02d.png)

* kbmemfree (MemFree) : free 상태의 메모리 크기
* kbavail (MemAvailable) : swap 없이 새로운 프로세스를 시작할 때 사용할 수 있는 메모리 양
* kbmemused (MemTotal - (MemFree + Buffers + Cached + Slab): user 영역에서 사용 중인 메모리 크기
* %memused : 사용 중인 메모리의 점유 백분율
* kbbuffers (Buffers) : buffer cache 메모리 크기
* kbcached (Cached) : page cache 메모리 크기
* kbcommit (Committed_AS)
  * 현재 시스템에 할당된 메모리의 크기. (실제 사용 중인 user영역 크기 + cache)
  * 현재의 Workload 상에서 어느 정도의 RAM 또는 SWAP 이 더 필요할지를 예측하여 Out of memory가 발생하지 않을 만한 메모리 양이다
* %commit : kbcommit의 점유 백분율
* kbactive (Active)
  * 사용 중인 메모리에서, 최근에 사용된 메모리 정보 
  * 메모리 부족으로 여유 메모리를 확보 (reclaiming) 할 때 후순위의 크기
* kbinact (Inactive)
  * 사용 중인 메모리에서 최근에 사용되지 않은 영역의 크기
  * 메모리 확보시 즉시 반환 가능한 크기
* kbdirty (Dirty) : disk에 write 하기 위해 대기 중인 크기
* kbanonpg (AnonPages) : user 영역 페이지 테이블에 매핑되는 non-file 지원 page 크기
* kbslab (Slab) : 커널 내 자료구조 캐시
* kbkstack (KernelStack) : 커널 스택이 사용하는 메모리
* kbpgtbl (PageTables) : page table 크기
* kbvmused (VmallocUsed) : 사용된 vmalloc 영역 크기

### sar -B
* 페이지 테이블 및 페이징 지표

![image](https://user-images.githubusercontent.com/48702893/164512145-1d6d541c-8133-4273-97b1-6e3b513f00b7.png)

* pgpgin/s : 초당 page in (KB/s) 된 크기
* pgpgout/s : 초당 page out (KB/s) 된 크기
* fault/s : page fault (minor + major) 수 
* majflt/s : major 되는 횟수
* pgfree/s : free page의 초당 개수
* pgscand/s : kernel에서 page scan 한 횟수
* pgsteal/s : 메모리 요구를 충족하기 위해 시스템이 초당 캐시페이지 캐시 및 스왑 캐시에서 재 확보한 페이지 수
* %vmeff : kswapd가 스캔한 페이지 중 릴리즈 (disk write) 된 비율 (%)

### sar -H
* hugepages utilization statistics

![image](https://user-images.githubusercontent.com/48702893/164512490-25411876-886c-4483-9b0a-c92af158f970.png)

* kbhugfree (HugePages_Free) : allocated 되지 않은 hugepage 크기 (kb)
* kbhugused : allocated 된 hugepage 크기 (kb).
* %hugused (HugePages_Total) : allocated 된 hugepage % 백분율
* kbhugrsvd (HugePages_Rsvd) : reserved 된 hugepage 크기 (kb)
* kbhugsurp (HugePages_Surp) : surplus 된 hugepage 크기 (kb)

### sar -W
* swapping statistics
* 해당 지표가 올라가면 disk io가 높아지므로 서버 퍼포먼스의 문제가 될 수 있다

![image](https://user-images.githubusercontent.com/48702893/164512603-bff5361e-1d71-4477-b13b-19698eaba467.png)

* pswpin/s : 초당 swap in 횟수
* pswpout/s : 초당 swap out 횟수


### sar -S
* swap space utilization statistics

![image](https://user-images.githubusercontent.com/48702893/164512651-ad5d8ed7-0ae3-476e-95f9-63dbfa25668e.png)

* kbswpfree : free swap 크기kb
* kbswpused : 사용 중인 swap 크기kbytes
* %swpused : 사용 중이 swap %
* kbswpcad : cache 된 swap 크기
* %swpcad : cache 된 swap %

### sar -F
* statistics for currently mounted filesystems

![image](https://user-images.githubusercontent.com/48702893/164512770-e254dae1-386d-413e-8828-c53e8c423d15.png)

* MBfsfree : free 디스크 크기 (MB)
* MBfsused : 사용 중인 디스크 크기 (MB)
* %fsused : 사용 중인 디스크 백분율 %
* %ufsused : free 디스크 백분율 %
* Ifree : free inode 수
* Iused : 사용 중인 inode 수
* %Iused : 사용 중인 inode 백분율 %
* FILESYSTEM : 파티션 영역

### sar -v

![image](https://user-images.githubusercontent.com/48702893/164513055-6f57a1e1-d610-44f7-a1f1-9024b62c3f64.png)

* status of inode, file and other kernel tables.
* dentunusd
  * dentry cache 중 사용하지 않는 반환 가능한 dentry cache 개수
  * 해당 지표가 갑자기 떨어지면 메모리가 모자라서 (vm.vfs_cache_pressure 커널 설정 등 반환하는 작업과 함께 부하가 올 수 있다
* file-nr : open file 수
* inode-nr : open inode 수
* pty-nr : pty(pseudo terminals) handles 수, 즉 ssh, telnet, xterm shell 접속된 수

### sar -n DEV
* 네트워크 작업 지표

![image](https://user-images.githubusercontent.com/48702893/164513212-d23b5b6c-d346-46af-b0c8-296c9148f489.png)

* IFACE : network interface 명
* rxpck/s : 초당 rx 수
* txpck/s : 초당 tx 수
* rxkB/s : 초당 rx 된 크기 (kb)
* txkB/s : 초당 tx 된 크기 (kb)
* rxcmp/s : 초당 압축된 패킷의 rx 수
* txcmp/s : 초당 압축된 패킷의 tx 수
* rxmcst/s : 초당 rx 된 다중 패킷 (multicast) 수
* %ifutil : NIC에서 사용 가능한 network 대역폭의 지표


### sar -n EDEV
* 네트워크 작업 에러 지표

![image](https://user-images.githubusercontent.com/48702893/164513373-c1d85c9c-bdd4-44b9-bcfb-1b1430102b19.png)

* IFACE : network interface 명
* rxerr/s : 초당 error rx 수
* txerr/s : 초당 error tx 수
* coll/s : 초당 발생한 패킷 충돌 수
* rxdrop/s : OS buffer 부족으로 rx drop 된 수
* txdrop/s : OS buffer 부족으로 tx drop 된 수
* txcarr/s : 패킷 tx 중 발생한 초당 carrier-errors 수
* rxfram/s : 패킷 rx 중 발생한 초당 frame alignment 수
* rxfifo/s : 패킷 rx 중 발생한 초당 FIFO overrun error 수
* txfifo/s : 패킷 tx 중 발생한 초당 FIFO overrun error 수


### sar -n SOCK
* ipv4 소켓 지표

![image](https://user-images.githubusercontent.com/48702893/164513492-3081ad9c-4681-45c8-adbd-e7c62740aa73.png)

* totsck
  * 총 사용된 socket 수 
  * 65000개 이상 발생 시 더 이상 소켓 생성이 안되어 문제가 될 수 있음
* tcpsck
  * 현재 사용 중인 TCP 소켓 수 
  * 해당 지표가 최대치로 올라갈 경우 net.ipv4.ip_local_port_range 커널 값에 설정된 값과 비교하여 디버깅

### sar -n IP
* ipv4 트래픽 지표

![image](https://user-images.githubusercontent.com/48702893/164513585-5c26a017-6e52-48c1-bf39-8bebfe81dfa1.png)

* irec/s : rx 된 datagrams 수 [ipInReceives]
* fwddgm/s : 최종 목적지로 가기 위해 포당 forward 된 수 [netstat -s의 ipForwDatagrams]
* idel/s : irec/s 중 성공적으로 (ICMP포함) rx 된 수 [ipOutRequests]
* orq/s : tx 된 datagrams 수 [ipInReceives]
* asmrq/s : fragments received 된 rx 수ipReasmReqds
* asmok/s : datagrams이 성공적으로 re-assembled 된 수 [ipReasmOKs]
* fragok/s : 성공적으로 fragmented 된 수 [ipFragOKs]
* fragcrt/s : datagrams fragments 생성된 수 [ipFragCreates]

### sar -n EIP
* ipv4 에러 트래픽 지표

![image](https://user-images.githubusercontent.com/48702893/164513702-b12e3c99-fde8-413b-a27b-07129d1e2831.png)

* ihdrerr/s : 잘못된 체크섬 버전 불일치 시간 초과 IP 옵션 처리 시 오류 등으로 버려진 datagrams 수[ipInHdrErrors]
* iadrerr/s : IP 헤더의 목적지 필드에 있는 IP가 수신할 유효한 주소가 아니어서 버려진 datagrams 수[ipInAddrErrors]
* iukwnpr/s : 지원되지 않는 프로토콜로 인해 수신은 성공했지만 무시된 datagrams 수[ipInUnknownProtos]
* idisc/s : 리눅스 nic 버퍼 공간 부족 등으로 버려진 rx ip datagrams 수 [ipInDiscards]
* odisc/s : 리눅스 nic 버퍼 공간 부족 등으로 버려진 tx ip datagrams 수ipOutDiscards
* onort/s : 목적지 경로를 찾을 수 없어 버려진 ip datagrams 수 [ipOutNoRoutes]
* asmf/s : IP re-assembly algorithm 의해 감지된 실패된 datagrams 수 [ipReasmFails]
* fragf/s : Fragment flag 설정으로 Fragment로 인해 버려진 datagrams 수 [ipFragFails]

### sar -n TCP
* ipv4 tcp 지표, TCP 통신량을 요약해서 보여준다.
* active와 passive 수를 보는것은 서버의 부하를 대략적으로 측정하는데에 편리하다. 
* retransmits은 네트워크나 서버의 이슈가 있음을 이야기한다. 신뢰성이 떨어지는 네트워크 환경이나(공용인터넷), 서버가 처리할 수 있는 용량 이상의 커넥션이 붙어서 패킷이 드랍되는것을 이야기한다.

![image](https://user-images.githubusercontent.com/48702893/164513847-1d023301-9b0f-4d2e-ac82-1d1079148300.png)

* active/s : 로컬에서부터 요청한 초당 TCP 커넥션 수를 보여준다 (예를들어, connect()를 통한 연결).
* passive/s : 원격으로부터 요청된 초당 TCP 커넥션 수를 보여준다 (예를들어, accept()를 통한 연결).
* retrans/s: 초당 TCP 재연결 수
* iseg/s : rx 된 총 세그먼트 수 [tcpInSegs]
* oseg/s : tx 된 총 세그먼트 수 [tcpOutSegs]

### sar -n ETCP
* ipv4 tcp 에러 지표

![image](https://user-images.githubusercontent.com/48702893/164513865-f41d5ab7-df37-4984-96ef-f30c5121fed3.png)

* atmptf/s : SYN-SEND or SYN-RCVD ==> CLOSED or LISTEN 전환한 수 [tcpAttemptFails]
* estres/s : ESTABLISHED or CLOSE-WAIT ==> CLOSED 전환한 수 [tcpEstabResets]
* retrans/s : 재전송된 세그먼트 수 [tcpRetransSegs]
* isegerr/s : 오류로 rx 된 세그먼트 수 [tcpInErrs]
* orsts/s 오류로 tx 된 세그먼트 수 [tcpOutRsts]

### sar -n SOFT
* 응용프로그램 레벨 네트워크 지표

![image](https://user-images.githubusercontent.com/48702893/164513959-cc94c1a8-7a63-4b11-a41e-ea9acc097e46.png)

* total/s : 초당 처리된 network frames processe 수
* dropd/s : processing queue 부족으로 drop 된 network frames 수
* squeezd/s : 초당 softirq handler function 종류 된 수
* rx_rps/s : 프로세스 간에 패킷 처리를 위해 인터럽트로 woken up 된 수
* flw_lim/s : 초당 flow limit 도달 한 수 CPU의 패킷 처리 부하 분산을 위해 flow 제한하여 제어한 횟수

<br>

# uptime
* 서버 구동 시간 및 접속해있는 사용자수, load average 값 출력(모두 top 명령어로 확인 가능)
```shell
$ uptime
23:51:26 up 21:31, 1 user, load average: 30.02, 26.43, 19.02
```

<br>

# dmesg | tail
* os레벨의 오류(segfault, oom 등) 메세지 확인
* 부팅시부터 시작해서 모든 커널메세지가 출력되기 때문에 tail을 이용해서 마지막 10줄만 출력하는식으로 사용
```shell
$ dmesg | tail
[1880957.563150] perl invoked oom-killer: gfp_mask=0x280da, order=0, oom_score_adj=0
[...]
[1880957.563400] Out of memory: Kill process 18694 (perl) score 246 or sacrifice child
[1880957.563408] Killed process 18694 (perl) total-vm:1972392kB, anon-rss:1953348kB, file-rss:0kB
[2320864.954447] TCP: Possible SYN flooding on port 7001. Dropping request.  Check SNMP counters.
```

<br>

# mpstat
* CPU 사용률을 CPU 별로 출력 
```shell
$ mpstat -P ALL 1
Linux 3.13.0-49-generic (titanclusters-xxxxx)  07/14/2015  _x86_64_ (32 CPU)

07:38:49 PM  CPU   %usr  %nice   %sys %iowait   %irq  %soft  %steal  %guest  %gnice  %idle
07:38:50 PM  all  98.47   0.00   0.75    0.00   0.00   0.00    0.00    0.00    0.00   0.78
07:38:50 PM    0  96.04   0.00   2.97    0.00   0.00   0.00    0.00    0.00    0.00   0.99
07:38:50 PM    1  97.00   0.00   1.00    0.00   0.00   0.00    0.00    0.00    0.00   2.00
07:38:50 PM    2  98.00   0.00   1.00    0.00   0.00   0.00    0.00    0.00    0.00   1.00
07:38:50 PM    3  96.97   0.00   0.00    0.00   0.00   0.00    0.00    0.00    0.00   3.03
[...]
```

<br>

# pidstat 1
* top과 출력하는 데이터가 동이라한, 지속적으로 변화하는 상황을 실시간으로 출력해주기 떄문에, 상황변화 파악에 용이
```shell
$ pidstat 1
Linux 3.13.0-49-generic (titanclusters-xxxxx)  07/14/2015    _x86_64_    (32 CPU)

07:41:02 PM   UID       PID    %usr %system  %guest    %CPU   CPU  Command
07:41:03 PM     0         9    0.00    0.94    0.00    0.94     1  rcuos/0
07:41:03 PM     0      4214    5.66    5.66    0.00   11.32    15  mesos-slave
07:41:03 PM     0      4354    0.94    0.94    0.00    1.89     8  java
07:41:03 PM     0      6521 1596.23    1.89    0.00 1598.11    27  java
07:41:03 PM     0      6564 1571.70    7.55    0.00 1579.25    28  java
07:41:03 PM 60004     60154    0.94    4.72    0.00    5.66     9  pidstat

07:41:03 PM   UID       PID    %usr %system  %guest    %CPU   CPU  Command
07:41:04 PM     0      4214    6.00    2.00    0.00    8.00    15  mesos-slave
07:41:04 PM     0      6521 1590.00    1.00    0.00 1591.00    27  java
07:41:04 PM     0      6564 1573.00   10.00    0.00 1583.00    28  java
07:41:04 PM   108      6718    1.00    0.00    0.00    1.00     0  snmp-pass
07:41:04 PM 60004     60154    1.00    4.00    0.00    5.00     9  pidstat
```

<br>

# iostat
* Disk의 read/write 통계지표 및 CPU 사용률, queue 대기열 길이 등 I/O 작업에 대한 지표 출력
* Disk I/O 작업에 문제가 발생할 때 주로 확인

### Columns

```shell
$ iostat -xz 1
Linux 3.13.0-49-generic (titanclusters-xxxxx)  07/14/2015  _x86_64_ (32 CPU)

avg-cpu:  %user   %nice %system %iowait  %steal   %idle
          73.96    0.00    3.73    0.03    0.06   22.21

Device:   rrqm/s   wrqm/s     r/s     w/s    rkB/s    wkB/s avgrq-sz avgqu-sz   await r_await w_await  svctm  %util
xvda        0.00     0.23    0.21    0.18     4.52     2.08    34.37     0.00    9.98   13.80    5.42   2.44   0.09
xvdb        0.01     0.00    1.02    8.94   127.97   598.53   145.79     0.00    0.43    1.78    0.28   0.25   0.25
xvdc        0.01     0.00    1.02    8.86   127.79   595.94   146.50     0.00    0.45    1.82    0.30   0.27   0.26
dm-0        0.00     0.00    0.69    2.32    10.47    31.69    28.01     0.01    3.23    0.71    3.98   0.13   0.04
dm-1        0.00     0.00    0.00    0.94     0.01     3.78     8.00     0.33  345.84    0.04  346.81   0.01   0.00
dm-2        0.00     0.00    0.09    0.07     1.35     0.36    22.50     0.00    2.55    0.23    5.62   1.78   0.03
```

* rrqm/s : 큐에 대기 중인 초당 읽기 요청 수
* wrqm/s : 큐에 대기 중인 초당 쓰기 요청 수
* r/s : 초당 읽기 섹터 수, OS의 block layer가 아니라 Process가 OS의 커널 영역으로 read/write 함수를 호출한 횟수
* w/s : 초당 쓰기 섹터 수, OS의 block layer가 아니라 Process가 OS의 커널 영역으로 read/write 함수를 호출한 횟수
* rMB/s : 초당 읽기 처리 크기 (r/s X 섹터 크기블록 사이즈 크기
* wMB/s : 초당 쓰기 처리 크기 (w/s X 섹터 크기블록 사이즈 크기
* avgrq-sz : 요청 건수의 평균 크기
* avgqu-sz 
  * 해당 Device에 발생된 request들의 대기 중인 queue의 평균 length
  * 만약 avgqu-sz 수치보다 await 수치가 훨씬 높으면 디스크 쪽에서 병합이 있다고 판단하면 된다
* await(ms)
  * I/O 처리 평균 시간(스레드가 blocking 되어있는 시간) 
  * 일반적인 장치의 요청 처리 시간보다 긴 경우에는 블럭장치 자체의 문제가 있거나 장치가 포화된 상태
* %util : Disk 가 버틸 수 있는 한계치를 %로 표시

### iostat %util, svctm 지표 오류
* iostat의 utils 성능 지표 반응은 hdd에 대한 반응만 비교적 정확합니다. 왜냐하면 hdd는 병렬 (직렬) 이 없기 때문입니다.SSD 디스크에 의미 없음
* iostat의 util 사용률은 디스크의 바쁜 정도를 나타냅니다.기계 디스크 HDD의 경우 성능 지표를 반영할 수 있다.SSD(병렬 IO 지원)의 경우 성능 지표를 반영할 수 없습니다.
* SSD, NVMe 와 같이 병렬처리가 가능한 디스크 장치에서 svctm 지표 자체가 제대로 계산되지 않아서 제 성능을 내기도 전에 100%의 Utilization 지표를 나타내는 이슈
* 스핀들 디스크 (SATA, SAS) 등의 경우 병렬 처리가 안되어 iostst 지표의 %util 값이 신뢰할 수 있지만, Nand (Nvme, ssd)의 경우 병렬처리가 가능하여 병렬 처리로 퍼포먼스를 높인 경우 iostat에서 이를 반영하여 계산하지 못하고 %util 에선 과도하게 높은 지표를 출력
* 따라서 Nand 를 사용하는 장비 일경우, iostat 의 %util 값은 무시
* [iostat man](https://man7.org/linux/man-pages/man1/iostat.1.html)
```
Percentage of elapsed time during which I/O requests were issued to the device (bandwidth utilization for the device). Device saturation occurs when this value is close to 100% for devices serving requests serially.  But for devices serving requests in parallel, such as RAID arrays and modern SSDs, this number does not reflect their performance limits.
```

![image](https://user-images.githubusercontent.com/48702893/164441840-803cf6a7-68d4-45cd-9904-00002ce120cc.png)
> job=3부터 이미 %util 은 최대치를 뿌려주고 있습니다 하지만 부하를 더 줄수록 write 성능은 계속 올라갑니다 즉 %util 필드 자체는 의미가 없게 됩니다.

<br>

# df (disk free)
* 디스크 여유 공간 출력
* 파일시스템,디스크크기, 사용량, 여유공간, 사용률, 마운트지점 순으로 출력

### Columns

![image](https://user-images.githubusercontent.com/48702893/164439315-8f04e7af-bf1f-4231-b2a3-5b7ac3fc0480.png)

* Filesystem : 리눅스에 마운트된 파일 시스템
* Size(1K-blocks) : 전체 디스크 용량
* Used : 사용량
* Available : 남은 용량
* Use% : 용량 대비 사용량 비율
* Mounted on : 마운트 된 지점(경로)

### Options
-a (all) : 모든 파일 시스템 출력
-h (human) : 사람이 읽기 쉬운 형태(단위)로 출력 (기본은 킬로바이트 단위)
-T (type) : 보여주는 목록을 파일시스템의 타입으로 제한
-l (local) : 출력하는 목록을 로컬 파일 시스템으로만 제한

<br>

# ping
* 대상 호스트(호스명 or IP주소)로 ICMP(Internet Control Message Protocol)을 전송하여 대상 호스트에 연결이 되어있는지 확인 가능

<br>

# netstat
* TCP-IP 커넥션 네트워크의 통계 정보 출력 
* 열려있는 포트와 동작하고 있는 소프트웨어도 확인할 수 있다.
* 네트워크 connection 상태, 라우팅테이블, 인터페이스 통계 정보 등을 출력

### Columns

![image](https://user-images.githubusercontent.com/48702893/164449966-a18119db-103e-42ac-b5cf-255b7b2a4f49.png)

* Proto : 프로토콜 종류. TCP / UDP / RAW
* Recv-Q : 해당 process가 현재 받는 바이트 표기
* Send-Q : 해당 process가 현재 보내는 바이트 표기
* Local Address : 출발지 주소 및 포트. 자신의 주소 및 포트
* Foreign Address : 목적지 주소 및 포트
* State : 포트의 상태 표기
  * CLOSED : 완전히 연결이 종료된 상태
  * CLOSING : 흔하지 않으나 주로 확인 메시지가 전송 도중 유실된 상태
  * CLOSE_WAIT : TCP 연결이 상위 응용프로그램 레벨로부터 연결 종료를 기다리는 상태
  * ESTABLISHED : 서버와 클라이언트 간에 세션 연결이 성립되어 통신이 이루어지고 있는 상태(클라이언트가 서버의 SYN을 받아서 세션이 연결된 상태)
  * FIN_WAIT1 : 클라이언트가 서버에게 연결을 끊고자 요청하는 상태(FIN을 보낸 상태)
  * FIN_WAIT2 : 서버가 클라이언트로부터 연결 종료 응답을 기다리는 상태(서버가 클라이언트로부터 최초로 FIN을 받은 후, 클라이언트에게 ACK를 주었을 때
  * LAST_ACK : 호스트가 원격지 호스트의 연결 종료 요구 승인을 기다리는 상태(서버가 클라이언트에게 FIN을 보냈을 때의 상태)
  * LISTEN : 서버의 데몬이 떠 있어서 클라이언트의 접속 요청을 기다리고 있는 상태
  * SYN_SENT : 클라이언트가 서버에게 연결을 요청한 상태
  * SYN_RECEIVED : 서버가 클라이언트로부터 접속 요구(SYN)을 받아 클라이언트에게 응답(SYN/ACK)하였지만, 아직 클라이언트에게 확인 메시지(ACK)는 받지 못한 상태
  * TIME_WAIT : 연결은 종결되었지만 당분간 소켓을 열어 놓은 상태, 약 1분 정도이며 시간이 지나면 사라짐
  * UNKNOWN : 소켓의 상태를 알 수 없음

### Options
* a : 모든 listening, nonlistening 상태 소켓 정보 출력
* i : 네트워크 인터페이스 상태 표시
* n : 주소, 포트번호 표시
* p : 소켓 소유하고 있는 pid와 프로그램 이름을 출력
* r : 라우팅 테이블 정보 출력
* t : TCP 소켓 리스트 출력
* u : UDP 소켓 리스트 출력
* l : LISTEN인 상태만 표시
* c : 1 초 단위로 반복해서 출력

e.g. 
netstat -anp : 열려있는 모든 포트 출력
netstat -anp | grep LISTEN : listen 되는 모든 포트 출력

<br>

# traceroute
* 지정된 호스트까지 패킷이 전달되는 경로 출력
* ping이 날라가지 않을 경우, traceroute을 통해 호스트 자체에 문제가 있는지, 호스트에 도달하기까지 네트워크 경로중에 문제가 있는지 알아볼 수 있다.

<br>

# Troubleshooting
### CPU 
* top 이나 sar 를 이용해 load average 를 확인하여 CPU 부하 확인 
* CPU 부하가 높다면, top 의 us, sy, wa column 을 확인하여 유저 영역과 커널 영역, IO 작업중 어느 부분에서 많은 부하가 발생중인지 확인
  * 일반적으로 커널 영역의 CPU 사용률은 30% 이하, IO Wait CPU 사용률은 20% 이하 
* ps 로 프로세스 상태나, CPU 사용시간등을 확인하여, 어느 프로세스에서 부하를 유발중인지 확인
  * 프로세스 찾은후, 좀 더 상세한 정보가 필요할 경우 strace 명령어로 어플리케이션의 systemcall 을 추적하거나 oprofile 로 프로파일링하여 병목지점 좁혀나감 
* 커널영역의 부하가 높을경우, truss, pstack, jstack 등을 이용해 어떤 유형의 시스템 콜에서 부하를 유발중인지 확인
* IO Wait 인 CPU 사용률이 높을경우, netstat, iostat, sar 등을 이용해 입출력이 과다하게 발생하는것은 아닌지 디스크 사용률등을 확인
* 안정적인 운영을 위한 CPU 사용율 기준 
  * CPU 사용률 : CPU 70% 이하 
  * CPU 실행큐 : CPU core 당 평균 3개 이하
  
### Memory
* 메모리 사용패턴이 100% 라고 해도 부족하지 않으면 성능저하가 발생하지 않고, Java 어플리케이션은 설정한 힙메모리 이상을 사용하지 않기때문에 운영체제 메모리 부족이 발생할 가능성은 낮음
* top 이나, vmstat, free 등의 명령어로 swap 공간이 사용되고 있는지 확인하여 메모리 부족 여부 판별
  * 스왑이 발생하고 있는 경우:
    1. 특정 프로세스가 극단적으로 메모리를 소비하고 있지 않는 지 ps명령어로 확인할 수 있다.
    2. 프로그램 오류로 메모리를 지나치게 사용하는 경우, 프로그램을 개선한다.
    3. 탑재된 메모리가 부족한 경우에는 메모리를 증설한다. 메모리를 증설할 수 없는 경우에는 분산을 검토한다.
  * 스왑이 발생하지 않고, 디스크로의 입출력이 빈번하게 발생하고 있는 상황:
    *캐시에 필요한 메모리가 부족한 경우로 생각해볼 수 있다. 해당 서버가 저장하고 있는 데이터 용량과 증설 가능한 메모리량을 비교해서 다음과 같이 나눠서 검토한다.
    1. 메모리 증설로 캐시영역을 확대시킬 수 있는 경우는 메모리를 증설한다.
    2. 메모리 증설로 대응할 수 없는 경우는 데이터 분산이나 캐시서버 도입등을 검토한다. 물론, 프로그램을 개선해서 I/O빈도를 줄이는것도 검토가능하다.
* 메모리가 부족하다면, top 이나 ps -euf 명령어로 어느 프로세스에서 메모리 사용률이 높은지 확인하여 조치 
* 안정적인 운영을 위한 메모리 판단 기준 
  * 메모리 사용률 : 100% 이하
  * 지속적으로 프로세스 스와핑이 발생하지 않는지 확인

***
> Reference
* https://pearlluck.tistory.com/125
* https://waspro.tistory.com/165
* https://brunch.co.kr/@leedongins/75
* https://chigon.tistory.com/entry/%EC%84%B1%EB%8A%A5-%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%8B%9C-%EC%84%9C%EB%B2%84-%EB%AA%A8%EB%8B%88%ED%84%B0%EB%A7%81-%EB%B0%A9%EB%B2%95-%EC%A0%95%EB%A6%AC
* https://luavis.me/server/linux-performance-analysis
* https://www.mimul.com/blog/linux-server-operations/
* https://www.whatap.io/ko/blog/10/
* https://brunch.co.kr/@leedongins/80
* https://pearlluck.tistory.com/129
* https://intrepidgeeks.com/tutorial/util-iostat-index-of-linux-hard-disk-and-solid-state-disk
* https://brunch.co.kr/@lars/7
* https://lunatine.net/2019/03/22/high-iostat-util-on-nvme/
* https://jaemunbro.medium.com/linux-network-%EB%AC%B8%EC%A0%9C%ED%95%B4%EA%B2%B0-%EA%B0%80%EC%9D%B4%EB%93%9C-for-beginners-b167d5175ef8
* https://sabarada.tistory.com/146
* https://brunch.co.kr/@lars/9