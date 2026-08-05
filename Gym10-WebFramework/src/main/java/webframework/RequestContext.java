package webframework;

import webframework.http.HttpRequest;
import webframework.ioc.RequestScopeStorage;

/**
 * The lifetime of one request, as an {@link AutoCloseable}.
 *
 * <p>BUILD_SPEC 2.6: the request scope is opened before dispatch and closed no matter how dispatch
 * ends. Being a resource is the whole point — {@code try (RequestContext ctx = open(request))} makes
 * the {@code finally} impossible to forget, and forgetting it is exactly how request-scoped
 * instances leak from one request into the next.
 */
public final class RequestContext implements AutoCloseable {

    private final HttpRequest request;

    private RequestContext(HttpRequest request) {
        this.request = request;
    }

    public static RequestContext open(HttpRequest request) {
        RequestScopeStorage.open();
        return new RequestContext(request);
    }

    public HttpRequest getRequest() {
        return request;
    }

    @Override
    public void close() {
        RequestScopeStorage.close();
    }
}
