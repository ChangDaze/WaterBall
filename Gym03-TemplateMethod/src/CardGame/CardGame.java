package CardGame;

import Showdown.ShowdownCard;
import Showdown.ShowdownPlayer;

import java.util.List;

public abstract class CardGame<C> {
    protected List<Player<C>> players;
    protected Deck<C> deck;
    protected Deck<C> abandonedDeck;
    protected int playerLimits;
    protected int handLimits;
    protected Player<C> gameWinner;

    protected void start() {
        initialize();
        dealCard();
        takeTurn();
        checkWinner();
    }

    /*
    初始化Player, Deck, 及相關設定
     */
    protected abstract void initialize();

    /*
    發牌階段
     */
    protected void dealCard() {
        for (int i = 0; i< handLimits; i++) {
            for(Player<C> player : players) {
                player.addHand(deck.drawCard());
            }
        }
    }

    /*
    出牌階段
     */
    protected abstract void takeTurn();

    /*
    決定勝利者
     */
    protected void checkWinner() {
        Player<C> winner = players.getFirst();

        for(Player<C> player : players) {

            if (winner.equals(player)) {
                continue;
            }

            if (winnerChallenge(player, winner)) {
                winner = player;
            }
        }

        gameWinner = winner;
    }

    /*
    決定新winner的比較
     */
    protected abstract boolean winnerChallenge(Player<C> challenger, Player<C> defender);

    protected Player<C> getGameWinner() {
        return gameWinner;
    }
}
