package Treasure;

import Map.GameMap;
import Role.*;

public class KingRock extends Treasure{
    public KingRock(int row, int column, GameMap gameMap){
        super(row, column, gameMap);
    }

    @Override
    public void touched(Role from) {
        super.touched(from);
        from.setState(new Stockpile(from, from.getGameMap().getRound()));
        System.out.printf("%s enter Stockpile", from.getSymbol());
    }
}
