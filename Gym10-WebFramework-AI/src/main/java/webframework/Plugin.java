package webframework;

/**
 * An extension installed at boot: {@code app.addPlugin(new SomeMediaTypePlugin())}.
 *
 * <p>A plugin is handed the whole application and registers its own contributions — serializers,
 * exception rules, lifecycles — into the registries it cares about. This is the seam that makes
 * BUILD_SPEC E work without core modification, and the reason nothing in the framework names a
 * single one of the media types an application might go on to support.
 */
public interface Plugin {

    void install(WebApplication app);
}
