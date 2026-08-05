package app.exception;

/**
 * No usable token on the request: missing, unparseable, or unknown. Mapped to 401 in {@code Main}.
 */
public class UnauthenticatedException extends RuntimeException {

    public UnauthenticatedException(String message) {
        super(message);
    }
}
