# Rewrite vs Redirect
![image](https://user-images.githubusercontent.com/48702893/106766182-f528c600-667c-11eb-9648-d62c96b94a9b.png)

# Rewrite
* 서버 사이드 URL 재작성
* 서버에서 내부적으로 다른 URL 로 재 요청 하는것
* 브라우저는 URL이 재작성되었다는것을 알 수 없고 주소창도 기존 URL 로 유지
* Forward 되는 서버에 사용자 요청정보가 그대로 함께 전달
	* 사용자가 (Forward 서버에서 응답한)응답페이지에서 새로고침을 여러번 할 경우, (요청정보가 세션에 유지되고있으므로)요청이 여러번 전달되어 의도치 않은 결과 유발 가능
	* 따라서 서버의 상태/데이터 에 변경을 가하지 않는 단순 조회 요청시에만 사용하는것이 안전 

![image](https://user-images.githubusercontent.com/48702893/106764942-a3337080-667b-11eb-94ce-8584d38b5658.png)


# Redirect
* 클라이언트 사이드 URL 재작성
* 서버에서 rediect 할 URL 을 300 번대 코드와 함께 응답하면 클라이언트의 브라우저에서 해당 URL로 재요청
* 브라우저에서 URL 재 요청하는것이므로 주소창도 새로운 URL 로 바뀜
* 브라우저에서 URL 재 요청시 새로운 Request, Response 객체 생성하여 요청하므로 요청정보가 유지되지 않음
	* 사용자가 응답페이지에서 여러번 새로고침하여도 기존의 요청정보가 존재하지 않으므로 요청이 여러번 수행되는 경우가 발생하지 않음
	* 따라서 서버 상태/데이터 변경 요청일경우 Redirect 를 사용하는것이 안전
	
![image](https://user-images.githubusercontent.com/48702893/106764978-acbcd880-667b-11eb-8b5d-4db7c8a154ea.png)
