package AbstractFactoryPattern.v3;

import AbstractFactoryPattern.v3.factories.PortalFactory;
import AbstractFactoryPattern.v3.factories.RegionFactory;

public class TopFloor extends Floor {
    public TopFloor(Game game, RegionFactory regionFactory, PortalFactory portalFactory) {
        super(game, "Top Floor", null, 1, regionFactory, portalFactory);
    }

    @Override
    public void access(Player player) {
        System.out.println("You win this game! Congratulations!");
        game.over();
    }
}
