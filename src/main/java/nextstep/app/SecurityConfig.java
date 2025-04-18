package nextstep.app;

import nextstep.oauth2.OAuth2ClientProperties;
import nextstep.oauth2.registration.ClientRegistration;
import nextstep.oauth2.registration.ClientRegistrationRepository;
import nextstep.security.access.AnyRequestMatcher;
import nextstep.security.access.MvcRequestMatcher;
import nextstep.security.access.hierarchicalroles.RoleHierarchy;
import nextstep.security.access.hierarchicalroles.RoleHierarchyImpl;
import nextstep.security.authorization.AuthorityAuthorizationManager;
import nextstep.security.authorization.PermitAllAuthorizationManager;
import nextstep.security.authorization.SecuredMethodInterceptor;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.web.annotation.EnableWebSecurity;
import nextstep.security.web.builders.Customizer;
import nextstep.security.web.builders.HttpSecurity;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy
@EnableConfigurationProperties(OAuth2ClientProperties.class)
public class SecurityConfig {

    private final OAuth2ClientProperties oAuth2ClientProperties;

    public SecurityConfig(OAuth2ClientProperties oAuth2ClientProperties) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
    }

    @Bean
    public SecuredMethodInterceptor securedMethodInterceptor() {
        return new SecuredMethodInterceptor();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.with()
                                .role("ADMIN").implies("USER")
                                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/login"))
                .formLogin(formLogin -> formLogin.loginPage("/login"))
                .httpBasic(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .authorizeHttpRequests(authorizeHttpRequests ->
                                               authorizeHttpRequests
                                                       .requestsMatcher(
                                                               new MvcRequestMatcher(HttpMethod.GET, "/members"),
                                                               new AuthorityAuthorizationManager<>(roleHierarchy(), "ADMIN"))
                                                       .requestsMatcher(
                                                               new MvcRequestMatcher(HttpMethod.GET, "/members/me"),
                                                               new AuthorityAuthorizationManager<>(roleHierarchy(), "USER"))
                                                       .requestsMatcher(
                                                               AnyRequestMatcher.INSTANCE,
                                                               new PermitAllAuthorizationManager<>()))
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        Map<String, ClientRegistration> registrations = getClientRegistrations(oAuth2ClientProperties);
        return new ClientRegistrationRepository(registrations);
    }

    private static Map<String, ClientRegistration> getClientRegistrations(OAuth2ClientProperties properties) {
        Map<String, ClientRegistration> clientRegistrations = new HashMap<>();
        properties.getRegistration().forEach((key, value) -> clientRegistrations.put(key,
                                                                                     getClientRegistration(key, value, properties.getProvider().get(key))));
        return clientRegistrations;
    }

    private static ClientRegistration getClientRegistration(String registrationId,
                                                            OAuth2ClientProperties.Registration registration, OAuth2ClientProperties.Provider provider) {
        return new ClientRegistration(registrationId, registration.getClientId(), registration.getClientSecret(), registration.getRedirectUri(), registration.getScope(), provider.getAuthorizationUri(), provider.getTokenUri(), provider.getUserInfoUri(), provider.getUserNameAttributeName());
    }
}

