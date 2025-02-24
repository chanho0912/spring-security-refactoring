package nextstep.security.web.builders;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import nextstep.security.config.DefaultSecurityFilterChain;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpSecurity extends AbstractConfiguredSecurityBuilder<DefaultSecurityFilterChain, HttpSecurity> {

    private List<OrderedFilter> filters = new ArrayList<>();

    @Override
    protected DefaultSecurityFilterChain performBuild() {
        List<Filter> sorted = new ArrayList<>();
        for (OrderedFilter filter : this.filters) {
            sorted.add(filter.filter);
        }
        return new DefaultSecurityFilterChain(sorted);
    }

    @SuppressWarnings("unchecked")
    private <C extends SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity>> C getOrApply(C configurer)
            throws Exception {
        C existingConfig = (C) getConfigurer(configurer.getClass());
        if (existingConfig != null) {
            return existingConfig;
        }
        return apply(configurer);
    }

    private static final class OrderedFilter implements Ordered, Filter {

        private final Filter filter;

        private final int order;

        private OrderedFilter(Filter filter, int order) {
            this.filter = filter;
            this.order = order;
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
                throws IOException, ServletException {
            this.filter.doFilter(servletRequest, servletResponse, filterChain);
        }

        @Override
        public int getOrder() {
            return this.order;
        }

        @Override
        public String toString() {
            return "OrderedFilter{" + "filter=" + this.filter + ", order=" + this.order + '}';
        }

    }

}
