package webframework.exceptions;

import webframework.http.HttpStatus;

/**
 * The always-matching fallback: anything unmapped is a 500 carrying the exception's message
 * (BUILD_SPEC D).
 *
 * <p>Held apart from the registered rules rather than appended to them, so a rule registered later
 * can never end up shadowed behind the catch-all.
 */
public class DefaultExceptionRule implements ExceptionRule {

    @Override
    public boolean matches(Throwable throwable) {
        return true;
    }

    @Override
    public HttpStatus getStatus(Throwable throwable) {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getMessage(Throwable throwable) {
        // Some exceptions carry no message (NullPointerException, say); the class name is more use
        // to whoever is reading the response than the literal text "null".
        return throwable.getMessage() != null
                ? throwable.getMessage()
                : throwable.getClass().getName();
    }
}
