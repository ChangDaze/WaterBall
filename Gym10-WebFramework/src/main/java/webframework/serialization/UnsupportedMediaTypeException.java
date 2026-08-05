package webframework.serialization;

/**
 * No serializer is registered for the media type in play.
 *
 * <p>Intentionally left out of the built-in exception rules: it falls through to the default rule
 * and becomes a 500, which is what BUILD_SPEC C asks for.
 */
public class UnsupportedMediaTypeException extends RuntimeException {

    public UnsupportedMediaTypeException(String message) {
        super(message);
    }
}
