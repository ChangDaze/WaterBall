package Treasure;

import Map.GameMap;
import Map.MapObject;

public abstract class Treasure extends MapObject {
    public Treasure(int row, int column, GameMap gameMap){
        super(row, column, gameMap);
    }

    @Override
    public String getType(){
        return "Treasure";
    }

    @Override
    public String getSymbol(){
        return "x";
    }
}
