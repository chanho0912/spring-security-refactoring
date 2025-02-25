package nextstep.security.web.annotation;

import nextstep.app.AuthenticationConfiguration;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.context.SecurityContextHolderFilter;
import nextstep.security.web.builders.Customizer;
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
        HttpSecurity http = new HttpSecurity(authenticationManager);

        return http.securityContext(Customizer.withDefaults());
    }
}
