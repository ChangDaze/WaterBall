package webframework.exceptions;

import webframework.http.HttpStatus;

/**
 * The common case: "exceptions of this type mean that status, and the body is their message".
 *
 * <p>Matching is by assignability, so registering a base type covers its subclasses.
 */
public class ExceptionTypeRule implements ExceptionRule {

    private final Class<? extends Throwable> exceptionType;
    private final HttpStatus status;

    public ExceptionTypeRule(Class<? extends Throwable> exceptionType, HttpStatus status) {
        this.exceptionType = exceptionType;
        this.status = status;
    }

    @Override
    public boolean matches(Throwable throwable) {
        return exceptionType.isInstance(throwable);
    }

    @Override
    public HttpStatus getStatus(Throwable throwable) {
        return status;
    }

    @Override
    public String getMessage(Throwable throwable) {
        return throwable.getMessage() != null
                ? throwable.getMessage()
                : throwable.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return exceptionType.getSimpleName() + " -> " + status.getCode();
    }
}
