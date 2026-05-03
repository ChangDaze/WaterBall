import v1.*;
import static v1.Level.*;
import static v1.Logger.declareLoggers;

public class Main {
    public static void main(String[] args) {
        // 定義根日誌器
        var root = new Logger("root", DEBUG, null, new StandardLayout(), new ConsoleExporter());

        // 定義 app.game 日誌器，繼承根日誌器並覆寫分級門檻和輸出器
        var gameLogger = new Logger("app.game",INFO, root, new StandardLayout(),
                new CompositeExporter(
                        new ConsoleExporter(),
                        new CompositeExporter(
                                new FileExporter("game.log"),
                                new FileExporter("game.backup.log")
                        )
                        ));

        // 定義 app.game.ai 日誌器，繼承 app.game 日誌器並覆寫分級門檻 => 繼承 exporter
        var aiLogger = new Logger("app.game.ai", TRACE, gameLogger, new StandardLayout(), null);

        // 配置剛定義好的三個日誌器
        declareLoggers(root, gameLogger, aiLogger);

        // 創建遊戲物件，並執行遊戲
        Game game = new Game();
        game.start();
    }
}
