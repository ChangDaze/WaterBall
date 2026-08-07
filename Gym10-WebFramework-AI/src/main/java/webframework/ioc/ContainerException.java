package webframework.ioc;

/**
 * A wiring problem: nothing registered for a requested type, no usable constructor, a dependency
 * cycle, or a constructor that threw.
 */
public class ContainerException extends RuntimeException {

    public ContainerException(String message) {
        super(message);
    }

    public ContainerException(String message, Throwable cause) {
        super(message, cause);
    }
}
