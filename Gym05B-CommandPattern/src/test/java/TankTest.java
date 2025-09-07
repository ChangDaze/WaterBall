import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {

    @Test
    void moveForward() {
        // 1. 讀取 input.txt（從 test resources）
//        byte[] inputBytes = Files.readAllBytes(Path.of("src/test/resources/always-play-first-card.in"));
//        ByteArrayInputStream testIn = new ByteArrayInputStream(inputBytes);
//        System.setIn(testIn);

        // 2. 捕捉標準輸出
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        // 3. 執行程式（整體一次跑完）
        Tank tank = new Tank();
        tank.moveForward();

        // 4. 讀取 output.txt（預期輸出）
//        String expectedOutput = Files.readString(Path.of("src/test/resources/always-play-first-card.out"))
//                .replace("\r\n", "\n").trim();

        // 5. 取得實際輸出
        String actualOutput = testOut.toString().replace("\r\n", "\n").trim();

        // 6. 驗證
        assertEquals("The tank has moved forward.", actualOutput);
    }
}