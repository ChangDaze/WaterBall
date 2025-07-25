import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class World {
    private Map<Integer, Sprite> map = new HashMap<>();
    private CollisionHandler handler;

    public World(CollisionHandler handler) {
        this.handler = handler;
    }

    public void start(){
        //建立生命
        map.put(1, new Sprite("H"));
        map.put(2, new Sprite("W"));
        map.put(3, new Sprite("F"));
        map.put(4, new Sprite("H"));
        map.put(5, new Sprite("W"));
        map.put(6, new Sprite("F"));
        map.put(7, new Sprite("H"));
        map.put(8, new Sprite("W"));
        map.put(9, new Sprite("F"));
        map.put(10, new Sprite("F"));

        Scanner scanner = new Scanner(System.in);
        System.out.println("玩家進入世界");

        while (true) {
            System.out.println("請輸入");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] locations = input.split(" ");

            int l1 = Integer.parseInt(locations[0]);
            int l2 = Integer.parseInt(locations[1]);

            var c1 = map.get(l1);
            var c2 = map.get(l2);

            boolean moveResult = false;

            if(c1 != null && c2 != null) {
                moveResult = handler.collision(c1, c2);

                if (c1.getHp() <= 0) {

                    map.remove(l1);
                    if (c1.getType().equals("H")){
                        System.out.println("Hero 被世界移除");
                    }
                }

                if (c2.getHp() <= 0) {
                    map.remove(l2);
                    if (c2.getType().equals("H")){
                        System.out.println("Hero 被世界移除");
                    }
                }
            }
            else if (c1 != null && c2 == null) {
                moveResult = true;
            }

            if(moveResult && c1.getHp() > 0) {
                System.out.println("移動成功");
                map.remove(l1);
                map.put(l2, c1);
            } else {
                System.out.println("移動失敗");
            }
        }
        scanner.close();
        System.out.println("玩家離開世界");
    }
}
