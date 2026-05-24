package AbstractFactoryPattern.v3.defaults;

import AbstractFactoryPattern.v3.BaseStage;
import AbstractFactoryPattern.v3.Region;
import AbstractFactoryPattern.v3.Stage;
import AbstractFactoryPattern.v3.factories.StageFactory;

public class BaseStageFactory implements StageFactory {
    @Override
    public Stage createStage(Region region) {
        return new BaseStage(region);
    }
}
