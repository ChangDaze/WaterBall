package AbstractFactoryPattern.v3.factories;

import AbstractFactoryPattern.v3.Region;
import AbstractFactoryPattern.v3.Stage;

public interface StageFactory {
    Stage createStage(Region region);
}
