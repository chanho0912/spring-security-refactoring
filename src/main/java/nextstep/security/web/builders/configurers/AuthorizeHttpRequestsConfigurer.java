package nextstep.security.web.builders.configurers;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.security.access.RequestMatcher;
import nextstep.security.access.RequestMatcherEntry;
import nextstep.security.authorization.AuthorizationFilter;
import nextstep.security.authorization.AuthorizationManager;
import nextstep.security.authorization.RequestMatcherDelegatingAuthorizationManager;
import nextstep.security.web.builders.HttpSecurity;

import java.util.ArrayList;
import java.util.List;

public class AuthorizeHttpRequestsConfigurer implements SecurityConfigurer {

    private final List<RequestMatcherEntry<AuthorizationManager<HttpServletRequest>>> mappings;

    public AuthorizeHttpRequestsConfigurer() {
        this.mappings = new ArrayList<>();
    }

    @Override
    public void init(HttpSecurity http) {
    }

    @Override
    public void configure(HttpSecurity http) {
        RequestMatcherDelegatingAuthorizationManager<HttpServletRequest> requestMatcherDelegatingAuthorizationManager =
                new RequestMatcherDelegatingAuthorizationManager<>(this.mappings);

        AuthorizationFilter authorizationFilter = new AuthorizationFilter(requestMatcherDelegatingAuthorizationManager);
        http.addFilter(authorizationFilter);
    }

    public AuthorizeHttpRequestsConfigurer requestsMatcher(RequestMatcher requestMatcher,
                                                           AuthorizationManager<HttpServletRequest> manager) {

        this.mappings.add(new RequestMatcherEntry<>(requestMatcher, manager));
        return this;
    }

}
