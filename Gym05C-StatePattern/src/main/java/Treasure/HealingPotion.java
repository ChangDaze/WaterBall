package Treasure;

import Map.GameMap;
import Role.*;

public class HealingPotion extends Treasure{
    public HealingPotion(int row, int column, GameMap gameMap){
        super(row, column, gameMap);
    }

    @Override
    public void touched(Role from) {
        super.touched(from);
        from.setState(new Healing(from, from.getGameMap().getRound()));
        System.out.printf("%s enter Healing", from.getSymbol());
    }
}
