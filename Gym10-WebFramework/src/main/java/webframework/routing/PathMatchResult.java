package webframework.routing;

import java.util.Collections;
import java.util.Map;

/**
 * The outcome of testing one path against one template.
 *
 * <p>BUILD_SPEC 2.2: this exists instead of a boolean because matching {@code /api/users/{userId}}
 * against {@code /api/users/3} already computes {@code userId=3}. Returning a boolean would throw
 * that away and force a second pass to recover it.
 */
public final class PathMatchResult {

    private static final PathMatchResult NO_MATCH = new PathMatchResult(false, Map.of());

    private final boolean matched;
    private final Map<String, String> pathVariables;

    private PathMatchResult(boolean matched, Map<String, String> pathVariables) {
        this.matched = matched;
        this.pathVariables = pathVariables;
    }

    public static PathMatchResult noMatch() {
        return NO_MATCH;
    }

    public static PathMatchResult matched(Map<String, String> pathVariables) {
        return new PathMatchResult(true, Collections.unmodifiableMap(pathVariables));
    }

    public boolean isMatched() {
        return matched;
    }

    /**
     * @return the bindings extracted while matching; empty for a template without variables
     */
    public Map<String, String> getPathVariables() {
        return pathVariables;
    }
}
