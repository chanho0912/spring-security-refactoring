package nextstep.security.web.builders;

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
