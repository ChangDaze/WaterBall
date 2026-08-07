package v5.app;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import v5.app.Main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestcaseHarnessTest {
    static final Path TESTCASES = Path.of("testcases");

    @TestFactory
    Stream<DynamicTest> goldenFiles() throws IOException {
        if (!Files.isDirectory(TESTCASES)) {
            return Stream.empty();
        }
        return Files.walk(TESTCASES)
                .filter(p -> p.toString().endsWith(".in"))
                .sorted()
                .map(in -> DynamicTest.dynamicTest(TESTCASES.relativize(in).toString(),
                        () -> runGoldenFile(in)));
    }

    private void runGoldenFile(Path in) throws IOException {
        Path out = in.resolveSibling(in.getFileName().toString().replaceAll("\\.in$", ".out"));
        String expected = Files.readString(out, StandardCharsets.UTF_8)
                .replace("\r\n", "\n").stripTrailing();

        List<String> produced = new ArrayList<>();
        try (Stream<String> lines = Files.lines(in, StandardCharsets.UTF_8)) {
            Main.run(lines, produced::add);
        }
        assertEquals(expected, String.join("\n", produced));
    }
}
