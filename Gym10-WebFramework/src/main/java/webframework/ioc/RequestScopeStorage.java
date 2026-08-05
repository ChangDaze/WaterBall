package webframework.ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Where request-scoped instances live between the opening and closing of one request.
 *
 * <p>BUILD_SPEC 7 asks for this choice to be stated rather than assumed: the storage is a
 * {@code ThreadLocal}, not a context threaded through method signatures. Threading a context
 * explicitly would mean {@link Container#get} taking a web-shaped parameter, which would drag HTTP
 * into every non-web use of the container. The cost of the ThreadLocal is that a handler which hands
 * work to another thread does not carry the scope with it, and that the scope must be closed
 * religiously — hence {@link #close()} living in a {@code finally} (BUILD_SPEC 2.6).
 *
 * <p>Static because several {@link HttpRequestScope} instances — one per registration — must all see
 * the same request boundary.
 */
public final class RequestScopeStorage {

    private static final ThreadLocal<Map<String, Object>> INSTANCES = new ThreadLocal<>();

    private RequestScopeStorage() {
    }

    public static void open() {
        INSTANCES.set(new HashMap<>());
    }

    /**
     * Discards this request's instances. Safe to call when nothing is open, and — being the
     * {@code finally} half of the pair — it must never throw.
     */
    public static void close() {
        INSTANCES.remove();
    }

    public static boolean isOpen() {
        return INSTANCES.get() != null;
    }

    /**
     * @return how many request-scoped instances the current request has created; 0 when closed.
     *         Exists so a test can assert the scope really was emptied.
     */
    public static int size() {
        Map<String, Object> instances = INSTANCES.get();
        return instances == null ? 0 : instances.size();
    }

    public static Object getOrCreate(String name, Supplier<Object> provider) {
        Map<String, Object> instances = INSTANCES.get();
        if (instances == null) {
            throw new IllegalStateException("No HTTP request scope is open on thread "
                    + Thread.currentThread().getName() + "; \"" + name
                    + "\" is request-scoped and cannot be resolved outside a request");
        }
        Object existing = instances.get(name);
        if (existing != null) {
            return existing;
        }
        Object created = provider.get();
        instances.put(name, created);
        return created;
    }
}
