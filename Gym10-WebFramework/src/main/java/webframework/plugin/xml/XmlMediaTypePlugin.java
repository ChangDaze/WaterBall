package webframework.plugin.xml;

import webframework.Plugin;
import webframework.WebApplication;
import webframework.http.MediaType;

/**
 * Teaches an application to speak {@code application/xml}.
 *
 * <p>This is BUILD_SPEC E's proof: the framework contains no reference to any class in this package,
 * yet installing the plugin adds a media type to both directions of the wire —
 *
 * <pre>{@code
 * app.addPlugin(new XmlMediaTypePlugin());
 * router.get("/api/users/xml", UserController.class, "queryUsers")
 *       .produces(XmlMediaTypePlugin.MEDIA_TYPE);
 * }</pre>
 *
 * <p>The media-type constant lives here rather than on {@link MediaType} for the same reason: a
 * built-in constant would be the framework knowing about XML.
 */
public final class XmlMediaTypePlugin implements Plugin {

    public static final MediaType MEDIA_TYPE = new MediaType("application", "xml");

    @Override
    public void install(WebApplication app) {
        app.addSerializer(new XmlBodySerializer());
    }
}
