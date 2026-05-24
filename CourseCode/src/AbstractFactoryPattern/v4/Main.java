package AbstractFactoryPattern.v4;

import AbstractFactoryPattern.v4.defaults.BaseFloorAbstractFactory;
import AbstractFactoryPattern.v4.factories.FloorAbstractFactory;
import AbstractFactoryPattern.v4.thirdparty.SuperFloorAbstractFactory;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        FloorAbstractFactory baseFactory = new BaseFloorAbstractFactory();
        FloorAbstractFactory superFactory = new SuperFloorAbstractFactory();

        TopFloor topFloor = new TopFloor(game, baseFactory);
        Floor f3 = new Floor(game, "Super Floor 3", topFloor, superFactory);
        Floor f2 = new Floor(game, "Super Floor 2", f3, superFactory);
        Floor f1 = new Floor(game, "Floor 1", f2, baseFactory);

        game.setFloor1(f1);
        game.start();
    }
}
