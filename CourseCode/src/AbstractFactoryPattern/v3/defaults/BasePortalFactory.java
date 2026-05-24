package AbstractFactoryPattern.v3.defaults;

import AbstractFactoryPattern.v3.BasePortal;
import AbstractFactoryPattern.v3.Portable;
import AbstractFactoryPattern.v3.factories.PortalFactory;

public class BasePortalFactory implements PortalFactory {
    @Override
    public BasePortal createPortal(Portable p1, Portable p2) {
        return BasePortal.makePortal(p1, p2);
    }
}
