package nextstep.security.web.builders;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class AbstractConfiguredSecurityBuilder<T, E extends SecurityBuilder<T>>
        extends AbstractSecurityBuilder<T> {

    private final LinkedHashMap<Class<? extends SecurityConfigurer<T, E>>, List<SecurityConfigurer<T, E>>> configurers =
            new LinkedHashMap<>();

    @Override
    protected final T doBuild() {
        synchronized (this.configurers) {
            init();
            configure();
            return performBuild();
        }
    }

    @SuppressWarnings("unchecked")
    public <C extends SecurityConfigurer<T, E>> C getConfigurer(Class<C> clazz) {
        List<SecurityConfigurer<T, E>> configs = this.configurers.get(clazz);
        if (configs == null) {
            return null;
        }
        Assert.state(configs.size() == 1,
                     () -> "Only one configurer expected for type " + clazz + ", but got " + configs);
        return (C) configs.get(0);
    }

    public <C extends SecurityConfigurer<T, E>> C apply(C configurer) {
        add(configurer);
        return configurer;
    }

    @SuppressWarnings("unchecked")
    private <C extends SecurityConfigurer<T, E>> void add(C configurer) {
        Class<? extends SecurityConfigurer<T, E>> clazz = (Class<? extends SecurityConfigurer<T, E>>) configurer
                .getClass();
        List<SecurityConfigurer<T, E>> configs = new ArrayList<>(1);
        configs.add(configurer);
        this.configurers.put(clazz, configs);
    }

    @SuppressWarnings("unchecked")
    private void init() {
        Collection<SecurityConfigurer<T, E>> configurers = getConfigurers();
        for (SecurityConfigurer<T, E> configurer : configurers) {
            configurer.init((E) this);
        }
    }

    @SuppressWarnings("unchecked")
    private void configure() {
        Collection<SecurityConfigurer<T, E>> configurers = getConfigurers();
        for (SecurityConfigurer<T, E> configurer : configurers) {
            configurer.configure((E) this);
        }
    }

    private Collection<SecurityConfigurer<T, E>> getConfigurers() {
        List<SecurityConfigurer<T, E>> result = new ArrayList<>();
        for (List<SecurityConfigurer<T, E>> configs : this.configurers.values()) {
            result.addAll(configs);
        }
        return result;
    }

    protected abstract T performBuild();
}
