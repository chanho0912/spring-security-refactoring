package nextstep.security.web.builders;

import jakarta.servlet.Filter;
import nextstep.oauth2.web.OAuth2AuthorizationRequestRedirectFilter;
import nextstep.oauth2.web.OAuth2LoginAuthenticationFilter;
import nextstep.security.authentication.BasicAuthenticationFilter;
import nextstep.security.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.security.authorization.AuthorizationFilter;
import nextstep.security.context.SecurityContextHolderFilter;
import nextstep.security.web.csrf.CsrfFilter;

import java.util.HashMap;
import java.util.Map;

class FilterOrderRegistration {
    private final Map<String, Integer> filterToOrder = new HashMap();

    FilterOrderRegistration() {
        Step order = new Step(100, 100);
        this.put(CsrfFilter.class, order.next());
        this.put(SecurityContextHolderFilter.class, order.next());
        this.put(UsernamePasswordAuthenticationFilter.class, order.next());
        this.put(BasicAuthenticationFilter.class, order.next());
        this.put(OAuth2AuthorizationRequestRedirectFilter.class, order.next());
        this.put(OAuth2LoginAuthenticationFilter.class, order.next());
        this.put(AuthorizationFilter.class, order.next());
    }

    void put(Class<? extends Filter> filter, int position) {
        this.filterToOrder.putIfAbsent(filter.getName(), position);
    }

    Integer getOrder(Class<?> clazz) {
        while(clazz != null) {
            Integer result = this.filterToOrder.get(clazz.getName());
            if (result != null) {
                return result;
            }

            clazz = clazz.getSuperclass();
        }

        return null;
    }

    private static class Step {
        private int value;
        private final int stepSize;

        Step(int initialValue, int stepSize) {
            this.value = initialValue;
            this.stepSize = stepSize;
        }

        int next() {
            int value = this.value;
            this.value += this.stepSize;
            return value;
        }
    }
}
