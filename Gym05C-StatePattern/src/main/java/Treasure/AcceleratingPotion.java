package Treasure;

import Map.GameMap;
import Role.*;

public class AcceleratingPotion extends Treasure{
    public AcceleratingPotion(int row, int column, GameMap gameMap){
        super(row, column, gameMap);
    }

    @Override
    public void touched(Role from) {
        super.touched(from);
        from.setState(new Accelerated(from, from.getGameMap().getRound()));
        System.out.printf("%s enter Accelerated", from.getSymbol());
    }
}
