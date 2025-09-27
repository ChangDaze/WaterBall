package Role;

import Map.GameMap;
import Map.MapObject;
import Treasure.Treasure;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class Role extends MapObject {
    private int hp = 1;
    private int hpLimit = 1;
    private String direction = "↑";
    protected State state = new Normal(this, 0);

    public Role(int row, int column, GameMap gameMap){
        super(row, column, gameMap);
    }

    @Override
    public String getType(){
        return "Role";
    }

    @Override
    public void disappear() {
        super.disappear();
        getGameMap().getRoles().remove(this);
    }

    public void roundStart() {
        state.roundStart();
    }

    public void action() {
        state.action();
    }

    protected abstract void normalAttack(GameMap gameMap);

    protected abstract void doAtion(List<String> command, int[][] directions);
}
