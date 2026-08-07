package webframework.http;

import java.util.Objects;

/**
 * Value object: a media type's {@code type/subtype} pair, e.g. {@code application/json}.
 *
 * <p>Parameters such as {@code ; charset=utf-8} are deliberately dropped: the framework selects a
 * serializer by type and subtype only, so keeping the parameters would break equality.
 *
 * <p>Only the two built-in media types have constants here. Anything further arrives through a plugin
 * (BUILD_SPEC E), and a plugin brings its own constant with it rather than adding one to this class.
 */
public final class MediaType {

    public static final MediaType APPLICATION_JSON = new MediaType("application", "json");
    public static final MediaType TEXT_PLAIN = new MediaType("text", "plain");

    private final String type;
    private final String subtype;

    public MediaType(String type, String subtype) {
        this.type = normalize(type, "type");
        this.subtype = normalize(subtype, "subtype");
    }

    /**
     * Parses a {@code content-type} header value, ignoring any parameters.
     *
     * @throws IllegalArgumentException if the value is not {@code type/subtype}
     */
    public static MediaType parse(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Media type must not be null");
        }
        String withoutParameters = raw.split(";", 2)[0].trim();
        String[] parts = withoutParameters.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Cannot parse media type \"" + raw + "\"");
        }
        return new MediaType(parts[0], parts[1]);
    }

    private static String normalize(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Media type " + field + " must not be blank");
        }
        return value.trim().toLowerCase();
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MediaType that)) {
            return false;
        }
        return type.equals(that.type) && subtype.equals(that.subtype);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, subtype);
    }

    /**
     * @return the header form, e.g. {@code application/json}
     */
    @Override
    public String toString() {
        return type + "/" + subtype;
    }
}
