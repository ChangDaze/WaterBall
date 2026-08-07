package webframework.routing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import webframework.http.HttpMethod;
import webframework.http.HttpRequest;
import webframework.testsupport.FakeHttpRequest;

class RouterTest {

    /** Public so reflection can see the handler methods, as it would for a real controller. */
    public static final class Handlers {

        public String show(HttpRequest request) {
            return "shown";
        }

        public String list(HttpRequest request) {
            return "listed";
        }
    }

    private Router router;

    @BeforeEach
    void setUp() {
        router = new Router();
    }

    @Test
    @DisplayName("no template matching the path at all is a 404-shaped miss")
    void unknownPathIsNoPathMatch() {
        router.get("/api/users", Handlers.class, "list");

        RoutingResult result = router.route(FakeHttpRequest.get("/api/orders"));

        assertEquals(RoutingResult.Status.NO_PATH_MATCH, result.getStatus());
    }

    @Test
    @DisplayName("a known path with an unregistered method is a 405-shaped miss, not a 404")
    void knownPathWrongMethodIsMethodNotAllowed() {
        router.get("/api/users", Handlers.class, "list");

        RoutingResult result = router.route(FakeHttpRequest.post("/api/users"));

        assertEquals(RoutingResult.Status.METHOD_NOT_ALLOWED, result.getStatus());
    }

    @Test
    @DisplayName("the winning route's path variables are written into the request exactly once")
    void pathVariablesAreWrittenOnceOnTheWinner() {
        router.get("/api/users/{userId}", Handlers.class, "show");
        FakeHttpRequest request = FakeHttpRequest.get("/api/users/3");

        RoutingResult result = router.route(request);

        assertEquals(RoutingResult.Status.MATCHED, result.getStatus());
        assertEquals(1, request.getSetPathVariablesCallCount());
        assertEquals(Map.of("userId", "3"), request.getPathVariables());
    }

    @Test
    @DisplayName("routes that matched the path but lost on method leave the request untouched")
    void losingCandidatesDoNotWriteTheirVariables() {
        // Both templates match /api/users/3. The first loses on method, the second wins — and the
        // request must end up with the winner's bindings written a single time.
        router.post("/api/users/{postOnlyId}", Handlers.class, "show");
        router.get("/api/users/{userId}", Handlers.class, "show");
        FakeHttpRequest request = FakeHttpRequest.get("/api/users/3");

        router.route(request);

        assertEquals(1, request.getSetPathVariablesCallCount());
        assertEquals(Map.of("userId", "3"), request.getPathVariables());
    }

    @Test
    @DisplayName("a 405 miss writes no path variables at all")
    void methodNotAllowedWritesNothing() {
        router.post("/api/users/{userId}", Handlers.class, "show");
        FakeHttpRequest request = FakeHttpRequest.get("/api/users/3");

        router.route(request);

        assertEquals(0, request.getSetPathVariablesCallCount());
    }

    @Test
    @DisplayName("registration order breaks ties, so a literal route can shadow a variable one")
    void registrationOrderDecidesTies() {
        Route literal = router.register(HttpMethod.GET, "/api/users/summary", Handlers.class, "list");
        router.register(HttpMethod.GET, "/api/users/{userId}", Handlers.class, "show");

        RoutingResult result = router.route(FakeHttpRequest.get("/api/users/summary"));

        assertSame(literal, result.getRoute());
    }

    @Test
    @DisplayName("a path match with the wrong method does not stop the search")
    void searchContinuesPastAMethodMismatch() {
        Route patch = router.patch("/api/users/{userId}", Handlers.class, "show");
        Route get = router.get("/api/users/xml", Handlers.class, "list");

        assertSame(get, router.route(FakeHttpRequest.get("/api/users/xml")).getRoute());
        assertSame(patch, router.route(
                new FakeHttpRequest(HttpMethod.PATCH, "/api/users/9")).getRoute());
    }
}
