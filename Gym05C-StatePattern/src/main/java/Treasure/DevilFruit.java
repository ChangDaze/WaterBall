package Treasure;

import Map.GameMap;
import Role.*;

public class DevilFruit extends Treasure{
    public DevilFruit(int row, int column, GameMap gameMap){
        super(row, column, gameMap);
    }

    @Override
    public void touched(Role from) {
        super.touched(from);
        from.setState(new Orderless(from, from.getGameMap().getRound()));
        System.out.printf("%s enter Orderless", from.getSymbol());
    }
}
