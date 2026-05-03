package v2;

import java.util.List;

public class Game {
    private Logger log = LogManager.getLogger("app.game.ai");

    // 四個 AI 玩家，依序命名為 AI 1~4。
    List<AI> players = List.of(
            new AI("AI 1"),
            new AI("AI 2"),
            new AI("AI 3"),
            new AI("AI 4")
    );

    // 模擬遊戲執行，請日誌器撰寫日誌訊息，並且做適當的訊息分級。
    public void start() {
        log.info("The game begins.");

        // 每個 AI 玩家輪流做決策
        for (var ai: players) {
            log.trace("The player *%s* begins his turn.".formatted(ai.getName()));
            ai.makeDecision();
            log.trace("The player *%s* finishes his turn.".formatted(ai.getName()));
        }

        log.debug("Game ends.");
    }
}
