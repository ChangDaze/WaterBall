package webframework.routing;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A compiled path template such as {@code /api/users/{userId}}.
 */
public final class PathPattern {

    private final String template;
    private final List<PathSegment> segments;

    private PathPattern(String template, List<PathSegment> segments) {
        this.template = template;
        this.segments = segments;
    }

    public static PathPattern parse(String template) {
        if (template == null || !template.startsWith("/")) {
            throw new IllegalArgumentException("Path template must start with '/': " + template);
        }
        List<PathSegment> segments = new ArrayList<>();
        for (String raw : split(template)) {
            segments.add(PathSegment.parse(raw));
        }
        return new PathPattern(template, segments);
    }

    /**
     * Matches a concrete request path, collecting variable bindings as it goes.
     *
     * <p>A trailing slash is not significant: {@code /api/users} and {@code /api/users/} both match
     * the same template.
     */
    public PathMatchResult match(String path) {
        if (path == null) {
            return PathMatchResult.noMatch();
        }
        List<String> actual = split(path);
        if (actual.size() != segments.size()) {
            return PathMatchResult.noMatch();
        }
        Map<String, String> pathVariables = new LinkedHashMap<>();
        for (int i = 0; i < segments.size(); i++) {
            PathSegment segment = segments.get(i);
            String value = actual.get(i);
            if (!segment.matches(value)) {
                return PathMatchResult.noMatch();
            }
            if (segment.isVariable()) {
                pathVariables.put(segment.getText(), value);
            }
        }
        return PathMatchResult.matched(pathVariables);
    }

    /**
     * Splits on '/', dropping the empty pieces produced by the leading and any trailing slash. The
     * root path therefore has zero segments.
     */
    private static List<String> split(String path) {
        List<String> segments = new ArrayList<>();
        for (String piece : path.split("/")) {
            if (!piece.isEmpty()) {
                segments.add(piece);
            }
        }
        return segments;
    }

    public List<PathSegment> getSegments() {
        return List.copyOf(segments);
    }

    @Override
    public String toString() {
        return template;
    }
}
