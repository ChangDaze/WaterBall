package webframework.routing;

import java.util.ArrayList;
import java.util.List;

import webframework.http.HttpMethod;
import webframework.http.HttpRequest;

/**
 * The route table. Registration happens declaratively at boot (BUILD_SPEC B); matching happens once
 * per request.
 *
 * <pre>{@code
 * router.post("/api/users",           UserController.class, "register").respondsWith(CREATED);
 * router.patch("/api/users/{userId}", UserController.class, "rename").producesNothing();
 * }</pre>
 */
public final class Router {

    private final List<Route> routes = new ArrayList<>();

    public Route get(String path, Class<?> handlerType, String methodName) {
        return register(HttpMethod.GET, path, handlerType, methodName);
    }

    public Route post(String path, Class<?> handlerType, String methodName) {
        return register(HttpMethod.POST, path, handlerType, methodName);
    }

    public Route put(String path, Class<?> handlerType, String methodName) {
        return register(HttpMethod.PUT, path, handlerType, methodName);
    }

    public Route patch(String path, Class<?> handlerType, String methodName) {
        return register(HttpMethod.PATCH, path, handlerType, methodName);
    }

    public Route delete(String path, Class<?> handlerType, String methodName) {
        return register(HttpMethod.DELETE, path, handlerType, methodName);
    }

    /**
     * @return the new route, so the caller can configure its response type and success status
     */
    public Route register(HttpMethod method, String path, Class<?> handlerType, String methodName) {
        Route route = new Route(method, path, handlerType, methodName);
        routes.add(route);
        return route;
    }

    /**
     * Finds the route for a request and, on a match, writes the extracted path variables into it.
     *
     * <p>BUILD_SPEC 2.2: the bindings are written exactly once, only for the winning route, and
     * before the handler runs. Losing candidates never touch the request — a route whose path
     * matched but whose method did not must not leave its variables behind.
     *
     * <p>Registration order decides ties, so a literal template registered before a variable one
     * wins for the paths they both cover.
     */
    public RoutingResult route(HttpRequest request) {
        boolean somePathMatched = false;
        for (Route route : routes) {
            PathMatchResult pathMatch = route.matchesPath(request);
            if (!pathMatch.isMatched()) {
                continue;
            }
            somePathMatched = true;
            if (!route.matchesMethod(request)) {
                continue;
            }
            request.setPathVariables(pathMatch.getPathVariables());
            return RoutingResult.matched(route);
        }
        return somePathMatched ? RoutingResult.methodNotAllowed() : RoutingResult.noPathMatch();
    }

    public List<Route> getRoutes() {
        return List.copyOf(routes);
    }
}
