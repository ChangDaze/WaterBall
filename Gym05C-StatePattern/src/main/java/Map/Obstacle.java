package Map;

public class Obstacle extends MapObject{
    public Obstacle(int row, int column, GameMap gameMap){
        super(row, column, gameMap);
    }

    @Override
    public String getType(){
        return "Obstacle";
    }

    @Override
    public String getSymbol(){
        return "□";
    }
}
