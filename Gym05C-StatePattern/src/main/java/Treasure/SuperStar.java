package Treasure;

import Map.GameMap;
import Role.*;

public class SuperStar extends Treasure{
    public SuperStar(int row, int column, GameMap gameMap){
        super(row, column, gameMap);
    }

    @Override
    public void touched(Role from) {
        super.touched(from);
        from.setState(new Invincible(from, from.getGameMap().getRound()));
        System.out.printf("%s enter Invincible", from.getSymbol());
    }
}
