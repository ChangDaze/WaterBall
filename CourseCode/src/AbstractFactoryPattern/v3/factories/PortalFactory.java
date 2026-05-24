package AbstractFactoryPattern.v3.factories;

import AbstractFactoryPattern.v3.Portable;
import AbstractFactoryPattern.v3.Portal;

public interface PortalFactory {
    Portal createPortal(Portable p1, Portable p2);
}
