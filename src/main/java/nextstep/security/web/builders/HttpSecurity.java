package nextstep.security.web.builders;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import nextstep.security.config.DefaultSecurityFilterChain;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.web.builders.csrf.CsrfConfigurer;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public class HttpSecurity {

    private final LinkedHashMap<Class<? extends SecurityConfigurer>, List<SecurityConfigurer>> configurers =
            new LinkedHashMap<>();
    private List<OrderedFilter> filters = new ArrayList<>();

    @SuppressWarnings("unchecked")
    private <C extends SecurityConfigurer> C getOrApply(C configurer) {
        C existingConfig = (C) getConfigurer(configurer.getClass());
        if (existingConfig != null) {
            return existingConfig;
        }
        return apply(configurer);
    }

    public SecurityFilterChain build() {
        synchronized (this.configurers) {
            init();
            configure();
            return performBuild();
        }
    }

    public DefaultSecurityFilterChain performBuild() {
        List<Filter> sorted = new ArrayList<>();
        for (OrderedFilter filter : this.filters) {
            sorted.add(filter.filter);
        }
        return new DefaultSecurityFilterChain(sorted);
    }


    @SuppressWarnings("unchecked")
    public <C extends SecurityConfigurer> C getConfigurer(Class<C> clazz) {
        List<SecurityConfigurer> configs = this.configurers.get(clazz);
        if (configs == null) {
            return null;
        }
        Assert.state(configs.size() == 1,
                     () -> "Only one configurer expected for type " + clazz + ", but got " + configs);
        return (C) configs.get(0);
    }

    public <C extends SecurityConfigurer> C apply(C configurer) {
        add(configurer);
        return configurer;
    }

    private <C extends SecurityConfigurer> void add(C configurer) {
        Class<? extends SecurityConfigurer> clazz = configurer.getClass();
        List<SecurityConfigurer> configs = new ArrayList<>(1);
        configs.add(configurer);
        this.configurers.put(clazz, configs);
    }

    private void init() {
        Collection<SecurityConfigurer> configurers = getConfigurers();
        for (SecurityConfigurer configurer : configurers) {
            configurer.init(this);
        }
    }

    private void configure() {
        Collection<SecurityConfigurer> configurers = getConfigurers();
        for (SecurityConfigurer configurer : configurers) {
            configurer.configure(this);
        }
    }

    private Collection<SecurityConfigurer> getConfigurers() {
        List<SecurityConfigurer> result = new ArrayList<>();
        for (List<SecurityConfigurer> configs : this.configurers.values()) {
            result.addAll(configs);
        }
        return result;
    }

    // csrf
    public HttpSecurity csrf(Customizer<CsrfConfigurer> csrfCustomizer) {
        csrfCustomizer.customize(getOrApply(new CsrfConfigurer()));
        return HttpSecurity.this;
    }

    public HttpSecurity addFilter(Filter filter) {
//        Integer order = this.filterOrders.getOrder(filter.getClass());
        // TODO: order
        this.filters.add(new OrderedFilter(filter, 1));
        return this;
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
