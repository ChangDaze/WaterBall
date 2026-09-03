package Game;

import Game.Pocker.PokerGame;
import Game.Uno.UnoGame;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Starting Poker Game ===");
        Game pokerGame = new PokerGame();
        pokerGame.start();

        System.out.println("\n=== Starting Uno Game ===");
        Game unoGame = new UnoGame();
        unoGame.start();
    }
}
