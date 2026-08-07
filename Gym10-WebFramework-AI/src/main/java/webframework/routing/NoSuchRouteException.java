package webframework.routing;

/**
 * No registered route's path template matches the requested path.
 *
 * <p>Mapped to 404 by a built-in rule, so the two routing misses travel the same road as every other
 * failure instead of being special-cased inside the pipeline (BUILD_SPEC D).
 */
public class NoSuchRouteException extends RuntimeException {

    public NoSuchRouteException(String message) {
        super(message);
    }
}
