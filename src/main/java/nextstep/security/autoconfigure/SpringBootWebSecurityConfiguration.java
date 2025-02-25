package nextstep.security.autoconfigure;

import nextstep.security.access.AnyRequestMatcher;
import nextstep.security.authorization.PermitAllAuthorizationManager;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.web.annotation.EnableWebSecurity;
import nextstep.security.web.builders.Customizer;
import nextstep.security.web.builders.HttpSecurity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration(
        proxyBeanMethods = false
)
@ConditionalOnWebApplication(
        type = ConditionalOnWebApplication.Type.SERVLET
)
public class SpringBootWebSecurityConfiguration {
    @Configuration(
            proxyBeanMethods = false
    )
    @ConditionalOnDefaultWebSecurity
    static class SecurityFilterChainConfiguration {
        SecurityFilterChainConfiguration() {
        }

        @Bean
        @Order(2147483642)
        SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
            http.authorizeHttpRequests((requests) -> requests.requestsMatcher(
                    AnyRequestMatcher.INSTANCE,
                    new PermitAllAuthorizationManager<>()));
            http.formLogin(Customizer.withDefaults());
            http.httpBasic(Customizer.withDefaults());
            return http.build();
        }
    }

    @Configuration(
            proxyBeanMethods = false
    )
    @ConditionalOnMissingBean(
            name = {"springSecurityFilterChain"}
    )
    @ConditionalOnClass({EnableWebSecurity.class})
    @EnableWebSecurity
    static class WebSecurityEnablerConfiguration {
        WebSecurityEnablerConfiguration() {
        }
    }

}
