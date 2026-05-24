package AbstractFactoryPattern.v3.thirdparty;

import AbstractFactoryPattern.v3.BasePortal;
import AbstractFactoryPattern.v3.Portable;
import AbstractFactoryPattern.v3.Portal;
import AbstractFactoryPattern.v3.factories.PortalFactory;

public class SuperPortalFactory implements PortalFactory {
    //在BasePortal上裝飾SuperPortal
    @Override
    public Portal createPortal(Portable p1, Portable p2) {
        return new SuperPortal(BasePortal.makePortal(p1, p2));
    }
}
