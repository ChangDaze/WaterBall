package AbstractFactoryPattern.v4.thirdparty;

import AbstractFactoryPattern.v4.Player;
import AbstractFactoryPattern.v4.Region;
import AbstractFactoryPattern.v4.RegionDecorator;

public class SuperRegion extends RegionDecorator {
    public SuperRegion(Region next) {
        super(next);
    }

    @Override
    public void access(Player player) {
        System.out.println("<Waterball Region>");
        next.access(player);
    }
}
