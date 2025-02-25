# spring-security
## 🚀 1단계 - SecurityFilterChain 리팩터링
- [x] 기본적인 SecurityFilterChain 생성 및 리팩터링

## 🚀 2단계 - 인증 관련 리팩터링
- [x] .formLogin() 메서드를 사용하여 폼 로그인 기능을 설정하고, UsernamePasswordAuthenticationFilter를 자동으로 추가한다.
- [x] .httpBasic() 메서드를 사용해 HTTP Basic 인증을 설정하고, BasicAuthenticationFilter를 자동으로 추가한다.

## 🚀 3단계 - 인가 관련 리팩터링
- [x] 기존에 직접 추가하던 AuthorizationFilter를 HttpSecurity의 .authorizeHttpRequests() 설정을 통해 대체하고, 접근 권한 설정을 커스터마이징한다.
 
## 🚀 4단계 - Auto Configuration 적용
- [x] 스프링 부트의 Auto Configuration을 통해 기본적인 보안 설정을 자동으로 적용하도록 설정한다.
- [x] 사용자가 새로운 SecurityFilterChain을 정의할 경우, 기본 설정이 비활성화되도록 조건부 설정을 추가한다.
