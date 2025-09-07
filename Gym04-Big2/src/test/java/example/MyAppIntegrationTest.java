package example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyAppIntegrationTest {
    @Test
    public void testFullInputOutputFlow() throws IOException {
        // 1. 讀取 input.txt（從 test resources）
        byte[] inputBytes = Files.readAllBytes(Path.of("src/test/resources/input.txt"));
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputBytes);
        System.setIn(testIn);

        // 2. 捕捉標準輸出
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        // 3. 執行程式（整體一次跑完）
        MyApp.run();

        // 4. 讀取 output.txt（預期輸出）
        String expectedOutput = Files.readString(Path.of("src/test/resources/output.txt"))
                .replace("\r\n", "\n").trim();

        // 5. 取得實際輸出
        String actualOutput = testOut.toString().replace("\r\n", "\n").trim();

        // 6. 驗證
        assertEquals(expectedOutput, actualOutput);
    }
}
