# Nit Test Statment
### Input parameter 절 : 입력 파라미터들을 넘기려고 할 때 사용
* Statement의 종류에 따라 입력 파라미터 지정 방법 다름
	* Http statement : key = value(type : long, int, string) 형태
	* nimm(npc) statement : 입력파라미터 순서대로 나열, 순서대로 전송. nit에서 지원하는 모든 data type 지정 가능 → nano::Variant 형으로 변환되어 서버로 전송

### Output parameter 절 : response data에서 일부 정보를 변수에 저장하고 싶을때 사용
* Http statement : response data가 json이나 xml format인 경우에만 output parameter 기능 사용 가능
* Nimm(npc) statement : output parameter 지정 시 nit가 생성한 row번호를 prefix로 붙여야함
​
### Filtering 절 : response data에서 일부 정보를 걸러내고 싶을 때 사용
* 예를 들어, response data에 날짜 정보가 있는데 이 값은 테스트를 실행할때마다 변하는 가변적인 정보. → orc파일을 생성할 수가 없음
* 따라서 이런 경우에 Filter 기능을 사용해 날짜정보를 걸러내야함.

### Redirection 절 : response data 전체를 출력하고 싶지 않을때 사용
* response data 전체를 filtering 해도 되지만 redirection 기능이 더 간단.
* ex) HTTP GET /GetRegisterUserInfo.ndrive IN_PRARMS userid="nv_nit_01" > WRITE("out") 처럼 response data 전체를 out 파일로 redirection하면 테스트 결과에 아무것도 나오지 않음.

### 문법
* SET Statement : 테스트 관련 속성 설정 시 사용
	* SET [property] = [value] \n
	* 다른 Statement 사용 시 해당 Statement 전에 먼저 설정해주어야 함.
	* property 목록
		* HTTP_SERVER_ADDR(str), HTTP_SERVER_PORT(long), HTTP_VERSION(str), HTTP_HEADERS(dict), HTTP_RESPONSE_TYPE(json/xml/text)
		* HTTP_KEEP_CONNECTION_ALIVE
			* = 1 : 하나의 tc 처리 시 http session 계속 유지한 상태에서 http statement 처리, default 값 0
		* HTTP_DISPLAY_RES_HEADER
			* = 1 : out file에 http response header 정보를 출력, default 값 0
		* NPC_SERVER_ADDR(str), NPC_SERVER_PORT(long), NPC_NAMESPACE(str), NIMM_DOMAIN_ID(long), NIMM_SERVER_ID(long)

* Declare Variable Statement : 변수 선언 시 사용
	* long, string 선언 : var = 값
	* cast 함수 사용 : var = str(값), var = int(값), var = char(값)
	* list 선언 : var = [값1, 값2, 값3, ... , 값n]  ( 이때 값들의 type은 달라도 됨)
	* dict 선언 : var = {k1: list_var, k2:str_var, k3:100, ...}

* NPC/NIMM Statement : NPC(or NIMM) message 보내려고 할 때 사용
	* npc_call_statement = NPC (hint_clause) CALL [method] (in_param_clause) (out_param_clause) (filter_clause) (out_redirect_clause)
	* nimm_send_statement = NIMM (hint_clause) SEND [method] (in_param_clause) (out_param_clause) (filter_clause) (out_redirect_clause)
		* hint_clause : NIT의 동작을 변경하려고 할 때 사용
		* method : npc method 

* HTTP GET/POST/HEAD/OPTION Statement : HTTP GET/POST/HEAD/OPTION method로 서버에 request 요청
	* http_get_statement = HTTP (hint_clause) GET/POST/HEAD/OPTION [url] (in_param_clause) (out_param_clause) (filter_clause) (out_redirect_clause)
		* url : ex) http://static.naver.com/www/u/2010/0611/nmms_215646753.gif
			* http: - 프로토콜
			* static.naver.com - 정보 자원을 가진 컴퓨터의 위치
			* www/u/2010/0611 - 파일 디렉토리
			* nmms_215646753.gif - 자원 이름
			* 이 때 파일 디렉토리와 자원이름 만을 입력.

* HTTP Upload Statement : HTTP request body에 파일의 contents 전송 시 사용
	* http_upload_statement = HTTP (hint_clause) UPLOAD [url] (in_param_clause) (out_param_clause) (filter_clause) (in_redirect_clause)
		* in_redirect_clause : 로컬 파일의 이름, offset, size지정. offset과 size 생략 시 파일 전체 전송

* HTTP Download Statement : HTTP response 전체를 특정 파일에 저장
	* http_download_statement = HTTP (hint_clause) DOWNLOAD[url] (in_param_clause) [out_redirect_clause]
		* out_redirect_clause : N드라이브 서버가 보내준 데이터를 로컬 컴퓨터에 저장하려고 할 때 사용

* HTTP Webdav Statement : HTTP webdav request를 서버로 보낼 때 사용
	* http_webdav_statement = HTTP (hint_clause) WEBDAV [method] [url] (in_param_clause) (in_redirect_clause) (out_param_clause) (filter_clause) (out_redirect_clause)
		* method : MKCOL, DELETE, COPY, MOVE, PROPFIND, PROPPATCHUrl
		* in_redirect_clause : 특정 파일의 내용을 http body에 보내려고 할 때 사용
		* filter_clause : N드라이브가 서버로 보내준 response에서 일부 정보를 필터링하려고 할 때 사용

* INCLUDE Statement : 다른 파일의 내용 include [INCLUDE "file path"]

* SYSTEM(command) : command 시스템 명령 실행 ex) SYSTEM("sleep 3")

* PRINT(variable) : 변수의 값 출력, tc 작성 시 디버깅 용도로 사용

| HTTP	| HEAD	| OPTIONS	| GET	| POST	| WEBDAV	 | UPLOAD |
|:------:|:------:|:------:|:------:|:------:|:------:|:------:|
|DOWNLOAD	|READ	|WRITE	|IN_PARAMS	|OUT_PARAMS	|SET	|FILT|
|PRINT	|SYSTEM	|NIMM	|SEND	|NPC	|CALL	|INCLUDE|