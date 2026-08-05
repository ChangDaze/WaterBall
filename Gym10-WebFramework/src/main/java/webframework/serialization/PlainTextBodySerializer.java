package webframework.serialization;

import webframework.http.MediaType;

/**
 * {@code text/plain}: the body is the text itself.
 */
public final class PlainTextBodySerializer implements BodySerializer {

    @Override
    public MediaType getMediaType() {
        return MediaType.TEXT_PLAIN;
    }

    @Override
    public String serialize(Object value) {
        return String.valueOf(value);
    }

    @Override
    public <T> T deserialize(String body, Class<T> type) {
        if (!type.isAssignableFrom(String.class)) {
            throw new UnsupportedMediaTypeException(
                    "text/plain bodies can only be read as String, not " + type.getName());
        }
        return type.cast(body);
    }
}
