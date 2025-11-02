import Battle.Battle;
import InputManager.InputManager;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BattleTest {

    private void runBattleTest(String testName) throws IOException {
        // 儲存原始輸入輸出（非常重要），不然沒辦法每個測試一起跑
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            // 1. 讀取 input
            byte[] inputBytes = Files.readAllBytes(Path.of("src/test/resources/" + testName + ".in"));
            Scanner testScanner = new Scanner(new ByteArrayInputStream(inputBytes));

            // 2. 注入 InputManager
            InputManager.getInstance().setScanner(testScanner);

            // 3. 捕捉輸出（同時導向 Console + ByteArray）
            ByteArrayOutputStream testOut = new ByteArrayOutputStream();
            PrintStream tee = new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    originalOut.write(b);  // 顯示在 console
                    testOut.write(b);      // 同時保存
                }
            });
            System.setOut(tee);

            // 4. 執行程式
            Battle battle = Battle.builder().troops(new ArrayList<>()).round(0).build();
            battle.createBattle();
            battle.startBattle();

            // 5. 驗證輸出
            String expectedOutput = Files.readString(Path.of("src/test/resources/" + testName + ".out"))
                    .replace("\r\n", "\n").trim();
            String actualOutput = testOut.toString().replace("\r\n", "\n").trim();

            assertEquals(expectedOutput, actualOutput);

        } finally {
            // 6. 無論測試結果如何都還原
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    //各測試改成呼叫共用方法即可
    /**
     * 追加傷害不會另外顯示
     */
    @Test
    public void testCheerup() throws IOException {
        runBattleTest("cheerup");
    }

    /**
     * 詛咒其實沒影響受詛咒者的負面效果
     * 回復血量時其實也沒提示訊息
     */
    @Test
    public void testCurse() throws IOException {
        runBattleTest("curse");
    }

    @Test
    public void testExample() throws IOException {
        runBattleTest("example");
    }

    /**
     * 測試裡面有時候或放很多相同技能只是為了方變AI seed 連續使用相同技能
     * 用onepuch時記得field不要在子類別加上，因為讀的都是共同父類別的next lombok分不出來
     * 連續傷害poisononepuch記得敵人死亡就不要繼續鞭屍了
     * 測試時的user input其實只有英雄行動，AI不會透過input，會全都是seed操作，知道這點比較好debug，找對應行動bug用英雄行動來找
     */
    @Test
    public void testOnePunch() throws IOException {
        runBattleTest("one-punch");
    }

    @Test
    public void testOnlyBasicAttack() throws IOException {
        runBattleTest("only-basic-attack");
    }

    /**
     * 將狀態檢查修正成每回合開始幫全部人檢查
     * 狀態變化也是沒有提示訊息
     * 除了透過英雄input和行動來幫助快速debug，也能把console輸出複製到notpad直接看行數比對，有了行數比對也容易許多
     * 複製到notpad可以用個角色行動時輸出狀趟做簡單的終極密碼比對，比到差異處再開始上下找原因
     */
    @Test
    public void testPetrochemical() throws IOException {
        runBattleTest("petrochemical");
    }

    /**
     * PoisonedState的扣血在這個測試也是沒有提示訊息的
     * 然後也因此多時做了沒有參數的damagereceived來提示死亡
     */
    @Test
    public void testPoison() throws IOException {
        runBattleTest("poison");
    }

    @Test
    public void testSelfExplosion() throws IOException {
        runBattleTest("self-explosion");
    }

    /**
     * 可以考慮將世芳既能印出的訊息邏輯分散到各個skill，目前是統一塞在rolestrategy，但感覺客製化開始變多了
     */
    @Test
    public void testSelfHealing() throws IOException {
        runBattleTest("self-healing");
    }

    @Test
    public void testSummon() throws IOException {
        runBattleTest("summon");
    }

    /**
     * 注意summon會在for迴圈時往list新增資料，所以不能用foreach
     */
    @Test
    public void testWaterballAndFireball1v2() throws IOException {
        runBattleTest("waterball-and-fireball-1v2");
    }
}
