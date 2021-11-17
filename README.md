# mobile_app_programming_team3
mobile app programming team3 project

#login 구현 설명 
##소스참고 
###https://doitddo.tistory.com/104 

##firebase 공식문서 
###https://firebase.google.com/docs/auth/android/google-signin 
##firebase console창 -> 사용자 추가되는거 확인 
##viewBinding->ActivityMainBinding 
###https://duckssi.tistory.com/42 
###https://jepark-diary.tistory.com/37 
####viewBinding을 위한 gradle version확인//https://icandooit.tistory.com/87

##필수 추가 항목 
```val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) .requestIdToken(default_web_clinet_id) .requestEmail() .build() ***default_web_client_id에 문자열 넣기->찾는 방법: project단위->app->build->res->google-services->debug->values->value에서 default_web_client_id확인 
```
####build를 하면 해결되는 경우도 있다. 
####google cloud platform에서 oauth2.0 클라이언트 id에서 web client의 클라이언트 id에서도 찾을 수 있다.

##androidmanifest.xml에 추가

##avd는 google playstore? 그 삼각형 표시가 포함된 것으로 사용

#####소스는 따로 upload 할 예정
