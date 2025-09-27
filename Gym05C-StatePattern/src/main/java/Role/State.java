package Role;

import Map.GameMap;
import Map.MapObject;
import Treasure.Treasure;

import java.util.List;
import java.util.Random;

public abstract class State {
    protected Role role;
    protected int enterStateRound;

    public State(Role role, int enterStateRound) {
        this.role = role;
        this.enterStateRound = enterStateRound;
    }

    protected void roundStart() {

    }

    protected void attack(GameMap gameMap){
        System.out.printf("%s do attack \n", role.getSymbol());
        role.normalAttack(gameMap);
    }

    protected void attacked(Role from) {
        System.out.printf("%s be attacked \n", role.getSymbol());
        role.setHp(role.getHp() - 50);
        if(role.getHp() < 0) {
            System.out.printf("%s do disappear \n", role.getSymbol());
            role.disappear();
        }
    }

    protected void move(int row, int column, GameMap gameMap, String direction) {
        role.setDirection(direction);

        System.out.printf("%s do move \n", role.getSymbol());

        if(!gameMap.validPoint(row, column)) {
            System.out.printf("%s move fail - out of bound\n", role.getSymbol());
        }

        MapObject[][] mapGrid = gameMap.getMapGrid();
        MapObject object = mapGrid[row][column];

        if(object == null) {
            mapGrid[row][column] = role;
            mapGrid[role.getRow()][role.getColumn()] = null;
        }
        else {
            object.touched(role);
        }
    }

    protected void action() {
        System.out.printf("%s do action start \n", role.getSymbol());

        List<String> actions = List.of("attack", "move");
        int[][] directions = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        role.doAtion(actions, directions);

        System.out.printf("%s do action end \n", role.getSymbol());
    }
}
