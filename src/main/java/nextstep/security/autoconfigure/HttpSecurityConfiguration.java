package nextstep.security.autoconfigure;

import nextstep.app.AuthenticationConfiguration;
import nextstep.oauth2.registration.ClientRegistrationRepository;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.web.builders.Customizer;
import nextstep.security.web.builders.HttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(
        proxyBeanMethods = false
)
public class HttpSecurityConfiguration {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public HttpSecurityConfiguration(AuthenticationConfiguration authenticationConfiguration,
                                     ClientRegistrationRepository clientRegistrationRepository) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public HttpSecurity httpSecurity() {
        AuthenticationManager authenticationManager = authenticationConfiguration.authenticationManager();
        HttpSecurity http = new HttpSecurity(authenticationManager, clientRegistrationRepository);

        return http.securityContext(Customizer.withDefaults());
    }
}
