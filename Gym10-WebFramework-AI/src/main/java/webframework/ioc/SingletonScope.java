package webframework.ioc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * One instance per registration, for the application's lifetime.
 *
 * <p>Construction is lazy: the provider is not called until the first {@code get}. Keyed by name so
 * a single scope instance can safely be shared by several registrations.
 */
public final class SingletonScope implements Lifecycle {

    private final Map<String, Object> instances = new ConcurrentHashMap<>();

    @Override
    public Object get(String name, Supplier<Object> provider) {
        // computeIfAbsent would deadlock on a recursive dependency resolving through the same map,
        // so check-then-put, and let the map's own atomicity settle a race between two threads.
        Object existing = instances.get(name);
        if (existing != null) {
            return existing;
        }
        Object created = provider.get();
        Object raced = instances.putIfAbsent(name, created);
        return raced != null ? raced : created;
    }
}
