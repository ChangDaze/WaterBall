package Showdown;

import CardGame.CardGame;
import CardGame.Player;

import java.util.ArrayList;
import java.util.List;

public class ShowdownGame extends CardGame<ShowdownCard> {
    private List<ShowdownPlayer> players;
    private ShowdownDeck deck;
    private ShowdownDeck abandonedDeck;

    @Override
    protected void initialize() {
        setPlayerLimits(4);
        setHandDefault(13);

        List<ShowdownPlayer> players = new ArrayList<>();
        for (int i = 0; i < getPlayerLimits(); i++) {
            ShowdownPlayer player = new ShowdownPlayer();
            player.nameHimself();
            players.add(player);
        }
        this.players = players;

        var deck = new ShowdownDeck();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.addCard(new ShowdownCard(rank, suit));
            }
        }
        deck.shuffle();
        this.deck = deck;

        var abandonedDeck = new ShowdownDeck();
        this.abandonedDeck = abandonedDeck;
    }

    @Override
    protected void dealCard() {
        for (int i = 0; i < getHandDefault(); i++) {
            for(var player : players) {
                player.addHand(deck.drawCard());
            }
        }
    }

    public void takeTurn() {
        for (int i = 0; i< getHandDefault(); i++) {

            var winner = players.getFirst();
            var winnerCard = winner.show(null);
            abandonedDeck.addCard(winnerCard);

            for(var player : players) {

                if (winner.equals(player)) {
                    continue;
                }

                var showCard = player.show(winnerCard);
                var showValue = showCard.getRank().getValue();
                var showSuit = showCard.getSuit().getValue();
                var winnerValue = winnerCard.getRank().getValue();
                var winnerSuit = winnerCard.getSuit().getValue();
                if ( showValue > winnerValue ||
                    (showValue == winnerValue && showSuit >  winnerSuit )
                )
                {
                    winner = player;
                    winnerCard = showCard;
                }

                abandonedDeck.addCard(showCard);
            }

            winner.addPoint();
            System.out.printf("【%d】 %s win \r\n", i, winner.getName());
        }
    }

    public void checkWinner() {
        var winner = players.getFirst();

        for(var player : players) {

            if (winner.equals(player)) {
                continue;
            }

            if (player.getPoint() > winner.getPoint()) {
                winner = player;
            }
        }

        setGameWinner(winner);
        System.out.printf("%s is winner, points : %d \r\n", winner.getName(), winner.getPoint());
    }
}
