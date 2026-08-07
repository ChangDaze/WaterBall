package webframework.ioc;

import java.util.function.Supplier;

/**
 * A fresh instance on every {@code get}.
 */
public final class PrototypeScope implements Lifecycle {

    @Override
    public Object get(String name, Supplier<Object> provider) {
        return provider.get();
    }
}
