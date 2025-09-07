package Big2;

import example.MyApp;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Big2Test {
    @Test
    public void testAlwaysPlayFirstCard() throws IOException {
        // 1. 讀取 input.txt（從 test resources）
        byte[] inputBytes = Files.readAllBytes(Path.of("src/test/resources/always-play-first-card.in"));
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputBytes);
        System.setIn(testIn);

        // 2. 捕捉標準輸出
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        // 3. 執行程式（整體一次跑完）
        Big2 big2 = new Big2();
        big2.start();

        // 4. 讀取 output.txt（預期輸出）
        String expectedOutput = Files.readString(Path.of("src/test/resources/always-play-first-card.out"))
                .replace("\r\n", "\n").trim();

        // 5. 取得實際輸出
        String actualOutput = testOut.toString().replace("\r\n", "\n").trim();

        // 6. 驗證
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testNormalNoRrrorPlay1() throws IOException {
        // 1. 讀取 input.txt（從 test resources）
        byte[] inputBytes = Files.readAllBytes(Path.of("src/test/resources/normal-no-error-play1.in"));
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputBytes);
        System.setIn(testIn);

        // 2. 捕捉標準輸出
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        // 3. 執行程式（整體一次跑完）
        Big2 big2 = new Big2();
        big2.start();

        // 4. 讀取 output.txt（預期輸出）
        String expectedOutput = Files.readString(Path.of("src/test/resources/normal-no-error-play1.out"))
                .replace("\r\n", "\n").trim();

        // 5. 取得實際輸出
        String actualOutput = testOut.toString().replace("\r\n", "\n").trim();

        // 6. 驗證
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testNormalNoRrrorPlay2() throws IOException {
        // 1. 讀取 input.txt（從 test resources）
        byte[] inputBytes = Files.readAllBytes(Path.of("src/test/resources/normal-no-error-play2.in"));
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputBytes);
        System.setIn(testIn);

        // 2. 捕捉標準輸出
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        // 3. 執行程式（整體一次跑完）
        Big2 big2 = new Big2();
        big2.start();

        // 4. 讀取 output.txt（預期輸出）
        String expectedOutput = Files.readString(Path.of("src/test/resources/normal-no-error-play2.out"))
                .replace("\r\n", "\n").trim();

        // 5. 取得實際輸出
        String actualOutput = testOut.toString().replace("\r\n", "\n").trim();

        // 6. 驗證
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testStraight() throws IOException {
        // 1. 讀取 input.txt（從 test resources）
        byte[] inputBytes = Files.readAllBytes(Path.of("src/test/resources/straight.in"));
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputBytes);
        System.setIn(testIn);

        // 2. 捕捉標準輸出
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        // 3. 執行程式（整體一次跑完）
        Big2 big2 = new Big2();
        big2.start();

        // 4. 讀取 output.txt（預期輸出）
        String expectedOutput = Files.readString(Path.of("src/test/resources/straight.out"))
                .replace("\r\n", "\n").trim();

        // 5. 取得實際輸出
        String actualOutput = testOut.toString().replace("\r\n", "\n").trim();

        // 6. 驗證
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testFullhouse() throws IOException {
        // 1. 讀取 input.txt（從 test resources）
        byte[] inputBytes = Files.readAllBytes(Path.of("src/test/resources/fullhouse.in"));
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputBytes);
        System.setIn(testIn);

        // 2. 捕捉標準輸出
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        // 3. 執行程式（整體一次跑完）
        Big2 big2 = new Big2();
        big2.start();

        // 4. 讀取 output.txt（預期輸出）
        String expectedOutput = Files.readString(Path.of("src/test/resources/fullhouse.out"))
                .replace("\r\n", "\n").trim();

        // 5. 取得實際輸出
        String actualOutput = testOut.toString().replace("\r\n", "\n").trim();

        // 6. 驗證
        assertEquals(expectedOutput, actualOutput);
    }


    @Test
    public void testIllegalActions() throws IOException {
        // 1. 讀取 input.txt（從 test resources）
        byte[] inputBytes = Files.readAllBytes(Path.of("src/test/resources/illegal-actions.in"));
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputBytes);
        System.setIn(testIn);

        // 2. 捕捉標準輸出
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        // 3. 執行程式（整體一次跑完）
        Big2 big2 = new Big2();
        big2.start();

        // 4. 讀取 output.txt（預期輸出）
        String expectedOutput = Files.readString(Path.of("src/test/resources/illegal-actions.out"))
                .replace("\r\n", "\n").trim();

        // 5. 取得實際輸出
        String actualOutput = testOut.toString().replace("\r\n", "\n").trim();

        // 6. 驗證
        assertEquals(expectedOutput, actualOutput);
    }
}
