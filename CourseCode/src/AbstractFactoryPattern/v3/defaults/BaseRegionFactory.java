package AbstractFactoryPattern.v3.defaults;

import AbstractFactoryPattern.v3.BaseRegion;
import AbstractFactoryPattern.v3.Floor;
import AbstractFactoryPattern.v3.factories.RegionFactory;
import AbstractFactoryPattern.v3.factories.StageFactory;

public class BaseRegionFactory implements RegionFactory {
    private final StageFactory stageFactory;

    public BaseRegionFactory(StageFactory stageFactory) {
        this.stageFactory = stageFactory;
    }

    @Override
    public BaseRegion createRegion(int number, Floor floor) {
        return new BaseRegion(number,floor,stageFactory);//要往下注入所以BaseRegionFactory的constructor也要注入StageFactory
    }
}
