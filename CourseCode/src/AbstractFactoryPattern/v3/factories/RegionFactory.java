package AbstractFactoryPattern.v3.factories;

import AbstractFactoryPattern.v3.Floor;
import AbstractFactoryPattern.v3.Region;

public interface RegionFactory {
    Region createRegion(int number, Floor floor);
}
