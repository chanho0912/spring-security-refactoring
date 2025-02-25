package nextstep.security.web.builders.configurers;

import nextstep.security.access.AndRequestMatcher;
import nextstep.security.access.MvcRequestMatcher;
import nextstep.security.access.NegatedRequestMatcher;
import nextstep.security.access.OrRequestMatcher;
import nextstep.security.access.RequestMatcher;
import nextstep.security.web.builders.HttpSecurity;
import nextstep.security.web.csrf.CsrfFilter;

import java.util.HashSet;
import java.util.Set;

public class CsrfConfigurer implements SecurityConfigurer {

    private final RequestMatcher requireCsrfProtectionMatcher;
    private final Set<RequestMatcher> ignoredCsrfProtectionMatchers;

    public CsrfConfigurer() {
        this.requireCsrfProtectionMatcher = CsrfFilter.DEFAULT_CSRF_MATCHER;
        this.ignoredCsrfProtectionMatchers = new HashSet<>();
    }

    public CsrfConfigurer ignoringRequestMatchers(String... patterns) {
        for (String pattern : patterns) {
            this.ignoredCsrfProtectionMatchers.add(new MvcRequestMatcher(null, pattern));
        }

        return this;
    }

    @Override
    public void init(HttpSecurity http) {
    }

    @Override
    public void configure(HttpSecurity http) {
        CsrfFilter filter = new CsrfFilter(this.ignoredCsrfProtectionMatchers);
        RequestMatcher requireCsrfProtectionMatcher = getRequireCsrfProtectionMatcher();
        if (requireCsrfProtectionMatcher != null) {
            filter.setRequireCsrfProtectionMatcher(requireCsrfProtectionMatcher);
        }

        http.addFilter(filter);
    }

    private RequestMatcher getRequireCsrfProtectionMatcher() {
        if (this.ignoredCsrfProtectionMatchers.isEmpty()) {
            return this.requireCsrfProtectionMatcher;
        }

        return new AndRequestMatcher(
                Set.of(
                        this.requireCsrfProtectionMatcher,
                        new NegatedRequestMatcher(
                                new OrRequestMatcher(
                                        this.ignoredCsrfProtectionMatchers
                                ))));
    }

}
