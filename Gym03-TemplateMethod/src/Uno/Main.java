package Uno;

import Showdown.ShowdownGame;

public class Main {
    public static void main(String[] args) {
        UnoGame game = new UnoGame();
        game.start();
        System.out.printf("Final winner %s \r\n", game.getGameWinner().getName());
    }
}
