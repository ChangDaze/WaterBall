package AbstractFactoryPattern.v3.thirdparty;

import AbstractFactoryPattern.v3.Player;
import AbstractFactoryPattern.v3.Region;
import AbstractFactoryPattern.v3.RegionDecorator;

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
