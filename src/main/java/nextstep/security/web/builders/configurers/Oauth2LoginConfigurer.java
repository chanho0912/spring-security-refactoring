package nextstep.security.web.builders.configurers;

import nextstep.oauth2.registration.ClientRegistrationRepository;
import nextstep.oauth2.web.OAuth2AuthorizationRequestRedirectFilter;
import nextstep.oauth2.web.OAuth2AuthorizedClientRepository;
import nextstep.oauth2.web.OAuth2LoginAuthenticationFilter;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.web.builders.HttpSecurity;

public class Oauth2LoginConfigurer implements SecurityConfigurer {

    @Override
    public void init(HttpSecurity http) {
    }

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        ClientRegistrationRepository clientRegistrationRepository = http.getSharedObject(ClientRegistrationRepository.class);

        OAuth2AuthorizationRequestRedirectFilter redirectFilter = new OAuth2AuthorizationRequestRedirectFilter(clientRegistrationRepository);
        OAuth2LoginAuthenticationFilter authenticationFilter = new OAuth2LoginAuthenticationFilter(clientRegistrationRepository,
                                                                                                   new OAuth2AuthorizedClientRepository(),
                                                                                                   authenticationManager);

        http.addFilter(redirectFilter);
        http.addFilter(authenticationFilter);
    }
}
