# send(), recv()
* 소켓 입출력을 위한 함수이다. 
* read()/write() 대비 flags 매개변수에 패킷의 옵션을 지정할 수 있는 차이점 존재

```
#include <sys/socket.h>

// flags: 패킷의 옵션을 지정한다.
ssize_t send(int sockfd, const void* buf, size_t len, int flags);
ssize_t recv(int sockfd, void* buf, size_t len, int flags)
```

# MSG_OOB 옵션
* 긴급하게 처리되어야 하는 메시지을 송수신
    * send() : TCP 패킷에 URG 플래그를 설정하여 전송
    * recv() : 현재 수신 버퍼에서 URG 패킷의 내용이 위치한 int형 오프셋을 반환
* 수신 측에서 시그널 핸들러를 미리 등록해두고, SIGURG 시그널이 발생했을 때 시그널 핸들러에서 긴급히 처리하는 식으로 구현한다. 이 때 fnctl() 시스템 콜을 사용하여 특정 소켓의 URG 패킷을 어떤 프로세스에서 처리할 지 지정하는 점을 명시!
* select()를 사용할 때 URG 패킷을 받은 소켓의 fd는 에외 상황이 발생한 fd를 관찰하여 도출 가능
> cf) 주의: MSG_OOB는 긴급히 처리되어야 할 데이터가 있다는 것을 알려줄 뿐 긴급히 처리된다는 것은 보장하지 못한다. 또한, 일반적인 패킷과 URG 패킷은 모두 같은 채널로 보내지기 때문에 빠른 전송도 보장되지 않는다.

# MSG_PEEK 옵션
* recv()에만 사용할 수 있는 옵션으로 입력 버퍼에 데이터가 있는지 검사
* MSG_DONTWAIT와 조합할 경우 블로킹 없이 데이터 유무 검사 가능
* Peek란 이름처럼 소켓의 입력 버퍼 내 데이터를 buf 매개변수에 지정된 버퍼로 복사

# readv(), writev()
* 데이터를 여러 버퍼에서 한번에 송신/수신하는 시스템 콜
```
#include <sys/uio.h>

struct iovec {
    void* iov_base; // Starting address
    size_t iov_len; // Number of bytes to transfer
}

ssize_t readv(int fd, const struct iovec* iov, int iovcnt);
ssize_t writev(int fd, const struct iovec* iov, int iovcnt);
```
### 사용법
1. N개의 버퍼를 준비한다.
2. 길이 N의 struct iovec형 배열을 만들고 버퍼의 주소와 크기를 지정한다.
3. 구조체 배열을 iov 매개변수에, N을 iovcnt 매개변수에 인자로 넘긴다.

* writev()는 Nagle 알고리즘을 사용하지 않을 때 전송되는 패킷의 수를 줄일 수 있다는 이점이 있다.