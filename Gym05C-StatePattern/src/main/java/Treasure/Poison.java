package Treasure;

import Map.GameMap;
import Role.*;

public class Poison extends Treasure{
    public Poison(int row, int column, GameMap gameMap){
        super(row, column, gameMap);
    }

    @Override
    public void touched(Role from) {
        super.touched(from);
        from.setState(new Poisoned(from, from.getGameMap().getRound()));
        System.out.printf("%s enter Poisoned", from.getSymbol());
    }
}
