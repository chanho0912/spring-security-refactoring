package nextstep.security.web.builders;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import nextstep.oauth2.registration.ClientRegistrationRepository;
import nextstep.security.authentication.AuthenticationManager;
import nextstep.security.config.DefaultSecurityFilterChain;
import nextstep.security.config.SecurityFilterChain;
import nextstep.security.web.builders.configurers.AuthorizeHttpRequestsConfigurer;
import nextstep.security.web.builders.configurers.CsrfConfigurer;
import nextstep.security.web.builders.configurers.FormLoginConfigurer;
import nextstep.security.web.builders.configurers.HttpBasicConfigurer;
import nextstep.security.web.builders.configurers.Oauth2LoginConfigurer;
import nextstep.security.web.builders.configurers.SecurityConfigurer;
import nextstep.security.web.builders.configurers.SecurityContextConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpSecurity {

    private final LinkedHashMap<Class<? extends SecurityConfigurer>, List<SecurityConfigurer>> configurers =
            new LinkedHashMap<>();
    private final Map<Class<?>, Object> sharedObjects = new HashMap<>();
    private List<OrderedFilter> filters = new ArrayList<>();
    private final FilterOrderRegistration filterOrderRegistration;
    private final AuthenticationManager authenticationManager;

    public HttpSecurity(AuthenticationManager authenticationManager,
                        ApplicationContext context) {

        this.authenticationManager = authenticationManager;
        this.setSharedObject(ApplicationContext.class, context);
        this.filterOrderRegistration = new FilterOrderRegistration();
    }

    private void configureAuthenticationManager() {
        AuthenticationManager authenticationManager = getSharedObject(AuthenticationManager.class);
        if (authenticationManager == null) {
            authenticationManager = getAuthenticationManager();
            setSharedObject(AuthenticationManager.class, authenticationManager);
        }
    }

    private void configureClientRegistrationRepository() {
        ClientRegistrationRepository clientRegistrationRepository = getSharedObject(ClientRegistrationRepository.class);

        if (clientRegistrationRepository == null) {
            clientRegistrationRepository = getClientRegistrationRepository();
            setSharedObject(ClientRegistrationRepository.class, clientRegistrationRepository);
        }
    }

    private AuthenticationManager getAuthenticationManager() {
        return this.authenticationManager;
    }

    private ClientRegistrationRepository getClientRegistrationRepository() {
        return getSharedObject(ApplicationContext.class).getBean(ClientRegistrationRepository.class);
    }

    public SecurityFilterChain build() {
        synchronized (this.configurers) {
            init();
            beforeConfigure();
            configure();
            return performBuild();
        }
    }

    private DefaultSecurityFilterChain performBuild() {
        this.filters.sort(OrderComparator.INSTANCE);
        List<Filter> sorted = new ArrayList<>(this.filters.size());

        for (OrderedFilter filter : this.filters) {
            sorted.add(filter.filter);
        }
        return new DefaultSecurityFilterChain(sorted);
    }

    @SuppressWarnings("unchecked")
    private <C extends SecurityConfigurer> C getOrApply(C configurer) {
        C existingConfig = (C) getConfigurer(configurer.getClass());
        if (existingConfig != null) {
            return existingConfig;
        }
        return apply(configurer);
    }

    @SuppressWarnings("unchecked")
    private <C extends SecurityConfigurer> C getConfigurer(Class<C> clazz) {
        List<SecurityConfigurer> configs = this.configurers.get(clazz);
        if (configs == null) {
            return null;
        }
        Assert.state(configs.size() == 1,
                     () -> "Only one configurer expected for type " + clazz + ", but got " + configs);
        return (C) configs.get(0);
    }

    private <C extends SecurityConfigurer> C apply(C configurer) {
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

    private void beforeConfigure() {
        configureAuthenticationManager();
        configureClientRegistrationRepository();
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

    // form login
    public HttpSecurity formLogin(Customizer<FormLoginConfigurer> formLoginCustomizer) {
        formLoginCustomizer.customize(getOrApply(new FormLoginConfigurer()));
        return HttpSecurity.this;
    }

    // http basic
    public HttpSecurity httpBasic(Customizer<HttpBasicConfigurer> httpBasicCustomizer) {
        httpBasicCustomizer.customize(getOrApply(new HttpBasicConfigurer()));
        return HttpSecurity.this;
    }

    // oauth2 login
    public HttpSecurity oauth2Login(Customizer<Oauth2LoginConfigurer> oauth2LoginCustomizer) {
        oauth2LoginCustomizer.customize(getOrApply(new Oauth2LoginConfigurer()));
        return HttpSecurity.this;
    }

    // security context
    public HttpSecurity securityContext(Customizer<SecurityContextConfigurer> securityContextCustomizer) {
        securityContextCustomizer.customize(getOrApply(new SecurityContextConfigurer()));
        return HttpSecurity.this;
    }

    // authorizeHttpRequests
    public HttpSecurity authorizeHttpRequests(Customizer<AuthorizeHttpRequestsConfigurer> authorizeHttpRequestsCustomizer) {
        authorizeHttpRequestsCustomizer.customize(getOrApply(new AuthorizeHttpRequestsConfigurer()));
        return HttpSecurity.this;
    }

    public void addFilter(Filter filter) {
        Integer order = this.filterOrderRegistration.getOrder(filter.getClass());
        this.filters.add(new OrderedFilter(filter, order));
    }

    @SuppressWarnings("unchecked")
    public <C> C getSharedObject(Class<C> sharedType) {
        return (C) this.sharedObjects.get(sharedType);
    }

    public <C> void setSharedObject(Class<C> sharedType, C object) {
        this.sharedObjects.put(sharedType, object);
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
