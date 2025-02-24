package nextstep.security.web.builders;

public abstract class AbstractSecurityBuilder<T> implements SecurityBuilder<T> {
    private boolean building = false;

    @Override
    public T build() {
        synchronized (this) {
            if (building) {
                throw new IllegalStateException("Cannot build twice");
            }
            building = true;
        }
        return doBuild();
    }

    protected abstract T doBuild();
}
