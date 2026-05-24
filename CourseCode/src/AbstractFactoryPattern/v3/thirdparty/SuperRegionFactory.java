package AbstractFactoryPattern.v3.thirdparty;

import AbstractFactoryPattern.v3.BaseRegion;
import AbstractFactoryPattern.v3.Floor;
import AbstractFactoryPattern.v3.Region;
import AbstractFactoryPattern.v3.factories.RegionFactory;
import AbstractFactoryPattern.v3.factories.StageFactory;

public class SuperRegionFactory implements RegionFactory {
    private final StageFactory stageFactory;

    public SuperRegionFactory(StageFactory stageFactory) {
        this.stageFactory = stageFactory;
    }

    @Override
    public Region createRegion(int number, Floor floor) {
        //在BaseRegion上裝飾SuperRegion
        return new SuperRegion(new BaseRegion(number, floor, stageFactory));
    }
}
