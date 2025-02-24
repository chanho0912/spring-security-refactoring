package nextstep.security.web.builders;

public interface SecurityConfigurer {

    void init(HttpSecurity http);

    void configure(HttpSecurity http);
}
