package app.exception;

/**
 * A request body failed its own validation. Mapped to 400 in {@code Main}.
 *
 * <p>BUILD_SPEC 2.4 is about this exception in particular: it is raised from inside
 * {@code readBodyAsObject}, and it has to arrive at the exception resolver with its own type intact.
 * If the framework wrapped it, every 400 in this application would be reported as a 500.
 */
public class InvalidFormatException extends RuntimeException {

    public InvalidFormatException(String message) {
        super(message);
    }
}
