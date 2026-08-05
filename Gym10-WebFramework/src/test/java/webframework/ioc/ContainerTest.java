package webframework.ioc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import webframework.RequestContext;
import webframework.http.HttpMethod;
import webframework.testsupport.FakeHttpRequest;

class ContainerTest {

    public static class Leaf {
        static int constructionCount;

        public Leaf() {
            constructionCount++;
        }
    }

    public static class Middle {
        final Leaf leaf;

        public Middle(Leaf leaf) {
            this.leaf = leaf;
        }
    }

    public static class Top {
        final Middle middle;
        final Leaf leaf;

        public Top(Middle middle, Leaf leaf) {
            this.middle = middle;
            this.leaf = leaf;
        }
    }

    public static class Explodes {
        public Explodes() {
            throw new IllegalStateException("cannot be built");
        }
    }

    public static class Ping {
        public Ping(Pong pong) {
        }
    }

    public static class Pong {
        public Pong(Ping ping) {
        }
    }

    private Container container;

    @BeforeEach
    void setUp() {
        Leaf.constructionCount = 0;
        container = new Container();
        RequestScopeStorage.close();
    }

    @Test
    @DisplayName("nothing is constructed by register(); the first get() builds it, once")
    void singletonIsLazyAndBuiltOnce() {
        container.register(Leaf.class);
        assertEquals(0, Leaf.constructionCount, "register() must not construct anything");

        Leaf first = container.get(Leaf.class);
        Leaf second = container.get(Leaf.class);

        assertEquals(1, Leaf.constructionCount);
        assertSame(first, second);
    }

    @Test
    @DisplayName("a prototype yields a different instance on every get()")
    void prototypeYieldsDistinctInstances() {
        container.register(Leaf.class, new PrototypeScope());

        assertNotSame(container.get(Leaf.class), container.get(Leaf.class));
        assertEquals(2, Leaf.constructionCount);
    }

    @Test
    @DisplayName("a request-scoped type is one instance within a request and a new one in the next")
    void requestScopeIsPerRequest() {
        container.register(Leaf.class, new HttpRequestScope());

        Leaf firstRequestFirstGet;
        Leaf firstRequestSecondGet;
        try (RequestContext ignored = RequestContext.open(FakeHttpRequest.get("/one"))) {
            firstRequestFirstGet = container.get(Leaf.class);
            firstRequestSecondGet = container.get(Leaf.class);
        }
        Leaf secondRequest;
        try (RequestContext ignored = RequestContext.open(FakeHttpRequest.get("/two"))) {
            secondRequest = container.get(Leaf.class);
        }

        assertSame(firstRequestFirstGet, firstRequestSecondGet, "same instance within one request");
        assertNotSame(firstRequestFirstGet, secondRequest, "a new instance for the next request");
        assertEquals(2, Leaf.constructionCount);
    }

    @Test
    @DisplayName("the request scope is empty afterwards even when the request threw")
    void requestScopeIsCleanedUpAfterAFailure() {
        // BUILD_SPEC 2.6: closing only on the happy path is how request-scoped instances leak into
        // the next request.
        container.register(Leaf.class, new HttpRequestScope());

        assertThrows(IllegalStateException.class, () -> {
            try (RequestContext ignored = RequestContext.open(FakeHttpRequest.get("/boom"))) {
                container.get(Leaf.class);
                assertEquals(1, RequestScopeStorage.size());
                throw new IllegalStateException("handler blew up");
            }
        });

        assertFalse(RequestScopeStorage.isOpen());
        assertEquals(0, RequestScopeStorage.size());
    }

    @Test
    @DisplayName("resolving a request-scoped type outside a request is an error, not a silent leak")
    void requestScopedTypeNeedsAnOpenRequest() {
        container.register(Leaf.class, new HttpRequestScope());

        IllegalStateException failure =
                assertThrows(IllegalStateException.class, () -> container.get(Leaf.class));
        assertTrue(failure.getMessage().contains("request scope"));
    }

    @Test
    @DisplayName("constructor injection resolves recursively, honouring each dependency's lifecycle")
    void recursiveConstructorInjection() {
        container.register(Top.class);
        container.register(Middle.class);
        container.register(Leaf.class);

        Top top = container.get(Top.class);

        assertSame(top.leaf, top.middle.leaf, "the singleton Leaf is shared down the whole graph");
        assertEquals(1, Leaf.constructionCount);
    }

    @Test
    @DisplayName("a prototype deep in the graph is rebuilt for each dependent that asks")
    void recursiveInjectionRespectsPrototypes() {
        container.register(Top.class);
        container.register(Middle.class);
        container.register(Leaf.class, new PrototypeScope());

        Top top = container.get(Top.class);

        assertNotSame(top.leaf, top.middle.leaf);
        assertEquals(2, Leaf.constructionCount);
    }

    @Test
    @DisplayName("registrations are reachable by name as well as by type")
    void lookupByName() {
        container.register(Leaf.class);

        assertSame(container.get(Leaf.class), container.get("Leaf"));
    }

    @Test
    @DisplayName("a new lifecycle works without a line changing in Container")
    void customLifecycleNeedsNoCoreChange() {
        // BUILD_SPEC F's "new lifecycles must be addable without modifying Container", demonstrated
        // from outside the framework: this scope hands out the same instance twice, then a new one.
        class EverySecondGetScope implements Lifecycle {
            private Object cached;
            private int calls;

            @Override
            public Object get(String name, Supplier<Object> provider) {
                if (calls++ % 2 == 0) {
                    cached = provider.get();
                }
                return cached;
            }
        }
        container.register(Leaf.class, new EverySecondGetScope());

        Leaf first = container.get(Leaf.class);
        Leaf second = container.get(Leaf.class);
        Leaf third = container.get(Leaf.class);

        assertSame(first, second);
        assertNotSame(second, third);
        assertEquals(2, Leaf.constructionCount);
    }

    @Test
    @DisplayName("an unregistered type is reported, both directly and as a dependency")
    void unregisteredTypesAreReported() {
        assertThrows(ContainerException.class, () -> container.get(Leaf.class));

        container.register(Middle.class);
        ContainerException failure =
                assertThrows(ContainerException.class, () -> container.get(Middle.class));
        assertTrue(failure.getMessage().contains(Leaf.class.getName()));
    }

    @Test
    @DisplayName("a dependency cycle is named rather than overflowing the stack")
    void cyclesAreDetected() {
        container.register(Ping.class);
        container.register(Pong.class);

        ContainerException failure =
                assertThrows(ContainerException.class, () -> container.get(Ping.class));
        assertTrue(failure.getMessage().contains("Dependency cycle"), failure.getMessage());
    }

    @Test
    @DisplayName("a throwing constructor surfaces its own cause")
    void constructorFailuresKeepTheirCause() {
        container.register(Explodes.class);

        ContainerException failure =
                assertThrows(ContainerException.class, () -> container.get(Explodes.class));
        assertEquals("cannot be built", failure.getCause().getMessage());
    }

    @Test
    @DisplayName("registering the same type or name twice is refused")
    void duplicateRegistrationsAreRefused() {
        container.register(Leaf.class);

        assertThrows(ContainerException.class, () -> container.register(Leaf.class));
        assertThrows(ContainerException.class,
                () -> container.register("Leaf", Middle.class, new PrototypeScope()));
    }

    @Test
    @DisplayName("two requests on different threads do not see each other's scoped instances")
    void requestScopeIsPerThread() throws Exception {
        container.register(Leaf.class, new HttpRequestScope());
        Leaf[] fromOtherThread = new Leaf[1];

        try (RequestContext ignored = RequestContext.open(
                new FakeHttpRequest(HttpMethod.GET, "/one"))) {
            Leaf mine = container.get(Leaf.class);

            Thread other = new Thread(() -> {
                try (RequestContext alsoIgnored = RequestContext.open(
                        new FakeHttpRequest(HttpMethod.GET, "/two"))) {
                    fromOtherThread[0] = container.get(Leaf.class);
                }
            });
            other.start();
            other.join();

            assertNotSame(mine, fromOtherThread[0]);
        }
    }
}
