package webframework.http;

/**
 * The HTTP methods the framework understands.
 */
public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS, TRACE, CONNECT;

    /**
     * @throws IllegalArgumentException if the verb is not a known HTTP method
     */
    public static HttpMethod parse(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("HTTP method must not be null");
        }
        return HttpMethod.valueOf(raw.trim().toUpperCase());
    }
}
