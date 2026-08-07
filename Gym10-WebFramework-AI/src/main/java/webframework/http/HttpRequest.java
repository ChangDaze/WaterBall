package webframework.http;

import java.util.Map;

/**
 * Everything the layers above the server adapter are allowed to know about an inbound request.
 *
 * <p>BUILD_SPEC 2.5: the vendor HTTP API ({@code com.sun.net.httpserver}) never appears above this
 * interface, which is what makes the pipeline unit-testable without opening a socket.
 */
public interface HttpRequest {

    HttpMethod getMethod();

    /**
     * @return the request path, without the query string
     */
    String getPath();

    /**
     * @return the header value, or {@code null}. Names are compared case-insensitively.
     */
    String getHeader(String name);

    /**
     * @return the value bound to a {@code {name}} path segment, or {@code null} if this request has
     *         no such binding
     */
    String getPathVariable(String name);

    /**
     * @return the first value of a query-string parameter, or {@code null}
     */
    String getQueryParameter(String name);

    /**
     * Writes the bindings extracted by the winning route.
     *
     * <p>BUILD_SPEC 2.2: {@code {userId}} is not part of HTTP, so this binding cannot exist before a
     * route has matched. The Router is the single collaborator allowed to call this, exactly once,
     * before the handler runs.
     *
     * @throws IllegalStateException if called more than once
     */
    void setPathVariables(Map<String, String> pathVariables);

    /**
     * Deserializes the body using the serializer chosen by this request's {@code content-type}.
     *
     * <p>BUILD_SPEC 2.4: this call fails in two different ways and they must stay distinguishable —
     * an unsupported {@code content-type} is a 500, whereas the DTO rejecting its own contents is a
     * 400. Implementations must therefore let the body type's own exceptions through untouched
     * rather than wrapping everything in one deserialization exception.
     */
    <T> T readBodyAsObject(Class<T> type);
}
