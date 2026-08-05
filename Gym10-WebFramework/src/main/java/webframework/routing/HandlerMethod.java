package webframework.routing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import webframework.http.HttpRequest;

/**
 * A handler identified by its declaring type and method name — never by a bound instance.
 *
 * <p>BUILD_SPEC 2.1: had registration captured {@code userController::login}, the instance would be
 * the one built at boot and the Container would never be consulted, making every lifecycle in
 * BUILD_SPEC F unreachable for handlers. Keeping the type here is what lets the Router ask the
 * Container for an instance on every request.
 *
 * <p>A handler method takes either {@code (HttpRequest)} or no arguments, and either returns the
 * object to serialize or is {@code void} — a {@code void} handler produces a 204.
 */
public final class HandlerMethod {

    private final Class<?> handlerType;
    private final String methodName;
    private final Method method;

    public HandlerMethod(Class<?> handlerType, String methodName) {
        this.handlerType = handlerType;
        this.methodName = methodName;
        this.method = resolve(handlerType, methodName);
    }

    /**
     * Resolved at registration time so a typo in a method name fails at boot, not on the first
     * request that happens to hit that route.
     */
    private static Method resolve(Class<?> handlerType, String methodName) {
        List<Method> candidates = new ArrayList<>();
        for (Method candidate : handlerType.getMethods()) {
            if (!candidate.getName().equals(methodName) || candidate.isSynthetic()) {
                continue;
            }
            if (Modifier.isStatic(candidate.getModifiers())) {
                continue;
            }
            if (accepts(candidate)) {
                candidates.add(candidate);
            }
        }
        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("No handler method " + handlerType.getName() + "#"
                    + methodName + "(HttpRequest) or #" + methodName + "()");
        }
        if (candidates.size() > 1) {
            throw new IllegalArgumentException("Ambiguous handler method " + handlerType.getName()
                    + "#" + methodName + ": " + candidates.size() + " candidates");
        }
        return candidates.get(0);
    }

    private static boolean accepts(Method candidate) {
        Class<?>[] parameters = candidate.getParameterTypes();
        return parameters.length == 0
                || (parameters.length == 1 && parameters[0].isAssignableFrom(HttpRequest.class));
    }

    /**
     * @return whatever the handler returned, or {@code null} for a {@code void} handler
     */
    public Object invoke(Object instance, HttpRequest request) {
        try {
            return method.getParameterCount() == 0
                    ? method.invoke(instance)
                    : method.invoke(instance, request);
        } catch (InvocationTargetException e) {
            // Rethrow the handler's own exception unchanged: the exception rules match on concrete
            // type, so any wrapper here would defeat every application-specific status mapping.
            throw rethrow(e.getCause());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot invoke " + this, e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> RuntimeException rethrow(Throwable cause) throws E {
        throw (E) cause;
    }

    public Class<?> getHandlerType() {
        return handlerType;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        return handlerType.getSimpleName() + "#" + methodName;
    }
}
