package nextstep.security.web.builders;

public interface SecurityConfigurer<T, E extends SecurityBuilder<T>> {

    void init(E builder);

    void configure(E builder);
}
