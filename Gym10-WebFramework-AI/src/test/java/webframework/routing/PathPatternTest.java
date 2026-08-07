package webframework.routing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathPatternTest {

    @Test
    @DisplayName("a literal template matches only that exact path")
    void literalMatch() {
        PathPattern pattern = PathPattern.parse("/api/users");

        assertTrue(pattern.match("/api/users").isMatched());
        assertFalse(pattern.match("/api/user").isMatched());
        assertFalse(pattern.match("/api/users/1").isMatched());
        assertFalse(pattern.match("/api").isMatched());
    }

    @Test
    @DisplayName("a variable segment matches anything and records what it matched")
    void variableExtraction() {
        PathMatchResult result = PathPattern.parse("/api/users/{userId}").match("/api/users/3");

        assertTrue(result.isMatched());
        assertEquals(Map.of("userId", "3"), result.getPathVariables());
    }

    @Test
    @DisplayName("several variables in one template are all captured")
    void multipleVariables() {
        PathMatchResult result = PathPattern.parse("/api/users/{userId}/posts/{postId}")
                .match("/api/users/7/posts/42");

        assertEquals(Map.of("userId", "7", "postId", "42"), result.getPathVariables());
    }

    @Test
    @DisplayName("a different number of segments never matches, however similar the prefix")
    void wrongSegmentCount() {
        PathPattern pattern = PathPattern.parse("/api/users/{userId}");

        assertFalse(pattern.match("/api/users").isMatched());
        assertFalse(pattern.match("/api/users/3/posts").isMatched());
    }

    @Test
    @DisplayName("a trailing slash is not significant, on either side")
    void trailingSlash() {
        assertTrue(PathPattern.parse("/api/users").match("/api/users/").isMatched());
        assertTrue(PathPattern.parse("/api/users/").match("/api/users").isMatched());
        assertEquals(Map.of("userId", "3"),
                PathPattern.parse("/api/users/{userId}").match("/api/users/3/").getPathVariables());
    }

    @Test
    @DisplayName("the root path has no segments and matches itself")
    void rootPath() {
        assertTrue(PathPattern.parse("/").match("/").isMatched());
        assertFalse(PathPattern.parse("/").match("/hello").isMatched());
    }

    @Test
    @DisplayName("a non-matching path yields no variables, not partially collected ones")
    void noMatchCarriesNoBindings() {
        PathMatchResult result = PathPattern.parse("/api/users/{userId}/posts")
                .match("/api/users/3/comments");

        assertFalse(result.isMatched());
        assertTrue(result.getPathVariables().isEmpty());
    }

    @Test
    @DisplayName("a template must be absolute")
    void templateMustStartWithSlash() {
        assertThrows(IllegalArgumentException.class, () -> PathPattern.parse("api/users"));
    }
}
