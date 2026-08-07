package webframework.testsupport;

import java.util.LinkedHashMap;
import java.util.Map;

import webframework.http.HttpMethod;
import webframework.http.HttpRequest;

/**
 * An {@link HttpRequest} with no socket behind it.
 *
 * <p>This class only exists because of BUILD_SPEC 2.5 — because the vendor exchange stops at the
 * adapter, the routing and dispatch tests need nothing more than this to run.
 *
 * <p>It counts {@code setPathVariables} calls, which is how the "written exactly once, only on the
 * winning route" rule of BUILD_SPEC 2.2 gets asserted rather than assumed.
 */
public final class FakeHttpRequest implements HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> headers = new LinkedHashMap<>();
    private final Map<String, String> queryParameters = new LinkedHashMap<>();

    private Map<String, String> pathVariables = Map.of();
    private int setPathVariablesCallCount;
    private Object body;

    public FakeHttpRequest(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public static FakeHttpRequest get(String path) {
        return new FakeHttpRequest(HttpMethod.GET, path);
    }

    public static FakeHttpRequest post(String path) {
        return new FakeHttpRequest(HttpMethod.POST, path);
    }

    public FakeHttpRequest withHeader(String name, String value) {
        headers.put(name.toLowerCase(), value);
        return this;
    }

    public FakeHttpRequest withQueryParameter(String name, String value) {
        queryParameters.put(name, value);
        return this;
    }

    /**
     * Sets the object {@link #readBodyAsObject} will hand back, skipping deserialization entirely.
     */
    public FakeHttpRequest withBody(Object body) {
        this.body = body;
        return this;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name.toLowerCase());
    }

    @Override
    public String getPathVariable(String name) {
        return pathVariables.get(name);
    }

    @Override
    public String getQueryParameter(String name) {
        return queryParameters.get(name);
    }

    @Override
    public void setPathVariables(Map<String, String> pathVariables) {
        setPathVariablesCallCount++;
        this.pathVariables = Map.copyOf(pathVariables);
    }

    @Override
    public <T> T readBodyAsObject(Class<T> type) {
        return type.cast(body);
    }

    public int getSetPathVariablesCallCount() {
        return setPathVariablesCallCount;
    }

    public Map<String, String> getPathVariables() {
        return pathVariables;
    }
}
