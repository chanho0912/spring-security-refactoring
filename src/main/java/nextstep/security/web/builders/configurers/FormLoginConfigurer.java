package nextstep.security.web.builders.configurers;

import nextstep.security.access.MvcRequestMatcher;
import nextstep.security.access.RequestMatcher;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.security.web.builders.HttpSecurity;
import nextstep.security.web.builders.SecurityConfigurer;
import org.springframework.http.HttpMethod;

public class FormLoginConfigurer implements SecurityConfigurer {
    private static final MvcRequestMatcher DEFAULT_MVC_REQUEST_MATCHER = new MvcRequestMatcher(HttpMethod.POST, "/login");

    private boolean permitAll;
    private RequestMatcher loginRequestMatcher;

    public FormLoginConfigurer() {
        this.loginRequestMatcher = DEFAULT_MVC_REQUEST_MATCHER;
        this.permitAll = false;
    }

    @Override
    public void init(HttpSecurity http) {
    }

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter(authenticationManager);
        http.addFilter(usernamePasswordAuthenticationFilter);
    }

    public FormLoginConfigurer loginPage(String loginPage) {
        this.loginRequestMatcher = new MvcRequestMatcher(HttpMethod.POST, loginPage);
        return this;
    }

    public FormLoginConfigurer permitAll() {
        permitAll = true;
        return this;
    }
}
