package app.exception;

/**
 * Unknown email, or the wrong password for a known one. Mapped to 400 in {@code Main}.
 *
 * <p>Deliberately one exception for both cases: telling a caller which half was wrong tells them
 * which emails are registered.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
