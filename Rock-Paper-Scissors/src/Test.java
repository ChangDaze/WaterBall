public class Test {
    public static void main(String[] args) {
        Game game = new Game(new HumanPlayer(1), new AIPlayer(2));
        game.Start();

        Game game2 = new Game(new HumanPlayer(2), new AIPlayer(2));
        game.Start();
    }
}
