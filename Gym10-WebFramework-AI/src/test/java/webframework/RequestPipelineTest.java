package webframework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import webframework.http.HttpMethod;
import webframework.http.HttpRequest;
import webframework.http.HttpResponse;
import webframework.http.HttpStatus;
import webframework.http.MediaType;
import webframework.ioc.HttpRequestScope;
import webframework.ioc.RequestScopeStorage;
import webframework.testsupport.FakeHttpRequest;

/**
 * The dispatch algorithm, driven without a socket — which is the payoff of BUILD_SPEC 2.5.
 */
class RequestPipelineTest {

    public static class Handlers {

        public String text(HttpRequest request) {
            return "plain words";
        }

        public String json(HttpRequest request) {
            return "serialized";
        }

        public void nothing(HttpRequest request) {
        }

        public String unmapped(HttpRequest request) {
            throw new IllegalStateException("something went wrong");
        }

        public String mapped(HttpRequest request) {
            throw new NotAllowedHere("you may not");
        }

        public String messageless(HttpRequest request) {
            throw new IllegalStateException();
        }
    }

    static class NotAllowedHere extends RuntimeException {
        NotAllowedHere(String message) {
            super(message);
        }
    }

    private WebApplication app;

    @BeforeEach
    void setUp() {
        RequestScopeStorage.close();
        app = new WebApplication();
        app.getContainer().register(Handlers.class);
    }

    @Test
    @DisplayName("an unknown path is a 404 with the contract's exact text")
    void unknownPathIs404() {
        app.getRouter().get("/hello", Handlers.class, "text");

        HttpResponse response = app.createPipeline().handle(FakeHttpRequest.get("/nope"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        assertEquals("Cannot find the path \"/nope\"", response.getBody());
        assertEquals("text/plain", response.getHeaders().get("content-type"));
    }

    @Test
    @DisplayName("a known path with the wrong method is a 405 with the contract's exact text")
    void wrongMethodIs405() {
        app.getRouter().get("/hello", Handlers.class, "text");

        HttpResponse response = app.createPipeline().handle(
                new FakeHttpRequest(HttpMethod.DELETE, "/hello"));

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatus());
        assertEquals("The method \"DELETE\" is not allowed on \"/hello\"", response.getBody());
        assertEquals("text/plain", response.getHeaders().get("content-type"));
    }

    @Test
    @DisplayName("a body is serialized as the route's configured media type")
    void bodyUsesTheRoutesMediaType() {
        app.getRouter().get("/text", Handlers.class, "text").produces(MediaType.TEXT_PLAIN);
        app.getRouter().get("/json", Handlers.class, "json");

        HttpResponse text = app.createPipeline().handle(FakeHttpRequest.get("/text"));
        HttpResponse json = app.createPipeline().handle(FakeHttpRequest.get("/json"));

        assertEquals("plain words", text.getBody());
        assertEquals("text/plain", text.getHeaders().get("content-type"));
        assertEquals("\"serialized\"", json.getBody());
        assertEquals("application/json", json.getHeaders().get("content-type"));
    }

    @Test
    @DisplayName("a route's configured success status is used instead of 200")
    void configuredSuccessStatus() {
        app.getRouter().post("/create", Handlers.class, "json").respondsWith(HttpStatus.CREATED);

        HttpResponse response = app.createPipeline().handle(FakeHttpRequest.post("/create"));

        assertEquals(HttpStatus.CREATED, response.getStatus());
    }

    @Test
    @DisplayName("a void handler produces a 204 with no body and no content-type header")
    void voidHandlerProduces204() {
        // BUILD_SPEC 2.3 case 3, asserted where it is decided rather than only over the wire.
        app.getRouter().patch("/quiet", Handlers.class, "nothing").producesNothing();

        HttpResponse response = app.createPipeline().handle(
                new FakeHttpRequest(HttpMethod.PATCH, "/quiet"));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        assertFalse(response.hasBody());
        assertNull(response.getBody());
        assertTrue(response.getHeaders().isEmpty(), "a 204 carries no headers of ours at all");
        assertFalse(response.getHeaders().containsKey("content-type"));
    }

    @Test
    @DisplayName("a route whose media type has no serializer is a 500")
    void unknownResponseMediaTypeIs500() {
        app.getRouter().get("/yaml", Handlers.class, "json")
                .produces(MediaType.parse("application/yaml"));

        HttpResponse response = app.createPipeline().handle(FakeHttpRequest.get("/yaml"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals("Unsupported media type \"application/yaml\"", response.getBody());
    }

    @Test
    @DisplayName("an unmapped exception is a 500 carrying its message")
    void unmappedExceptionIs500() {
        app.getRouter().get("/boom", Handlers.class, "unmapped");

        HttpResponse response = app.createPipeline().handle(FakeHttpRequest.get("/boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals("something went wrong", response.getBody());
        assertEquals("text/plain", response.getHeaders().get("content-type"));
    }

    @Test
    @DisplayName("a mapped exception uses its registered status, with its own message")
    void mappedExceptionUsesItsStatus() {
        app.mapException(NotAllowedHere.class, HttpStatus.FORBIDDEN);
        app.getRouter().get("/nope", Handlers.class, "mapped");

        HttpResponse response = app.createPipeline().handle(FakeHttpRequest.get("/nope"));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatus());
        assertEquals("you may not", response.getBody());
    }

    @Test
    @DisplayName("an exception with no message still produces a usable body")
    void messagelessExceptionStillHasABody() {
        app.getRouter().get("/silent", Handlers.class, "messageless");

        HttpResponse response = app.createPipeline().handle(FakeHttpRequest.get("/silent"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals(IllegalStateException.class.getName(), response.getBody());
    }

    @Test
    @DisplayName("the request scope is closed after a request that threw")
    void requestScopeIsClosedAfterAFailedRequest() {
        WebApplication scoped = new WebApplication();
        scoped.getContainer().register(Handlers.class, new HttpRequestScope());
        scoped.getRouter().get("/boom", Handlers.class, "unmapped");

        HttpResponse response = scoped.createPipeline().handle(FakeHttpRequest.get("/boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
        assertFalse(RequestScopeStorage.isOpen(), "the scope must not outlive a failed request");
        assertEquals(0, RequestScopeStorage.size());
    }

    @Test
    @DisplayName("the handler is resolved from the container on every request")
    void handlerComesFromTheContainerEachTime() {
        // BUILD_SPEC 2.1: were the instance captured at registration, this route could not observe a
        // per-request lifecycle at all.
        WebApplication scoped = new WebApplication();
        scoped.getContainer().register(Counter.class, new HttpRequestScope());
        scoped.getRouter().get("/count", Counter.class, "count");
        Counter.instances = 0;

        scoped.createPipeline().handle(FakeHttpRequest.get("/count"));
        scoped.createPipeline().handle(FakeHttpRequest.get("/count"));

        assertEquals(2, Counter.instances);
    }

    public static class Counter {
        static int instances;

        public Counter() {
            instances++;
        }

        public String count(HttpRequest request) {
            return String.valueOf(instances);
        }
    }
}
