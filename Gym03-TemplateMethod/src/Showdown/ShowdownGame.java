package Showdown;

import java.util.ArrayList;
import java.util.List;

public class ShowdownGame {
    private List<ShowdownPlayer> players;
    private ShowdownDeck deck;
    private ShowdownDeck abandonedDeck;

    public void start() {
        players = new ArrayList<>();
        for (int i = 0 ; i < 4 ; i++) {
            ShowdownPlayer player = new ShowdownPlayer();
            player.nameHimself();
            players.add(player);
        }

        deck = new ShowdownDeck();
        deck.shuffle();

        abandonedDeck = new ShowdownDeck();

        dealCard();
        takeTurn();
        checkWinner();
    }

    private void dealCard() {
        for (int i = 0; i< 13; i++) {
            for(ShowdownPlayer player : players) {
                player.drawCard(deck);
            }
        }
    }

    public void takeTurn() {
        for (int i = 0; i< 13; i++) {

            ShowdownPlayer winner = players.getFirst();
            ShowdownCard winnerCard = winner.show();
            abandonedDeck.addCard(winnerCard);


            for(ShowdownPlayer player : players) {

                if (winner.equals(player)) {
                    continue;
                }

                var showCard = player.show();
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
        ShowdownPlayer winner = players.getFirst();

        for(ShowdownPlayer player : players) {

            if (winner.equals(player)) {
                continue;
            }

            if (player.getPoint() > winner.getPoint()) {
                winner = player;
            }
        }

        System.out.printf("%s is winner, points : %d \r\n", winner.getName(), winner.getPoint());
    }
}
