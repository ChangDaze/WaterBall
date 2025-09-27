package Role;

import Map.GameMap;
import Map.MapObject;
import Map.Obstacle;

import java.util.*;

public class Hero extends Role{
    public Hero(int row, int column, GameMap gameMap){
        super(row, column, gameMap);
        setHp(300);
        setHpLimit(300);
    }

    @Override
    public String getSymbol(){
        return super.getDirection();
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

        String[] arrows = {"↓", "↑", "→", "←"};
        int index = Arrays.asList(arrows).indexOf(getDirection());

        int currentRow = getRow();
        int currentColumn = getColumn();
        while (true) {
            int newRow = currentRow + directions[index][0];
            int newColumn =  currentColumn + directions[index][1];

            if(!gameMap.validPoint(newRow, newColumn)) {
                break;
            }

            MapObject object = mapGrid[newRow][newColumn];

            if(object instanceof Obstacle) {
                break;
            }

            if(object instanceof Role role) {
                role.state.attacked(this);
            }
        }
    }

    @Override
    protected void doAtion(List<String> command, int[][] directions){

        System.out.println("請選擇動作：");
        for (int i = 0; i < command.size(); i++) {
            System.out.printf("%d: %s%n", i, command.get(i));
        }

        // 讀取使用者輸入
        Scanner scanner = new Scanner(System.in);
        System.out.print("輸入編號：");
        int choice = scanner.nextInt(); // 讀取使用者輸入數字

        if(command.get(choice).equals("attack")) {
            state.attack(getGameMap());
        }

        if(command.get(choice).equals("move")) {
            System.out.println("請選擇方向：");
            for (int i = 0; i < directions.length; i++) {
                System.out.printf("%d: [%d,%d]%n", i, directions[i][0], directions[i][1]);
            }

            System.out.print("輸入編號：");
            int choiceMove = scanner.nextInt();

            Map<String, String> map = new HashMap<>();
            map.put("1,0", "↓");
            map.put("-1,0", "↑");
            map.put("0,1", "→");
            map.put("0,-1", "←");

            String arrow = map.get(directions[choiceMove][0] + "," + directions[choiceMove][1]);
            state.move(getRow() + directions[choiceMove][0], getColumn() + directions[choiceMove][1],getGameMap(), arrow);
        }
    }
}
