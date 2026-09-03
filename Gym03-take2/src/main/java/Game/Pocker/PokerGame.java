package Game.Pocker;

import Game.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokerGame extends Game {
    private static final String[] RANK_FACES = {
            "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"
    };
    private static final String[] SUIT_FACES = {
            "Club", "Diamond", "Heart", "Spade"
    };

    protected Map<Player, Integer> scoreMap = new HashMap<>();

    @Override
    protected void prepareDeck() {
        List<Card> cards = new ArrayList<>();
        for (int suit = 1; suit <= SUIT_FACES.length; suit++) {
            String suitFace = SUIT_FACES[suit - 1];
            for (int rank = 1; rank <= RANK_FACES.length; rank++) {
                String rankFace = RANK_FACES[rank - 1];
                cards.add(new PockerCard(rank, rankFace, suit, suitFace));
            }
        }
        this.deck = new Deck(cards);
        this.deck.shuffle();
    }

    @Override
    protected void beforePlay() {
        this.players = new ArrayList<>();
        this.players.add(new HumanPockerPlayer());
        for (int i = 0; i < 3; i++) {
            this.players.add(new AIPokerPlayer());
        }
        for (Player player : players) {
            player.nameHimself();
            scoreMap.put(player, 0);
        }
        for (int i = 0; i < 13; i++) {
            for (Player player : players) {
                player.addHand(this.deck.drawCard());
            }
        }
    }

    @Override
    protected boolean gameOver() {
        if (this.turn > 13) {
            Player winner = null;
            int maxScore = Integer.MIN_VALUE;
            for (Map.Entry<Player, Integer> entry : scoreMap.entrySet()) {
                if (entry.getValue() > maxScore) {
                    maxScore = entry.getValue();
                    winner = entry.getKey();
                }
            }
            if (winner != null) {
                System.out.println("=== Poker Game Over! Winner: " + winner.getName() + " (Score: " + maxScore + ") ===");
            }
            return true;
        }
        return false;
    }

    @Override
    protected void compare(Map<Player, Card> playerShows) {
        if (playerShows == null || playerShows.isEmpty()) {
            return;
        }
        Player winner = null;
        PockerCard winningCard = null;

        for (Map.Entry<Player, Card> entry : playerShows.entrySet()) {
            if (entry.getValue() instanceof PockerCard card) {
                if (winningCard == null || card.compareTo(winningCard) > 0) {
                    winningCard = card;
                    winner = entry.getKey();
                }
            }
        }

        if (winner != null) {
            scoreMap.put(winner, scoreMap.getOrDefault(winner, 0) + 1);
            System.out.println("Turn " + this.turn + " Winner: " + winner.getName());
        }
    }

    @Override
    protected void turnOver() {
        this.playerShows.clear();
        this.shows.clear();
    }
}
