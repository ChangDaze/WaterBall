package webframework.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import webframework.http.MediaType;

/**
 * {@code application/json}, backed by Gson.
 */
public final class JsonBodySerializer implements BodySerializer {

    private final Gson gson;

    public JsonBodySerializer() {
        this(new GsonBuilder().create());
    }

    public JsonBodySerializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.APPLICATION_JSON;
    }

    @Override
    public String serialize(Object value) {
        return gson.toJson(value);
    }

    @Override
    public <T> T deserialize(String body, Class<T> type) {
        // An absent body is read as an empty object rather than as null. That way a DTO whose fields
        // are all missing still gets to run its own validation and report its own 400, instead of
        // handing the handler a null and turning a bad request into a 500.
        String json = body == null || body.isBlank() ? "{}" : body;
        return gson.fromJson(json, type);
    }
}
