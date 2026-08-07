package webframework.routing;

import java.util.Optional;

import webframework.http.HttpMethod;
import webframework.http.HttpRequest;
import webframework.http.HttpStatus;
import webframework.http.MediaType;

/**
 * One registered route: a method, a path template, and the handler to reflectively invoke.
 *
 * <p>{@link #produces}, {@link #producesNothing} and {@link #respondsWith} are boot-time
 * configuration, called from the registration script while the application is being assembled.
 * Nothing mutates a Route once the server is serving.
 */
public final class Route {

    private final HttpMethod method;
    private final PathPattern pathPattern;
    private final HandlerMethod handlerMethod;

    /** 0..1 — absent means "this route never has a body", i.e. the 204 case of BUILD_SPEC 2.3. */
    private MediaType responseType = MediaType.APPLICATION_JSON;

    private HttpStatus successStatus = HttpStatus.OK;

    public Route(HttpMethod method, String pathTemplate, Class<?> handlerType, String methodName) {
        this.method = method;
        this.pathPattern = PathPattern.parse(pathTemplate);
        this.handlerMethod = new HandlerMethod(handlerType, methodName);
    }

    /**
     * BUILD_SPEC 2.2: returns the bindings alongside the verdict so the Router can hand them to the
     * request without matching a second time.
     */
    public PathMatchResult matchesPath(HttpRequest request) {
        return pathPattern.match(request.getPath());
    }

    public boolean matchesMethod(HttpRequest request) {
        return method == request.getMethod();
    }

    /**
     * Declares the media type this route's successful responses are serialized as.
     */
    public Route produces(MediaType responseType) {
        this.responseType = responseType;
        return this;
    }

    /**
     * Declares that this route never returns a body — its handler is {@code void} and the response
     * is a 204 with no {@code content-type} header.
     */
    public Route producesNothing() {
        this.responseType = null;
        return this;
    }

    /**
     * Overrides the success status for a route whose happy path is not 200 (e.g. 201 on create).
     */
    public Route respondsWith(HttpStatus successStatus) {
        this.successStatus = successStatus;
        return this;
    }

    public Optional<MediaType> getResponseType() {
        return Optional.ofNullable(responseType);
    }

    public HttpStatus getSuccessStatus() {
        return successStatus;
    }

    public HandlerMethod getHandlerMethod() {
        return handlerMethod;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public PathPattern getPathPattern() {
        return pathPattern;
    }

    @Override
    public String toString() {
        return method + " " + pathPattern + " -> " + handlerMethod;
    }
}
