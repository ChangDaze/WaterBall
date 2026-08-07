package app.exception;

/**
 * The email is already registered to another member. Mapped to 400 in {@code Main}.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }
}
