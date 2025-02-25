package nextstep.security.web.builders.configurers;

import nextstep.security.access.RequestMatcher;
import nextstep.security.access.RequestMatcherEntry;
import nextstep.security.access.hierarchicalroles.NullRoleHierarchy;
import nextstep.security.access.hierarchicalroles.RoleHierarchy;
import nextstep.security.authorization.AuthorizationFilter;
import nextstep.security.authorization.AuthorizationManager;
import nextstep.security.authorization.RequestMatcherDelegatingAuthorizationManager;
import nextstep.security.web.builders.HttpSecurity;
import nextstep.security.web.builders.SecurityConfigurer;

import java.util.ArrayList;
import java.util.List;

public class AuthorizeHttpRequestsConfigurer implements SecurityConfigurer {

    private List<RequestMatcherEntry<AuthorizationManager>> mappings;
    private RoleHierarchy roleHierarchy;

    public AuthorizeHttpRequestsConfigurer() {
        this(new NullRoleHierarchy());
    }

    public AuthorizeHttpRequestsConfigurer(RoleHierarchy roleHierarchy) {
        this.mappings = new ArrayList<>();
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public void init(HttpSecurity http) {
    }

    @Override
    public void configure(HttpSecurity http) {
        RequestMatcherDelegatingAuthorizationManager requestMatcherDelegatingAuthorizationManager =
            new RequestMatcherDelegatingAuthorizationManager(this.mappings);

        AuthorizationFilter authorizationFilter = new AuthorizationFilter(requestMatcherDelegatingAuthorizationManager);
        http.addFilter(authorizationFilter);
    }

    public AuthorizeHttpRequestsConfigurer requestsMatcher(RequestMatcher requestMatcher, AuthorizationManager manager) {
        this.mappings.add(new RequestMatcherEntry<>(requestMatcher, manager));
        return this;
    }

}
