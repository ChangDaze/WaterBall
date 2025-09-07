import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    @Test
    void mainTest() throws IOException {
        //最後一行沒有換行
        //Scanner.nextLine() 在檔案最後一行沒有換行時，可能會立即到達 EOF。 => 可能會出錯
        // 1. 讀取 input.txt（從 test resources）
        byte[] inputBytes = Files.readAllBytes(Path.of("src/test/resources/input.txt"));
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputBytes);
        System.setIn(testIn);

        // 2. 捕捉標準輸出
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut, true));// true 表示 autoFlush這樣才能把輸出都讀到

        // 3. 執行程式（整體一次跑完）
        Main main = new Main();
        main.main(null); //要讓 JUnit 測試 Scanner 輸入，就必須 模擬輸入 (fake System.in)。

        // 4. 讀取 output.txt（預期輸出）
        String expectedOutput = Files.readString(Path.of("src/test/resources/output.txt"))
                .replace("\r\n", "\n").trim();

        // 5. 取得實際輸出
        String actualOutput = testOut.toString().replace("\r\n", "\n").trim();

        // 6. 驗證
        assertEquals(expectedOutput, actualOutput);
    }
}
