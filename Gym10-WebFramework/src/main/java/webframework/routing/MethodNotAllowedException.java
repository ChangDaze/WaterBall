package webframework.routing;

/**
 * A route's path matched, but no route with that path accepts the request's method.
 *
 * <p>Mapped to 405 by a built-in rule.
 */
public class MethodNotAllowedException extends RuntimeException {

    public MethodNotAllowedException(String message) {
        super(message);
    }
}
