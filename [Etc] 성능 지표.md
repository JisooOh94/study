# Throughput, Latency
* Throughput(처리량) : 초당 처리할 수 있는 작업 개수
* Latency(지연시간) : 하나의 작업을 처리하는데 걸리는 시간
* 서비스 제공자 측면에선 처리량(Throughput)을 높이는게 이득이고, 서비스 사용자 측면에선 자신의 작업 지연시간(Latency)이 적은게 이득 
* Throughput 과 Latency 는 서로 trade-off 관계
	* Throughput이 커지만 Latency가 늦어지고, Latency이 빨라지면 Throughput이 줄어듬
* 한정된 리소스내에서 두가지 모두 얻는것은 불가능하며, 요구사향에 따라 적절히 비중을 선택해야함
	* Throughput 만 올리면, Latency 로 인해 사용성이 떨어져 사용자가 다른 서비스로 떠나감
	* Latency 만 최소화하면, Throughput 이 떨어져 더 많은 리소스 비용 발생

<br>

# HA (High Availability)
* availbility(가용성) : 서비스가 다운되지 않고 정상적으로 유지된 시간, 정도
* High Availability(고가용성)
	* 서비스가 오랜 기간 동안 지속적으로 정상 운영되는 상황 
	* 서비스 장애 발생 빈도가 적으며, 장애가 발생하더라도 빠르게 복구되는 서비스
	* 서비스 장애가 빈번할수록, 장애상황이 오래 지속될술록, 서비스에 대한 고객의 신뢰도는 떨어지므로, 고가용성을 보장하는 설계가 필수
* 클러스터링, 이중화, 확장성, RAID 등의 설계를 통해 고가용성 보장

***
> Reference
> * https://hellowkorea.tistory.com/entry/Throughput%EC%B2%98%EB%A6%AC%EB%9F%89%EA%B3%BC-Latency%EC%A7%80%EC%97%B0%EC%8B%9C%EA%B0%84%EC%9D%98-%EC%B0%A8%EC%9D%B4
> * http://idchowto.com/it%EC%97%90%EC%84%9C-%EB%A7%90%ED%95%98%EB%8A%94-%EA%B0%80%EC%9A%A9%EC%84%B1%EC%9D%B4%EB%9E%80/