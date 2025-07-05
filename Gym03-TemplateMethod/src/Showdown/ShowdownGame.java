package Showdown;

import CardGame.CardGame;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ShowdownGame extends CardGame<ShowdownCard, ShowdownDeck, ShowdownPlayer> {
    private final List<ShowdownPlayer> players = new ArrayList<>();
    private ShowdownDeck deck;
    private ShowdownDeck abandonedDeck;

    // <editor-fold desc="getter & setter">
    @Override
    public ShowdownDeck getDeck() {
        return deck;
    }

    @Override
    public ShowdownDeck getAbandonedDeck() {
        return abandonedDeck;
    }

    @Override
    public List<ShowdownPlayer> getPlayers() {
        return players;
    }
    // </editor-fold>

    @Override
    protected void initialize() {
        setPlayerLimits(4);
        setHandDefault(13);
    }

    @Override
    protected void addNewPlayer(List<ShowdownPlayer> players) {
        ShowdownPlayer player = new ShowdownPlayer();
        player.nameHimself();
        players.add(player);
    }

    @Override
    protected void dealDeck() {
        this.deck = new ShowdownDeck();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                this.deck.addCard(new ShowdownCard(rank, suit));
            }
        }
        this.deck.shuffle();

        this.abandonedDeck = new ShowdownDeck();
    }

    @Override
    protected void dealHand(ShowdownPlayer player, ShowdownDeck deck) {
        player.addHand(deck.drawCard());
    }

    @Override
    protected void takeTurn(ShowdownPlayer player, ShowdownDeck deck, ShowdownDeck abandonedDeck, List<Pair<ShowdownCard, ShowdownPlayer>> turnCards) {
        var showCard = player.show(null); //大家看不到出牌
        turnCards.add(Pair.of(showCard, player));
        abandonedDeck.addCard(showCard);
    }

    @Override
    protected boolean gameOver(int turn) {
        return turn > 13;
    }

    @Override
    protected void turnOver(List<Pair<ShowdownCard, ShowdownPlayer>> turnCards) {
        Pair<ShowdownCard, ShowdownPlayer> turnWinner = null;

        for(var turnCard : turnCards) {

            if (turnWinner == null) {
                turnWinner = turnCard;
                continue;
            }


            var card = turnCard.getLeft();
            var cardValue = card.getRank().getValue();
            var cardSuit = card.getSuit().getValue();

            var winnerCard = turnWinner.getLeft();
            var winnerValue = winnerCard.getRank().getValue();
            var winnerSuit = winnerCard.getSuit().getValue();
            if ( cardValue > winnerValue || (cardValue == winnerValue && cardSuit >  winnerSuit )
            )
            {
                turnWinner = turnCard;
            }
        }

        assert turnWinner != null;
        var winner = turnWinner.getRight();
        winner.addPoint();
        System.out.printf("%s win \r\n", winner.getName());
    }

    @Override
    public void checkWinner() {
        ShowdownPlayer winner  = null;

        for(var player : players) {

            if (winner == null) {
                winner = player;
                continue;
            }

            if (player.getPoint() > winner.getPoint()) {
                winner = player;
            }
        }

        assert winner != null;
        setGameWinner(winner);
        System.out.printf("%s is winner, points : %d \r\n", winner.getName(), winner.getPoint());
    }
}
