package webframework;

import java.util.ArrayList;
import java.util.List;

import webframework.exceptions.ExceptionRule;
import webframework.exceptions.ExceptionRuleRegistry;
import webframework.http.HttpStatus;
import webframework.ioc.Container;
import webframework.routing.MethodNotAllowedException;
import webframework.routing.NoSuchRouteException;
import webframework.routing.Router;
import webframework.serialization.BodySerializer;
import webframework.serialization.JsonBodySerializer;
import webframework.serialization.PlainTextBodySerializer;
import webframework.serialization.SerializerRegistry;
import webframework.server.JdkHttpServerAdapter;

/**
 * The boot entry point, and the object every extension point hangs off.
 *
 * <pre>{@code
 * WebApplication app = new WebApplication();
 * app.addPlugin(new SomeMediaTypePlugin());
 * app.mapException(InvalidCredentialsException.class, HttpStatus.BAD_REQUEST);
 * app.getContainer().register(UserController.class);
 * app.getRouter().post("/api/users/login", UserController.class, "login");
 * app.start(8080);
 * }</pre>
 *
 * <p>Note what the constructor installs and what it does not: JSON and plain text are built in
 * (BUILD_SPEC C), as are the two routing-miss rules, and that is all. Every other media type,
 * exception mapping and lifecycle arrives from outside through the methods below — which is
 * BUILD_SPEC E's "no core modification" stated as code.
 */
public final class WebApplication {

    private final Container container = new Container();
    private final Router router = new Router();
    private final SerializerRegistry serializers = new SerializerRegistry();
    private final ExceptionRuleRegistry exceptionRules = new ExceptionRuleRegistry();
    private final List<Plugin> plugins = new ArrayList<>();

    private JdkHttpServerAdapter server;

    public WebApplication() {
        serializers.register(new JsonBodySerializer())
                .register(new PlainTextBodySerializer());
        exceptionRules.register(NoSuchRouteException.class, HttpStatus.NOT_FOUND)
                .register(MethodNotAllowedException.class, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Installs a plugin immediately, so ordering in the boot script is the ordering that applies.
     */
    public WebApplication addPlugin(Plugin plugin) {
        plugins.add(plugin);
        plugin.install(this);
        return this;
    }

    public WebApplication addSerializer(BodySerializer serializer) {
        serializers.register(serializer);
        return this;
    }

    /**
     * Maps an exception type to a status: {@code app.mapException(ForbiddenException.class, FORBIDDEN)}.
     */
    public WebApplication mapException(Class<? extends Throwable> exceptionType, HttpStatus status) {
        exceptionRules.register(exceptionType, status);
        return this;
    }

    public WebApplication addExceptionRule(ExceptionRule rule) {
        exceptionRules.register(rule);
        return this;
    }

    /**
     * Builds a pipeline over the current registries. Useful on its own: a test can drive the whole
     * dispatch algorithm through this without binding a port.
     */
    public RequestPipeline createPipeline() {
        return new RequestPipeline(router, container, serializers, exceptionRules);
    }

    /**
     * @param port the port to bind, or 0 to let the OS pick one — then read it back with
     *             {@link #getPort()}
     */
    public void start(int port) {
        server = new JdkHttpServerAdapter(createPipeline(), serializers);
        server.start(port);
    }

    public void stop() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    public int getPort() {
        if (server == null) {
            throw new IllegalStateException("Application is not started");
        }
        return server.getPort();
    }

    public Container getContainer() {
        return container;
    }

    public Router getRouter() {
        return router;
    }

    public SerializerRegistry getSerializerRegistry() {
        return serializers;
    }

    public ExceptionRuleRegistry getExceptionRuleRegistry() {
        return exceptionRules;
    }

    public List<Plugin> getPlugins() {
        return List.copyOf(plugins);
    }
}
