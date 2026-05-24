package AbstractFactoryPattern.v4.factories;

import AbstractFactoryPattern.v4.*;

public interface FloorAbstractFactory {
    Portal createPortal(Portable p1, Portable p2);
    Region createRegion(int number, Floor floor);
    Stage createStage(Region region);
}
