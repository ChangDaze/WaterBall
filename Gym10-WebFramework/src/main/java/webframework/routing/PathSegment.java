package webframework.routing;

/**
 * One slash-delimited piece of a path template: either literal text, or a {@code {variable}}.
 */
public final class PathSegment {

    private final String text;
    private final boolean variable;

    private PathSegment(String text, boolean variable) {
        this.text = text;
        this.variable = variable;
    }

    /**
     * @param raw a single segment of a template, e.g. {@code users} or {@code {userId}}
     */
    public static PathSegment parse(String raw) {
        if (raw.length() > 2 && raw.startsWith("{") && raw.endsWith("}")) {
            String name = raw.substring(1, raw.length() - 1).trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Path variable must have a name: \"" + raw + "\"");
            }
            return new PathSegment(name, true);
        }
        return new PathSegment(raw, false);
    }

    /**
     * @return the literal text, or — for a variable segment — the variable's name without braces
     */
    public String getText() {
        return text;
    }

    public boolean isVariable() {
        return variable;
    }

    public boolean matches(String actual) {
        return variable || text.equals(actual);
    }

    @Override
    public String toString() {
        return variable ? "{" + text + "}" : text;
    }
}
