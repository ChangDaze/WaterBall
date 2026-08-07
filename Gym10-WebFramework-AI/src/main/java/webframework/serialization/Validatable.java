package webframework.serialization;

/**
 * Opt-in hook for request bodies that police their own contents.
 *
 * <p>A deserializer builds a DTO by filling in fields, so a constructor guard would never run. This
 * interface gives the DTO a place to say no: {@code readBodyAsObject} calls {@link #validate()}
 * right after deserializing and lets whatever it throws propagate untouched, so an application
 * exception mapped to 400 stays a 400 instead of collapsing into a generic 500 (BUILD_SPEC 2.4).
 */
public interface Validatable {

    /**
     * @throws RuntimeException — of the application's own type — if the contents are unacceptable
     */
    void validate();
}
