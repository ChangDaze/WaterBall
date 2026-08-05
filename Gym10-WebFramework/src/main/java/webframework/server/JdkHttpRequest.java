package webframework.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import webframework.http.HttpMethod;
import webframework.http.HttpRequest;
import webframework.http.MediaType;
import webframework.serialization.BodySerializer;
import webframework.serialization.SerializerRegistry;
import webframework.serialization.UnsupportedMediaTypeException;
import webframework.serialization.Validatable;

/**
 * Adapter from the JDK's {@code HttpExchange} to the framework's {@link HttpRequest}.
 *
 * <p>BUILD_SPEC 2.5: this class and {@link JdkHttpServerAdapter} are the only two places in
 * production code allowed to import {@code com.sun.net.httpserver}. Swapping the underlying server
 * means rewriting these two files and nothing else.
 */
public final class JdkHttpRequest implements HttpRequest {

    private final HttpExchange exchange;
    private final SerializerRegistry serializers;
    private final HttpMethod method;
    private final Map<String, String> queryParameters;

    private Map<String, String> pathVariables = Collections.emptyMap();
    private boolean pathVariablesAssigned;
    private String bodyText;

    public JdkHttpRequest(HttpExchange exchange, SerializerRegistry serializers) {
        this.exchange = exchange;
        this.serializers = serializers;
        this.method = HttpMethod.parse(exchange.getRequestMethod());
        this.queryParameters = parseQuery(exchange.getRequestURI().getRawQuery());
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public String getPath() {
        return exchange.getRequestURI().getPath();
    }

    @Override
    public String getHeader(String name) {
        // The JDK's Headers map normalizes keys, so lookups are already case-insensitive.
        return exchange.getRequestHeaders().getFirst(name);
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
        // BUILD_SPEC 2.2 accepts mutability here on the condition that exactly one collaborator
        // writes, exactly once. This guard is that condition, enforced rather than documented.
        if (pathVariablesAssigned) {
            throw new IllegalStateException("Path variables have already been set on this request");
        }
        this.pathVariables = Map.copyOf(pathVariables);
        this.pathVariablesAssigned = true;
    }

    /**
     * BUILD_SPEC C: the deserializer is chosen by this request's {@code content-type}.
     *
     * <p>BUILD_SPEC 2.4: note what is <em>not</em> here. Nothing wraps the two calls below in a
     * catch-all deserialization exception. An unsupported media type raises
     * {@link UnsupportedMediaTypeException} and lands on the 500 path, while anything the body type
     * itself throws — a DTO refusing a malformed email, say — travels outwards with its own type
     * intact so the exception rules can still turn it into a 400.
     */
    @Override
    public <T> T readBodyAsObject(Class<T> type) {
        BodySerializer serializer = serializers.requireByMediaType(requestMediaType());
        T body = serializer.deserialize(readBodyAsText(), type);
        if (body instanceof Validatable validatable) {
            validatable.validate();
        }
        return body;
    }

    private MediaType requestMediaType() {
        String contentType = getHeader("content-type");
        if (contentType == null || contentType.isBlank()) {
            throw new UnsupportedMediaTypeException("Request has no content-type header");
        }
        try {
            return MediaType.parse(contentType);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedMediaTypeException("Unsupported media type \"" + contentType + "\"");
        }
    }

    /**
     * Reads the body once and remembers it, so two {@code readBodyAsObject} calls do not race for a
     * stream that can only be consumed a single time.
     */
    private String readBodyAsText() {
        if (bodyText == null) {
            try (InputStream in = exchange.getRequestBody()) {
                bodyText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new UncheckedIOException("Cannot read request body", e);
            }
        }
        return bodyText;
    }

    private static Map<String, String> parseQuery(String rawQuery) {
        Map<String, String> parameters = new LinkedHashMap<>();
        if (rawQuery == null || rawQuery.isEmpty()) {
            return parameters;
        }
        for (String pair : rawQuery.split("&")) {
            if (pair.isEmpty()) {
                continue;
            }
            int separator = pair.indexOf('=');
            String name = separator < 0 ? pair : pair.substring(0, separator);
            String value = separator < 0 ? "" : pair.substring(separator + 1);
            // First value wins, matching getQueryParameter's "the first value" contract.
            parameters.putIfAbsent(decode(name), decode(value));
        }
        return parameters;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
