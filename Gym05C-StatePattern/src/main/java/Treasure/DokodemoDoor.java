package Treasure;

import Map.GameMap;
import Role.*;

public class DokodemoDoor extends Treasure{
    public DokodemoDoor(int row, int column, GameMap gameMap){
        super(row, column, gameMap);
    }

    @Override
    public void touched(Role from) {
        super.touched(from);
        from.setState(new Teleport(from, from.getGameMap().getRound()));
        System.out.printf("%s enter Teleport", from.getSymbol());
    }
}
