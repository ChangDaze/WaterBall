package webframework.http;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The framework's own response representation: status, headers, and an already-serialized body.
 *
 * <p>BUILD_SPEC 2.3 decides who owns {@code content-type}, and the static factories below are that
 * decision made explicit — {@link #noContent()} is the only way to build a 204 and it cannot carry
 * a {@code content-type} header at all.
 */
public final class HttpResponse {

    private final HttpStatus status;
    private final Map<String, String> headers;
    private final String body;

    private HttpResponse(HttpStatus status, Map<String, String> headers, String body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    /**
     * A response with a body: the {@code content-type} comes from the caller (for a success, that is
     * the route's configured response media type).
     */
    public static HttpResponse of(HttpStatus status, MediaType contentType, String body) {
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("content-type", contentType.toString());
        return new HttpResponse(status, headers, body);
    }

    /**
     * A plain-text response. Every error body in the framework takes this shape (BUILD_SPEC D).
     */
    public static HttpResponse text(HttpStatus status, String body) {
        return of(status, MediaType.TEXT_PLAIN, body);
    }

    /**
     * BUILD_SPEC 2.3 case 3: no body and no {@code content-type} header whatsoever.
     */
    public static HttpResponse noContent() {
        return new HttpResponse(HttpStatus.NO_CONTENT, new LinkedHashMap<>(), null);
    }

    public HttpResponse withHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    /**
     * @return the serialized body, or {@code null} when there is none
     */
    public String getBody() {
        return body;
    }

    public boolean hasBody() {
        return body != null;
    }
}
