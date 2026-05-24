package AbstractFactoryPattern.v3.thirdparty;

import AbstractFactoryPattern.v3.Player;
import AbstractFactoryPattern.v3.Portal;
import AbstractFactoryPattern.v3.PortalDecorator;

public class SuperPortal extends PortalDecorator {
    public SuperPortal(Portal next) {
        super(next);
    }

    @Override
    public void access(Player player) {
        System.out.println("<有機率打廣告>");
        next.access(player);
    }
}
