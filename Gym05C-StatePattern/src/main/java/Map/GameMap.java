package Map;

import Treasure.*;
import Role.*;
import lombok.Data;

import java.util.*;

@Data
public class GameMap {
    private MapObject[][] mapGrid;
    private int round;
    private List<Role> roles;

    public void createMap(int row, int column, int monsterCount, int heroCount, int treasureCount, LinkedHashMap<Class<? extends Treasure>, Double> treasureProbability) {
        if( row * column < monsterCount + treasureCount + 1) {
            throw new RuntimeException("地圖太小無法放入設定的物件數量");
        }

        mapGrid = new MapObject[row][column];
        round = 0;
        roles = new ArrayList<>();
        Random random = new Random();

        int existMonster = 0;
        while (existMonster < monsterCount) {
            int tempRow = random.nextInt(row);
            int tempColumn = random.nextInt(column);

            if(mapGrid[tempRow][tempColumn] != null) {
                continue;
            }

            Monster monster = new Monster(tempRow, tempColumn, this);
            mapGrid[tempRow][tempColumn] = monster;
            roles.add(monster);

            existMonster++;
        }

        int existTreasure = 0;
        while (existTreasure < treasureCount) {
            int tempRow = random.nextInt(row);
            int tempColumn = random.nextInt(column);

            if(mapGrid[tempRow][tempColumn] != null) {
                continue;
            }

            mapGrid[tempRow][tempColumn] = treasureProbability
                    .entrySet()
                    .stream()
                    .filter(entry -> random.nextDouble() <= entry.getValue())
                    .findFirst()
                    .map(entry -> {
                        try {
                            return entry.getKey()
                                    .getConstructor(int.class, int.class, GameMap.class)
                                    .newInstance(tempRow, tempColumn, this);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElseThrow(() -> new RuntimeException("treasureProbability error"));

            existTreasure++;
        }

        int existHero = 0;
        while (existHero < heroCount) {
            int tempRow = random.nextInt(row);
            int tempColumn = random.nextInt(column);

            if(mapGrid[tempRow][tempColumn] != null) {
                continue;
            }

            Hero hero = new Hero(tempRow, tempColumn, this);
            mapGrid[tempRow][tempColumn] = hero;
            roles.add(hero);

            existHero++;
        }
    }

    public void printMap(){
        for(MapObject[] row : mapGrid){
            StringBuilder sb = new StringBuilder();
            for (MapObject point : row) {
                if (point == null) {
                    sb.append(".");
                }
                else {
                    sb.append(point.getSymbol());
                }
            }
            System.out.println(sb.toString());
        }
    }

    public boolean validPoint(int row, int column) {
        if (row >=0 && row < mapGrid.length &&
                column >= 0 && column < mapGrid.length) {
            return true;
        }

        return false;
    }

    public void start() {
        Boolean gameOverFlag = false;
        while (!gameOverFlag) {
            System.out.println("RoundStart Time");
            printMap();
            Iterator<Role> roundStarts = roles.iterator();
            while (roundStarts.hasNext()) {
                Role role = roundStarts.next();
                role.roundStart();
            }

            gameOverFlag = gameOver();
            if(gameOverFlag) {
                break;
            }

            System.out.println("Action Time");
            printMap();
            Iterator<Role> actions = roles.iterator();
            while (actions.hasNext()) {
                Role role = actions.next();
                role.action();
                gameOverFlag = gameOver();
                if(gameOverFlag) {
                    break;
                }
            }
        }
    }

    public boolean gameOver() {
        int heroCount = 0;
        int monsterCount = 0;
        for (Role role : roles) {
            if (role instanceof Hero) {
                heroCount++;
            }
            else if(role instanceof Monster) {
                monsterCount++;
            }
        }

        if (heroCount == 0) {
            System.out.println("heroCount == 0 gameover ");
            return true;
        }

        if (monsterCount == 0) {
            System.out.println("monsterCount == 0 gameover ");
            return true;
        }

        return false;
    }
}
