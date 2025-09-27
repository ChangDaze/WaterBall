package Map;

import Role.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MapObject {
    private int row;
    private int column;
    private GameMap gameMap;

    public MapObject(int row, int column, GameMap gameMap) {
        this.row = row;
        this.column = column;
        this.gameMap = gameMap;
    }

    public void touched(Role from) {
         System.out.printf("%s be touched by %s", this.getClass().getName(), from.getClass().getName());
    }

    public void disappear() {
        gameMap.getMapGrid()[row][column] = null;
    }

    public abstract String getType();

    public abstract String getSymbol();

}
