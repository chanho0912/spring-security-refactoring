package nextstep.security.autoconfigure;

import jakarta.servlet.Filter;
import nextstep.security.access.AnyRequestMatcher;
import nextstep.security.authorization.PermitAllAuthorizationManager;
import nextstep.security.config.DefaultSecurityFilterChain;
import nextstep.security.config.FilterChainProxy;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.web.builders.Customizer;
import nextstep.security.web.builders.HttpSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class WebSecurityConfiguration {

    @Autowired(
            required = false
    )
    private HttpSecurity httpSecurity;
    private List<SecurityFilterChain> securityFilterChains;

    @Bean(
            name = {"springSecurityFilterChain"}
    )
    public Filter springSecurityFilterChain() {
        boolean hasFilterChain = !this.securityFilterChains.isEmpty();

        if (!hasFilterChain) {
            this.httpSecurity.authorizeHttpRequests((authorize) -> authorize.requestsMatcher(
                    AnyRequestMatcher.INSTANCE,
                    new PermitAllAuthorizationManager<>()));
            this.httpSecurity.formLogin(Customizer.withDefaults());
            this.httpSecurity.httpBasic(Customizer.withDefaults());
            DefaultSecurityFilterChain filterChain = (DefaultSecurityFilterChain) this.httpSecurity.build();
            securityFilterChains.add(filterChain);
        }

        return new FilterChainProxy(securityFilterChains);
    }

    @Autowired(
            required = false
    )
    void setFilterChains(List<SecurityFilterChain> securityFilterChains) {
        this.securityFilterChains = securityFilterChains;
    }
}
