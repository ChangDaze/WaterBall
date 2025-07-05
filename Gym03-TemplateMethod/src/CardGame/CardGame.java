package CardGame;

import java.util.List;

public abstract class CardGame<C> {
    private int playerLimits;
    private int handDefault;
    private Player<C> gameWinner;

    public void start() {
        initialize();
        dealCard();
        takeTurn();
        checkWinner();
    }

    // <editor-fold desc="getter & setter">
    public int getPlayerLimits() {
        return playerLimits;
    }

    protected void setPlayerLimits(int playerLimits) {
        this.playerLimits = playerLimits;
    }

    public int getHandDefault() {
        return handDefault;
    }

    protected void setHandDefault(int handDefault) {
        this.handDefault = handDefault;
    }

    public Player<C> getGameWinner() {
        return gameWinner;
    }

    protected void setGameWinner(Player<C> gameWinner) {
        this.gameWinner = gameWinner;
    }
    // </editor-fold>

    /**
    初始化Player, Deck, 及相關設定
     **/
    protected abstract void initialize();

    /**
    發牌階段
     **/
    protected abstract void dealCard();

    /**
    出牌階段
     **/
    protected abstract void takeTurn();

    /**
    決定勝利者
     **/
    protected abstract void checkWinner();
}
