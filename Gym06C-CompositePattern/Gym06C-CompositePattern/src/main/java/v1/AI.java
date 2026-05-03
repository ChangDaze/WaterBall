package v1;

import static v1.Logger.getLogger;

public class AI {
    // 取得 "app.game.ai" 日誌器，名為 log 屬性
    private Logger log = getLogger("app.game.ai");
    private String name;

    // constructor
    public AI(String name) {
        this.name = name;
    }

    // 模擬 AI 決策，請日誌器撰寫日誌訊息，並做適當的訊息分級。
    public void makeDecision() {

        log.trace("%s starts making decisions...".formatted(name));

        log.warn("%s decides to give up.".formatted(name));
        log.error("Something goes wrong when AI gives up.");

        log.trace("%s completes its decision.".formatted(name));
    }

    public String getName() {
        return name;
    }
}
