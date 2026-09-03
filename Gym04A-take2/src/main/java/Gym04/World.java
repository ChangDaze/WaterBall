package Gym04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class World {
    private Sprite[] maps = new Sprite[30];
    private CollisionHandler handler;

    public World(CollisionHandler handler) {
        this.handler = handler;
    }

    public void start() {
        System.out.println("==================================================");
        System.out.println("             WORLD SIMULATION START               ");
        System.out.println("==================================================");

        initSprites();

        System.out.println("\n[Initial World Map]");
        printMap();

        Random random = new Random();

        //因為目前只設計移動一格，如果相同屬性相鄰遊戲就會因為無動作無限迴圈，所以不能跑無限次
        for (int round = 1; round <= 30; round++) {
            List<Integer> existingIndices = getExistingSpriteIndices();
            if (existingIndices.isEmpty()) {
                System.out.println("\nAll sprites have been eliminated.");
                break;
            }

            System.out.println("\n--------------------------------------------------");
            System.out.println(" Move " + round + " / 30 (Alive Sprites: " + existingIndices.size() + ")");
            System.out.println("--------------------------------------------------");
            printMap();

            int c1 = existingIndices.get(random.nextInt(existingIndices.size()));
            int direction;
            if (c1 == 0) {
                direction = 1;
            } else if (c1 == maps.length - 1) {
                direction = -1;
            } else {
                direction = random.nextBoolean() ? 1 : -1;
            }

            int c2 = c1 + direction;
            System.out.println("[Move " + round + "] " + maps[c1] + " at [" + c1 + "] chooses to move to [" + c2 + "] (Target: " + (maps[c2] == null ? "Empty" : maps[c2]) + ")");

            move(c1, c2);
        }

        System.out.println("\n==================================================");
        System.out.println("                   GAME OVER                      ");
        System.out.println("==================================================");
        System.out.println("[Final World Map]");
        printMap();

        List<Integer> remainingIndices = getExistingSpriteIndices();
        if (remainingIndices.isEmpty()) {
            System.out.println("Result: No sprites survived.");
        } else if (remainingIndices.size() == 1) {
            int winnerPos = remainingIndices.get(0);
            System.out.println("Result: Last surviving sprite is " + maps[winnerPos] + " at position [" + winnerPos + "]!");
        } else {
            System.out.println("Result: Simulation completed after 30 moves. " + remainingIndices.size() + " sprites survived:");
            for (int pos : remainingIndices) {
                System.out.println("  - " + maps[pos] + " at position [" + pos + "]");
            }
        }
    }

    /**
     * 初始化 10 個 Sprite
     */
    private void initSprites() {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < maps.length; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);

        Random random = new Random();
        System.out.println("[Init] Spawning 10 random sprites onto map:");
        for (int i = 0; i < 10; i++) {
            int pos = positions.get(i);
            int type = random.nextInt(3);
            Sprite sprite;
            switch (type) {
                case 0:
                    sprite = new Hero(pos, this);
                    break;
                case 1:
                    sprite = new Water(pos, this);
                    break;
                case 2:
                default:
                    sprite = new Fire(pos, this);
                    break;
            }
            maps[pos] = sprite;
            System.out.println("  - Placed " + sprite + " at position [" + pos + "]");
        }
    }

    /**
     * 數還存在的 Sprite 數量
     */
    public int countSprites() {
        int count = 0;
        for (Sprite sprite : maps) {
            if (sprite != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * 取得存活 Sprite 的位置列表
     */
    private List<Integer> getExistingSpriteIndices() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < maps.length; i++) {
            if (maps[i] != null) {
                indices.add(i);
            }
        }
        return indices;
    }

    /**
     * 印出易讀的地圖
     */
    public void printMap() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maps.length; i++) {
            String symbol = (maps[i] == null) ? "." : maps[i].getSymbol();
            sb.append(String.format("[%02d:%-6s] ", i, symbol));
            if ((i + 1) % 10 == 0) {
                sb.append("\n");
            }
        }
        System.out.print(sb.toString());
    }

    public void move(int c1, int c2) {
        handler.collision(maps[c1], maps[c2], c2);
    }

    public Sprite[] getMaps() {
        return maps;
    }
}
