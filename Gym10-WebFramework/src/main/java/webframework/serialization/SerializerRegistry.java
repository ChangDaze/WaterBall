package webframework.serialization;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import webframework.http.MediaType;

/**
 * The set of media types this application can read and write.
 */
public final class SerializerRegistry {

    private final Map<MediaType, BodySerializer> serializers = new LinkedHashMap<>();

    /**
     * Registering the same media type twice replaces the previous serializer, which lets a plugin
     * deliberately override a built-in one.
     */
    public SerializerRegistry register(BodySerializer serializer) {
        serializers.put(serializer.getMediaType(), serializer);
        return this;
    }

    public Optional<BodySerializer> findByMediaType(MediaType mediaType) {
        return Optional.ofNullable(serializers.get(mediaType));
    }

    /**
     * @throws UnsupportedMediaTypeException when nothing handles {@code mediaType}; unmapped, so it
     *         reaches the default rule and becomes a 500 (BUILD_SPEC C)
     */
    public BodySerializer requireByMediaType(MediaType mediaType) {
        return findByMediaType(mediaType)
                .orElseThrow(() -> new UnsupportedMediaTypeException(
                        "Unsupported media type \"" + mediaType + "\""));
    }
}
