package webframework.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import webframework.exceptions.DefaultExceptionRule;
import webframework.exceptions.ExceptionRule;
import webframework.exceptions.ExceptionRuleRegistry;
import webframework.http.HttpStatus;
import webframework.http.MediaType;

class SerializerRegistryTest {

    private final SerializerRegistry registry = new SerializerRegistry();

    @Test
    @DisplayName("a registered serializer is found by its media type")
    void findsByMediaType() {
        BodySerializer json = new JsonBodySerializer();
        registry.register(json);

        assertSame(json, registry.requireByMediaType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("parameters on a content-type header do not defeat the lookup")
    void mediaTypeParametersAreIgnored() {
        registry.register(new JsonBodySerializer());

        assertEquals(MediaType.APPLICATION_JSON, MediaType.parse("application/json; charset=utf-8"));
        registry.requireByMediaType(MediaType.parse("application/json; charset=utf-8"));
    }

    @Test
    @DisplayName("an unknown media type raises an exception that no built-in rule claims")
    void unknownMediaTypeTakesThe500Path() {
        registry.register(new PlainTextBodySerializer());
        MediaType unknown = MediaType.parse("application/yaml");

        assertFalse(registry.findByMediaType(unknown).isPresent());
        UnsupportedMediaTypeException failure = assertThrows(UnsupportedMediaTypeException.class,
                () -> registry.requireByMediaType(unknown));

        // BUILD_SPEC C: unsupported request content-type is a 500, which is what "unmapped" means —
        // only the always-matching default rule picks it up.
        ExceptionRule rule = new ExceptionRuleRegistry().resolve(failure);
        assertEquals(DefaultExceptionRule.class, rule.getClass());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, rule.getStatus(failure));
    }

    @Test
    @DisplayName("registering the same media type twice lets a plugin override a built-in")
    void reregistrationOverrides() {
        registry.register(new JsonBodySerializer());
        BodySerializer replacement = new JsonBodySerializer();
        registry.register(replacement);

        assertSame(replacement, registry.requireByMediaType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("a blank JSON body deserializes to an empty object, not to null")
    void blankJsonBodyBecomesAnEmptyObject() {
        // So a DTO can still run its own validation and report its own 400 (BUILD_SPEC 2.4) instead
        // of handing the handler a null and turning a bad request into a 500.
        Holder holder = new JsonBodySerializer().deserialize("   ", Holder.class);

        assertEquals(null, holder.value);
    }

    static final class Holder {
        String value;
    }
}
