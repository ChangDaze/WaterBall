package AbstractFactoryPattern.v4;

import AbstractFactoryPattern.v4.factories.FloorAbstractFactory;

public class TopFloor extends Floor {
    public TopFloor(Game game, FloorAbstractFactory factory) {
        super(game, "Top Floor", null, 1, factory);
    }

    @Override
    public void access(Player player) {
        System.out.println("You win this game! Congratulations!");
        game.over();
    }
}
