package app.exception;

/**
 * The caller is authenticated, but not as the member they are trying to act on. Mapped to 403 in
 * {@code Main}.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
