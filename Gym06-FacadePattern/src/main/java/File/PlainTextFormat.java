package File;

import lombok.Builder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Builder
public class PlainTextFormat{

    public List<String> read(Path path) {
        try {
            // 每行作為一個元素讀取
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Plain text read failed: " + path, e);
        }
    }
}
