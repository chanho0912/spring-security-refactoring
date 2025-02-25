# spring-security
## 🚀 1단계 - SecurityFilterChain 리팩터링
- [x] 기본적인 SecurityFilterChain 생성 및 리팩터링

## 🚀 2단계 - 인증 관련 리팩터링
- [x] .formLogin() 메서드를 사용하여 폼 로그인 기능을 설정하고, UsernamePasswordAuthenticationFilter를 자동으로 추가한다.
- [x] .httpBasic() 메서드를 사용해 HTTP Basic 인증을 설정하고, BasicAuthenticationFilter를 자동으로 추가한다.

## 🚀 3단계 - 인가 관련 리팩터링
- [x] 기존에 직접 추가하던 AuthorizationFilter를 HttpSecurity의 .authorizeHttpRequests() 설정을 통해 대체하고, 접근 권한 설정을 커스터마이징한다.
## 🚀 4단계 - Auto Configuration 적용
