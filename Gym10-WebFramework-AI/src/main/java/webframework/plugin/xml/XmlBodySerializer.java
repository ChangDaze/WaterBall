package webframework.plugin.xml;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import webframework.http.MediaType;
import webframework.serialization.BodySerializer;

/**
 * A small reflective {@code application/xml} serializer, built on the JDK's own DOM parser.
 *
 * <p>Field names become element names; a collection becomes a {@code <list>} of elements named after
 * their item type. Deliberately modest — the point of this class is that a media type can be added
 * from outside the framework, not that it is a complete XML data binder. Its limits:
 *
 * <ul>
 *   <li>reading supports scalars and nested objects, but not collections;</li>
 *   <li>attributes, namespaces and mixed content are not modelled;</li>
 *   <li>a type being read needs a no-argument constructor.</li>
 * </ul>
 */
public final class XmlBodySerializer implements BodySerializer {

    @Override
    public MediaType getMediaType() {
        return XmlMediaTypePlugin.MEDIA_TYPE;
    }

    @Override
    public String serialize(Object value) {
        StringBuilder out = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writeElement(out, rootNameOf(value), value);
        return out.toString();
    }

    @Override
    public <T> T deserialize(String body, Class<T> type) {
        // A blank body yields an all-null instance rather than a null: the DTO then gets to run its
        // own validation and report its own 400, mirroring the JSON serializer.
        if (body == null || body.isBlank()) {
            return type.cast(newInstance(type));
        }
        Element root = parse(body).getDocumentElement();
        return type.cast(bind(root, type));
    }

    // ---------------------------------------------------------------- writing

    private void writeElement(StringBuilder out, String name, Object value) {
        if (value == null) {
            out.append('<').append(name).append("/>");
            return;
        }
        out.append('<').append(name).append('>');
        if (isScalar(value.getClass())) {
            out.append(escape(String.valueOf(value)));
        } else if (value instanceof Collection<?> items) {
            for (Object item : items) {
                writeElement(out, rootNameOf(item), item);
            }
        } else {
            for (Field field : fieldsOf(value.getClass())) {
                writeElement(out, field.getName(), read(field, value));
            }
        }
        out.append("</").append(name).append('>');
    }

    private static String rootNameOf(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Collection<?>) {
            return "list";
        }
        String simpleName = value.getClass().getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    private static String escape(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    // ---------------------------------------------------------------- reading

    private Object bind(Element element, Class<?> type) {
        Object instance = newInstance(type);
        for (Field field : fieldsOf(type)) {
            Element child = firstChildElement(element, field.getName());
            if (child == null) {
                continue;
            }
            write(field, instance, convert(child, field.getType()));
        }
        return instance;
    }

    private Object convert(Element element, Class<?> targetType) {
        if (Collection.class.isAssignableFrom(targetType)) {
            throw new IllegalArgumentException(
                    "Reading collections from XML is not supported (<" + element.getTagName() + ">)");
        }
        if (isScalar(targetType)) {
            return toScalar(textOf(element), targetType);
        }
        return bind(element, targetType);
    }

    private static Object toScalar(String text, Class<?> targetType) {
        if (targetType == String.class) {
            return text;
        }
        if (text.isBlank()) {
            return null;
        }
        String trimmed = text.trim();
        if (targetType == int.class || targetType == Integer.class) {
            return Integer.valueOf(trimmed);
        }
        if (targetType == long.class || targetType == Long.class) {
            return Long.valueOf(trimmed);
        }
        if (targetType == double.class || targetType == Double.class) {
            return Double.valueOf(trimmed);
        }
        if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.valueOf(trimmed);
        }
        throw new IllegalArgumentException("Cannot read " + targetType.getName() + " from XML text");
    }

    private static Document parse(String body) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // No external entities: an XML body is untrusted input.
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse XML body: " + e.getMessage(), e);
        }
    }

    private static Element firstChildElement(Element parent, String name) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element element && element.getTagName().equals(name)) {
                return element;
            }
        }
        return null;
    }

    private static String textOf(Element element) {
        return element.getTextContent() == null ? "" : element.getTextContent();
    }

    // ---------------------------------------------------------------- reflection helpers

    private static boolean isScalar(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || Number.class.isAssignableFrom(type)
                || type == Boolean.class
                || type == Character.class
                || type.isEnum();
    }

    private static List<Field> fieldsOf(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> current = type; current != null && current != Object.class;
                current = current.getSuperclass()) {
            for (Field field : current.getDeclaredFields()) {
                if (!field.isSynthetic() && !Modifier.isStatic(field.getModifiers())) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    private static Object newInstance(Class<?> type) {
        try {
            var constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(
                    type.getName() + " needs a no-argument constructor to be read from XML", e);
        }
    }

    private static Object read(Field field, Object owner) {
        try {
            field.setAccessible(true);
            return field.get(owner);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot read field " + field, e);
        }
    }

    private static void write(Field field, Object owner, Object value) {
        try {
            field.setAccessible(true);
            field.set(owner, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot write field " + field, e);
        }
    }
}
