package Showdown;

public class Main {
    public static void main(String[] args) {
        ShowdownGame game = new ShowdownGame();
        game.start(1);
        System.out.printf("Final winner %s , points : %d \r\n", game.getGameWinner().getName(), game.getGameWinner().getPoint());
    }
}
