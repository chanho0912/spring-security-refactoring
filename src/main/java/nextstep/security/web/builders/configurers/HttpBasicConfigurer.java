package nextstep.security.web.builders.configurers;

import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.BasicAuthenticationFilter;
import nextstep.security.web.builders.HttpSecurity;
import nextstep.security.web.builders.SecurityConfigurer;

public class HttpBasicConfigurer implements SecurityConfigurer {

    @Override
    public void init(HttpSecurity http) {
    }

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        BasicAuthenticationFilter basicAuthenticationFilter = new BasicAuthenticationFilter(authenticationManager);
        http.addFilter(basicAuthenticationFilter);
    }
}
