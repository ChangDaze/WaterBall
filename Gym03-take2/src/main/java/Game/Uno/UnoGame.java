package Game.Uno;

import Game.*;

import java.util.ArrayList;
import java.util.List;

public class UnoGame extends Game {
    private static final String[] COLORS = {"BLUE", "RED", "YELLOW", "GREEN"};

    @Override
    protected void prepareDeck() {
        List<Card> cards = new ArrayList<>();
        for (String color : COLORS) {
            for (int number = 0; number <= 9; number++) {
                cards.add(new UnoCard(color, number));
            }
        }
        this.deck = new Deck(cards);
        this.deck.shuffle();
    }

    @Override
    protected void beforePlay() {
        this.players = new ArrayList<>();
        this.players.add(new HumanUnoPlayer());
        for (int i = 0; i < 3; i++) {
            this.players.add(new AIUnoPlayer());
        }
        for (Player player : players) {
            player.nameHimself();
        }
        for (int i = 0; i < 5; i++) {
            for (Player player : players) {
                player.addHand(this.deck.drawCard());
            }
        }
    }

    @Override
    protected boolean gameOver() {
        if (players == null) {
            return false;
        }
        for (Player player : players) {
            if (player.getHands().isEmpty()) {
                System.out.println("=== Uno Game Over! Winner: " + player.getName() + " ===");
                return true;
            }
        }
        return false;
    }

    @Override
    protected void afterShow(Player player, Card show) {
        if (show == null) {
            System.out.println(player.getName() + " cannot play and draws a card.");
            if (this.deck.getCards().isEmpty()) {
                rebuildDeck();
            }
            player.addHand(this.deck.drawCard());
            if (this.deck.getCards().isEmpty()) {
                rebuildDeck();
            }
        }
    }

    private void rebuildDeck() {
        if (this.shows != null && this.shows.size() > 1) {
            System.out.println("[Deck empty: reshuffled discard pile back into deck]");
            Card lastCard = this.shows.removeLast();
            List<Card> newCards = new ArrayList<>(this.shows);
            this.shows.clear();
            this.shows.add(lastCard);
            this.deck = new Deck(newCards);
            this.deck.shuffle();
        }
    }
}
