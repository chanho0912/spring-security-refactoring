package nextstep.security.access;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

public class AndRequestMatcher implements RequestMatcher {

    private final Set<RequestMatcher> requestMatchers;

    public AndRequestMatcher(Set<RequestMatcher> requestMatchers) {
        this.requestMatchers = requestMatchers;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        for (RequestMatcher requestMatcher : requestMatchers) {
            if (!requestMatcher.matches(request)) {
                return false;
            }
        }

        return true;
    }
}
