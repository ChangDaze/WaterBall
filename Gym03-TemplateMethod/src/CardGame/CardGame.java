package CardGame;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public abstract class CardGame<C, D, P> {
    private int playerLimits;
    private int handDefault;
    private P gameWinner;

    // <editor-fold desc="getter & setter">
    protected int getPlayerLimits() {
        return playerLimits;
    }

    protected void setPlayerLimits(int playerLimits) {
        this.playerLimits = playerLimits;
    }

    protected int getHandDefault() {
        return handDefault;
    }

    protected void setHandDefault(int handDefault) {
        this.handDefault = handDefault;
    }

    protected void setGameWinner(P gameWinner) {
        this.gameWinner = gameWinner;
    }

    protected abstract D getDeck();

    protected abstract D getAbandonedDeck();

    protected abstract List<P> getPlayers();

    public P getGameWinner() {
        return gameWinner;
    }
    // </editor-fold>

    public void start() {
        initialize();
        dealPlayers();
        dealDeck();
        dealHands();
        gameProgress();
        checkWinner();
    }

    /**
    初始化Player, Deck, 及相關設定
     **/
    protected abstract void initialize();

    /**
     Player加入階段
     */
    protected void dealPlayers() {
        List<P> players = getPlayers();
        for (int i = 0; i < getPlayerLimits(); i++) {
            addNewPlayer(players);
        }
    }

    /**
     加入新Player入Game
     */
    protected abstract void addNewPlayer(List<P> players);

    /**
     Deck建立階段
     */
    protected abstract void dealDeck();

    /**
    發手牌階段
     */
    protected void dealHands() {
        for (int i = 0; i < getHandDefault(); i++) {
            for(var player : getPlayers()) {
                dealHand(player, getDeck());
            }
        }
    }

    /**
     加入新Player入Game
     */
    protected abstract void dealHand(P player, D deck);

    /**
     遊戲進行階段
     **/
    protected void gameProgress() {
        int turn = 1;
        while (!gameOver()) {
            System.out.printf("Turn 【%d】 \r\n", turn);
            List<Pair<C, P>> turnCards = new ArrayList<>();
            for(var player : getPlayers()) {
                takeTurn(player, getDeck(), getAbandonedDeck(), turnCards);
            }
            turnOver(turnCards);
            turn++;
        }
    }

    /**
     遊戲結束判斷
     */
    protected abstract boolean gameOver();

    /**
     玩家回合
     */
    protected abstract void takeTurn(P player, D deck, D abandonedDeck, List<Pair<C, P>> turnCards);

    /**
     回合結束
     */
    protected void turnOver(List<Pair<C, P>> turnCards) {

    }

    /**
    決定勝利者
     **/
    protected abstract void checkWinner();
}
