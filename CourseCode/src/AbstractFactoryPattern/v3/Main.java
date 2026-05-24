package AbstractFactoryPattern.v3;

import AbstractFactoryPattern.v3.defaults.*;
import AbstractFactoryPattern.v3.factories.*;
import AbstractFactoryPattern.v3.thirdparty.*;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        StageFactory baseStageFactory = new BaseStageFactory();
        PortalFactory basePortalFactory = new BasePortalFactory();
        RegionFactory baseRegionFactory = new BaseRegionFactory(baseStageFactory);
        SuperStageFactory superStageFactory = new SuperStageFactory();
        SuperRegionFactory superRegionFactory = new SuperRegionFactory(superStageFactory);
        SuperPortalFactory superPortalFactory = new SuperPortalFactory();

        TopFloor topFloor = new TopFloor(game, baseRegionFactory, basePortalFactory);
        Floor f3 = new Floor(game, "Super Floor 3", topFloor, superRegionFactory, superPortalFactory);
        Floor f2 = new Floor(game, "Super Floor 2", f3, superRegionFactory, superPortalFactory);
        Floor f1 = new Floor(game, "Floor 1", f2, baseRegionFactory, basePortalFactory);

        game.setFloor1(f1);
        game.start();
    }
}
