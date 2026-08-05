package webframework.exceptions;

import webframework.http.HttpStatus;

/**
 * Strategy: decides whether it speaks for a thrown exception, and what response that becomes.
 *
 * <p>Applications add mappings by registering more of these — never by editing the framework
 * (BUILD_SPEC E).
 */
public interface ExceptionRule {

    boolean matches(Throwable throwable);

    HttpStatus getStatus(Throwable throwable);

    /**
     * @return the plain-text body for this failure
     */
    String getMessage(Throwable throwable);
}
