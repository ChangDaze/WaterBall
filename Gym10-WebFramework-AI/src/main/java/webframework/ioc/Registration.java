package webframework.ioc;

import java.util.function.Supplier;

/**
 * One entry in the container: what it is called, what it is, how often it is built, and how.
 *
 * <p>The provider is the piece that keeps {@link Lifecycle} implementations free of reflection — a
 * scope decides only <em>whether</em> to build, never <em>how</em>.
 */
public final class Registration {

    private final String name;
    private final Class<?> type;
    private final Lifecycle lifecycle;
    private final Supplier<Object> provider;

    public Registration(String name, Class<?> type, Lifecycle lifecycle, Supplier<Object> provider) {
        this.name = name;
        this.type = type;
        this.lifecycle = lifecycle;
        this.provider = provider;
    }

    /**
     * Asks this registration's lifecycle for an instance, building one through the provider only if
     * the lifecycle decides it needs to.
     */
    public Object resolve() {
        return lifecycle.get(name, provider);
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    @Override
    public String toString() {
        return name + ": " + type.getName() + " (" + lifecycle.getClass().getSimpleName() + ")";
    }
}
