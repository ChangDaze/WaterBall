package Role;

import Map.GameMap;
import Map.MapObject;

import java.util.Random;

public class Teleport extends State{
    public Teleport(Role role, int enterStateRound) {
        super(role, enterStateRound);
    }

    @Override
    protected void roundStart() {
        GameMap gameMap = role.getGameMap();
        MapObject[][] mapGrid = gameMap.getMapGrid();
        Random random = new Random();

        while (true) {
            int tempRow = random.nextInt(mapGrid.length);
            int tempColumn = random.nextInt(mapGrid[0].length);

            if(mapGrid[tempRow][tempColumn] != null) {
                continue;
            }

            super.move(tempRow, tempColumn, gameMap, "↑");
            System.out.printf("%s is Teleported \n", role.getSymbol());
            break;
        }

        if(role.getGameMap().getRound() - enterStateRound > 1) {
            role.setState(new Normal(role, role.getGameMap().getRound()));
        }
    }
}
