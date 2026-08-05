package webframework.routing;

/**
 * What the Router concluded about a request: the winning route, or which kind of miss it was.
 *
 * <p>The two kinds of miss are distinct on purpose — an unknown path is a 404 while a known path
 * with the wrong verb is a 405 (BUILD_SPEC D).
 */
public final class RoutingResult {

    public enum Status {
        MATCHED, NO_PATH_MATCH, METHOD_NOT_ALLOWED
    }

    private static final RoutingResult NO_PATH_MATCH =
            new RoutingResult(Status.NO_PATH_MATCH, null);
    private static final RoutingResult METHOD_NOT_ALLOWED =
            new RoutingResult(Status.METHOD_NOT_ALLOWED, null);

    private final Status status;
    private final Route route;

    private RoutingResult(Status status, Route route) {
        this.status = status;
        this.route = route;
    }

    public static RoutingResult matched(Route route) {
        return new RoutingResult(Status.MATCHED, route);
    }

    public static RoutingResult noPathMatch() {
        return NO_PATH_MATCH;
    }

    public static RoutingResult methodNotAllowed() {
        return METHOD_NOT_ALLOWED;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isMatched() {
        return status == Status.MATCHED;
    }

    /**
     * @return the winning route, or {@code null} when nothing matched
     */
    public Route getRoute() {
        return route;
    }
}
