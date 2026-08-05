package app;

import app.controller.HelloController;
import app.controller.UserController;
import app.diagnostics.RequestId;
import app.diagnostics.ScopeDiagnosticsController;
import app.exception.DuplicateEmailException;
import app.exception.ForbiddenException;
import app.exception.InvalidCredentialsException;
import app.exception.InvalidFormatException;
import app.exception.UnauthenticatedException;
import app.repository.UserRepository;
import app.service.TokenService;
import app.service.UserService;
import webframework.WebApplication;
import webframework.http.HttpStatus;
import webframework.http.MediaType;
import webframework.ioc.Container;
import webframework.ioc.HttpRequestScope;
import webframework.ioc.PrototypeScope;
import webframework.plugin.xml.XmlMediaTypePlugin;
import webframework.routing.Router;

/**
 * The whole member system, assembled.
 *
 * <p>Separate from {@link Main} so the end-to-end tests boot the same wiring the {@code main} method
 * does. A test that assembles its own application is a test that can pass while the real boot script
 * is broken.
 */
public final class MemberSystemApplication {

    private MemberSystemApplication() {
    }

    public static WebApplication create() {
        WebApplication app = new WebApplication();

        // ---- extensions -----------------------------------------------------------------
        // Adds application/xml in both directions. The framework has no compile-time knowledge of
        // XML; installing this is the whole of what makes the XML routes below work.
        app.addPlugin(new XmlMediaTypePlugin());

        // ---- what the application's failures mean over HTTP ------------------------------
        // Registered from outside the framework, which is BUILD_SPEC E's second requirement.
        // Anything not listed here is an unmapped failure and becomes a 500.
        app.mapException(InvalidFormatException.class, HttpStatus.BAD_REQUEST)
                .mapException(DuplicateEmailException.class, HttpStatus.BAD_REQUEST)
                .mapException(InvalidCredentialsException.class, HttpStatus.BAD_REQUEST)
                .mapException(UnauthenticatedException.class, HttpStatus.UNAUTHORIZED)
                .mapException(ForbiddenException.class, HttpStatus.FORBIDDEN);

        // ---- dependencies ---------------------------------------------------------------
        Container container = app.getContainer();
        // Singletons, because they are the store: the members and the issued tokens have to outlive
        // the request that created them.
        container.register(UserRepository.class);
        container.register(TokenService.class);
        // A prototype, per BUILD_SPEC F's example. Worth being clear-eyed about what that buys here:
        // UserController is a singleton, so it is handed one UserService when it is first built. The
        // prototype matters for whoever asks the container repeatedly — see ScopeDiagnosticsController.
        container.register(UserService.class, new PrototypeScope());
        container.register(UserController.class);
        container.register(HelloController.class);
        // Request-scoped, and therefore only resolvable while a request is open.
        container.register(RequestId.class, new HttpRequestScope());
        container.register(ScopeDiagnosticsController.class, new HttpRequestScope());

        // ---- routes ---------------------------------------------------------------------
        Router router = app.getRouter();
        router.post("/api/users", UserController.class, "register")
                .respondsWith(HttpStatus.CREATED);
        router.post("/api/users/login", UserController.class, "login");
        // No response media type at all: this route's success is a 204, and a 204 carries no
        // content-type header (BUILD_SPEC 2.3 case 3).
        router.patch("/api/users/{userId}", UserController.class, "rename")
                .producesNothing();
        router.get("/api/users", UserController.class, "queryUsers");

        // The same handler, serialized as XML — the response media type is the route's business, not
        // the handler's. Registered after /api/users/{userId} and still reached, because a path match
        // with the wrong method does not stop the search.
        router.get("/api/users/xml", UserController.class, "queryUsers")
                .produces(XmlMediaTypePlugin.MEDIA_TYPE);

        router.get("/api/diagnostics/scopes", ScopeDiagnosticsController.class, "describe");

        router.get("/hello", HelloController.class, "hello").produces(MediaType.TEXT_PLAIN);
        router.post("/hello", HelloController.class, "hello").produces(MediaType.TEXT_PLAIN);

        return app;
    }
}
