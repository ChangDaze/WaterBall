package webframework.serialization;

import webframework.http.MediaType;

/**
 * Strategy: turns objects into a wire body and back for one media type.
 *
 * <p>Adding a media type means adding an implementation and registering it — never editing the
 * framework (BUILD_SPEC E), and a plugin is the usual way that registration arrives.
 */
public interface BodySerializer {

    MediaType getMediaType();

    String serialize(Object value);

    /**
     * <p>BUILD_SPEC 2.4: implementations must not wrap exceptions thrown by {@code type} itself —
     * the exception resolver matches on concrete type, so wrapping a DTO's validation failure would
     * turn a 400 into a 500.
     */
    <T> T deserialize(String body, Class<T> type);
}
