package webframework.ioc;

import java.util.function.Supplier;

/**
 * One instance per HTTP request: the same object for every {@code get} within a request, a different
 * one on the next request.
 *
 * <p>The request boundary is opened and closed by {@code RequestContext}; see
 * {@link RequestScopeStorage} for why it is a {@code ThreadLocal}.
 */
public final class HttpRequestScope implements Lifecycle {

    @Override
    public Object get(String name, Supplier<Object> provider) {
        return RequestScopeStorage.getOrCreate(name, provider);
    }
}
