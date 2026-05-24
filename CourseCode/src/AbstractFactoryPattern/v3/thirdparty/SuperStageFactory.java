package AbstractFactoryPattern.v3.thirdparty;

import AbstractFactoryPattern.v3.Region;
import AbstractFactoryPattern.v3.Stage;
import AbstractFactoryPattern.v3.factories.StageFactory;

public class SuperStageFactory implements StageFactory {
    @Override
    public Stage createStage(Region region) {
        return new SuperStage();
    }
}
