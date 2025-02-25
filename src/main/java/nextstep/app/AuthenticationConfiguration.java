package nextstep.app;

import nextstep.oauth2.authentication.OAuth2LoginAuthenticationProvider;
import nextstep.oauth2.userinfo.OAuth2UserService;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.DaoAuthenticationProvider;
import nextstep.security.authentication.ProviderManager;
import nextstep.security.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AuthenticationConfiguration {

    private final UserDetailsService userDetailsService;
    private final OAuth2UserService oAuth2UserService;

    public AuthenticationConfiguration(UserDetailsService userDetailsService, OAuth2UserService oAuth2UserService) {
        this.userDetailsService = userDetailsService;
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(
                new DaoAuthenticationProvider(userDetailsService),
                new OAuth2LoginAuthenticationProvider(oAuth2UserService)));
    }

}
