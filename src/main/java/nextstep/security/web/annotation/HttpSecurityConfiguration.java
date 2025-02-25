package nextstep.security.web.annotation;

import nextstep.app.AuthenticationConfiguration;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.web.builders.HttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpSecurityConfiguration {

    private final AuthenticationConfiguration authenticationConfiguration;

    public HttpSecurityConfiguration(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public HttpSecurity httpSecurity() {
        AuthenticationManager authenticationManager = authenticationConfiguration.authenticationManager();
        return new HttpSecurity(authenticationManager);
    }
}
