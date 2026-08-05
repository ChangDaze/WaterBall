package webframework;

import webframework.exceptions.ExceptionRule;
import webframework.exceptions.ExceptionRuleRegistry;
import webframework.http.HttpRequest;
import webframework.http.HttpResponse;
import webframework.http.MediaType;
import webframework.http.RequestHandler;
import webframework.ioc.Container;
import webframework.routing.MethodNotAllowedException;
import webframework.routing.NoSuchRouteException;
import webframework.routing.Route;
import webframework.routing.Router;
import webframework.routing.RoutingResult;
import webframework.serialization.BodySerializer;
import webframework.serialization.SerializerRegistry;

/**
 * The fixed dispatch algorithm, steps 2–7 of BUILD_SPEC 3. Step 1 (adapting the vendor exchange) and
 * step 8's socket write belong to the server adapter; everything here works against the framework's
 * own {@link HttpRequest}, which is what lets the whole pipeline be tested without a socket.
 *
 * <p>Deliberately one method plus two helpers rather than a middleware chain. A chain is the intended
 * evolution point — when authentication, logging or CORS want to interpose, {@link #handle} becomes
 * the terminal link and the steps below become filters around it. Until something actually needs
 * that, the straight-line version is easier to follow and to prove correct.
 */
public final class RequestPipeline implements RequestHandler {

    private final Router router;
    private final Container container;
    private final SerializerRegistry serializers;
    private final ExceptionRuleRegistry exceptionRules;

    public RequestPipeline(Router router, Container container, SerializerRegistry serializers,
                           ExceptionRuleRegistry exceptionRules) {
        this.router = router;
        this.container = container;
        this.serializers = serializers;
        this.exceptionRules = exceptionRules;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        // Step 2 / step 8: the scope is opened here and closed by try-with-resources before the
        // catch below runs — so a request that blew up still leaves the scope empty.
        try (RequestContext context = RequestContext.open(request)) {
            return dispatch(request);
        } catch (Throwable failure) {
            return toErrorResponse(failure);
        }
    }

    private HttpResponse dispatch(HttpRequest request) {
        // Steps 3 and 4: match, and — on the winner only — write the path variables into the request.
        RoutingResult result = router.route(request);
        if (result.getStatus() == RoutingResult.Status.NO_PATH_MATCH) {
            throw new NoSuchRouteException("Cannot find the path \"" + request.getPath() + "\"");
        }
        if (result.getStatus() == RoutingResult.Status.METHOD_NOT_ALLOWED) {
            throw new MethodNotAllowedException("The method \"" + request.getMethod()
                    + "\" is not allowed on \"" + request.getPath() + "\"");
        }
        Route route = result.getRoute();

        // Step 5: ask the container, every request, so the handler's lifecycle is honoured.
        Object handler = container.get(route.getHandlerMethod().getHandlerType());

        // Step 6: invoke. Anything thrown escapes to handle()'s catch with its type intact.
        Object body = route.getHandlerMethod().invoke(handler, request);

        return serialize(route, body);
    }

    /**
     * Step 7, and with it the priority order of BUILD_SPEC 2.3: a body is serialized as the route's
     * configured media type, and no body means 204 with no {@code content-type} header at all.
     */
    private HttpResponse serialize(Route route, Object body) {
        if (body == null) {
            return HttpResponse.noContent();
        }
        MediaType responseType = route.getResponseType().orElseThrow(() -> new IllegalStateException(
                "Route " + route + " is declared to produce no body, but its handler returned "
                        + body.getClass().getName()));
        BodySerializer serializer = serializers.requireByMediaType(responseType);
        return HttpResponse.of(route.getSuccessStatus(), responseType, serializer.serialize(body));
    }

    /**
     * The exception path: status from the matched rule, body from its message, always
     * {@code text/plain} (BUILD_SPEC D and 2.3 case 2).
     *
     * <p>The text is written straight into the response rather than run through the serializer
     * registry. The registry can itself be the thing that failed, and the last stop before the wire
     * is no place to acquire a new dependency.
     */
    private HttpResponse toErrorResponse(Throwable failure) {
        ExceptionRule rule = exceptionRules.resolve(failure);
        return HttpResponse.text(rule.getStatus(failure), rule.getMessage(failure));
    }
}
