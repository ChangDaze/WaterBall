package AbstractFactoryPattern.v4.defaults;

import AbstractFactoryPattern.v4.*;
import AbstractFactoryPattern.v4.factories.FloorAbstractFactory;

public class BaseFloorAbstractFactory implements FloorAbstractFactory {
    @Override
    public Portal createPortal(Portable p1, Portable p2) {
        return BasePortal.makePortal(p1, p2);
    }

    @Override
    public Region createRegion(int number, Floor floor) {
        return new BaseRegion(number, floor);
    }//結果Region和Stage都歸在工廠一致性的層級Floor下，所以也不用再注入Stage給Region了

    @Override
    public Stage createStage(Region region) {
        return new BaseStage(region);
    }
}
