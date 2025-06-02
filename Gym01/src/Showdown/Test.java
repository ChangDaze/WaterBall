package Showdown;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Player> players = new ArrayList<>();

        players.add(new HumanPlayer());
        players.add(new AIPlayer());
        players.add(new AIPlayer());
        players.add(new AIPlayer());

        Game game = new Game(players);
        game.Start();
    }
}
