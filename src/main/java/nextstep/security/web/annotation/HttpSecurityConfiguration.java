package nextstep.security.web.annotation;

import nextstep.security.web.builders.HttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpSecurityConfiguration {

    @Bean
    public HttpSecurity httpSecurity() {
        return new HttpSecurity();
    }
}
