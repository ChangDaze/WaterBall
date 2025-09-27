package Role;

import Map.GameMap;
import Map.MapObject;

import java.util.List;
import java.util.Random;

public class Monster extends Role{
    public Monster(int row, int column, GameMap gameMap){
        super(row, column, gameMap);
    }

    @Override
    public String getSymbol(){
        return "M";
    }

    @Override
    public void normalAttack(GameMap gameMap) {
        MapObject[][] mapGrid = gameMap.getMapGrid();

        int[][] directions = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };
        for (int[] forward : directions) {
            int newRow = this.getRow() + forward[0];
            int newColumn =  this.getColumn() + forward[1];
            if (gameMap.validPoint(newRow, newColumn)) {
                MapObject object = mapGrid[newRow][newColumn];

                if(object instanceof Role role) {
                    role.state.attacked(this);
                }
            }
        }
    }

    @Override
    protected void doAtion(List<String> command, int[][] directions){
        GameMap gameMap = getGameMap();
        MapObject[][] mapGrid = gameMap.getMapGrid();
        int currentRow = getRow();
        int currentColumn = getColumn();

        boolean doAttack = false;

        if(command.contains("attack")) {
            for (int[] forward : directions) {
                int newRow = currentRow + forward[0];
                int newColumn =  currentColumn + forward[1];
                if (gameMap.validPoint(newRow, newColumn)) {
                    MapObject object = mapGrid[newRow][newColumn];

                    if(object instanceof Hero) {
                        state.attack(gameMap);
                        doAttack = true;
                    }
                    break;
                }
            }
        }

        if(command.contains("move")) {
            if(!doAttack) {
                Random random = new Random();
                int n = random.nextInt(4);
                state.move(currentRow + directions[n][0], currentColumn + directions[n][1], gameMap, "↑");
            }
        }
    }
}
