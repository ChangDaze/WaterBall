import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The structural rules, as tests.
 *
 * <p>Three of BUILD_SPEC's constraints are about what the code may <em>not</em> refer to, and no
 * ordinary test can catch a violation of those — the code still compiles and still works. So they are
 * checked here by reading the sources.
 */
class ArchitectureTest {

    private static final Path SOURCE_ROOT = Path.of("src", "main", "java");
    private static final Path FRAMEWORK = SOURCE_ROOT.resolve("webframework");
    private static final Path PLUGINS = FRAMEWORK.resolve("plugin");
    private static final Path APPLICATION = SOURCE_ROOT.resolve("app");

    @Test
    @DisplayName("2.5: only the two server-adapter classes import com.sun.net.httpserver")
    void vendorApiIsConfinedToTheAdapter() throws IOException {
        List<String> importers = new ArrayList<>();
        for (Path source : javaFilesUnder(SOURCE_ROOT)) {
            if (containsLineStartingWith(source, "import com.sun.net.httpserver")) {
                importers.add(source.getFileName().toString());
            }
        }

        assertEquals(Set.of("JdkHttpRequest.java", "JdkHttpServerAdapter.java"),
                Set.copyOf(importers),
                "the vendor HTTP API must not leak past the adapter; found " + importers);
    }

    @Test
    @DisplayName("0: the framework never imports from the member system")
    void frameworkDoesNotDependOnTheApplication() throws IOException {
        List<String> offenders = new ArrayList<>();
        for (Path source : javaFilesUnder(FRAMEWORK)) {
            if (containsLineStartingWith(source, "import app.")) {
                offenders.add(source.toString());
            }
        }

        assertTrue(offenders.isEmpty(),
                "dependency direction is app -> framework -> jdk, but these point back: " + offenders);
    }

    @Test
    @DisplayName("M5: the framework holds no reference to any XML class")
    void frameworkDoesNotKnowAboutXml() throws IOException {
        List<String> offenders = new ArrayList<>();
        for (Path source : javaFilesUnder(FRAMEWORK)) {
            if (source.startsWith(PLUGINS)) {
                continue;
            }
            String text = Files.readString(source);
            if (text.contains("Xml") || text.contains("XML")) {
                offenders.add(source.toString());
            }
        }

        assertTrue(offenders.isEmpty(),
                "XML arrives only through a plugin, so the core must not name it: " + offenders);
    }

    @Test
    @DisplayName("the member system talks to the framework, never to the raw server")
    void applicationDoesNotTouchTheVendorApi() throws IOException {
        List<String> offenders = new ArrayList<>();
        for (Path source : javaFilesUnder(APPLICATION)) {
            if (containsLineStartingWith(source, "import com.sun.net.httpserver")) {
                offenders.add(source.toString());
            }
        }

        assertTrue(offenders.isEmpty(), "application code must not see the vendor API: " + offenders);
    }

    @Test
    @DisplayName("no web framework or DI library has crept onto the classpath")
    void noForbiddenLibrariesAreImported() throws IOException {
        List<String> forbidden = List.of(
                "import org.springframework", "import jakarta.servlet", "import javax.servlet",
                "import jakarta.inject", "import javax.inject", "import jakarta.ws.rs",
                "import io.micronaut", "import org.glassfish.jersey");
        List<String> offenders = new ArrayList<>();
        for (Path source : javaFilesUnder(SOURCE_ROOT)) {
            for (String prefix : forbidden) {
                if (containsLineStartingWith(source, prefix)) {
                    offenders.add(source + " -> " + prefix);
                }
            }
        }

        assertTrue(offenders.isEmpty(), "BUILD_SPEC 0 forbids these: " + offenders);
    }

    private static List<Path> javaFilesUnder(Path root) throws IOException {
        assertTrue(Files.isDirectory(root),
                root.toAbsolutePath() + " is missing; run this test from the project root");
        try (Stream<Path> files = Files.walk(root)) {
            return files.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    /**
     * Matches import statements only, so prose in a comment that happens to name a package is not
     * mistaken for a dependency.
     */
    private static boolean containsLineStartingWith(Path source, String prefix) throws IOException {
        try (Stream<String> lines = Files.lines(source)) {
            return lines.anyMatch(line -> line.stripLeading().startsWith(prefix));
        }
    }
}
