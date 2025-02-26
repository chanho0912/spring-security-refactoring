package nextstep.security.autoconfigure;

import nextstep.app.AuthenticationConfiguration;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.web.builders.Customizer;
import nextstep.security.web.builders.HttpSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration(
        proxyBeanMethods = false
)
public class HttpSecurityConfiguration {

    private final AuthenticationConfiguration authenticationConfiguration;
    private ApplicationContext context;

    public HttpSecurityConfiguration(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    @Scope("prototype")
    public HttpSecurity httpSecurity() {
        AuthenticationManager authenticationManager = authenticationConfiguration.authenticationManager();
        HttpSecurity http = new HttpSecurity(authenticationManager, context);

        return http.securityContext(Customizer.withDefaults());
    }
}
