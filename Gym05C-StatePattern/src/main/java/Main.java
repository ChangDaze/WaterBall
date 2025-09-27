import Map.GameMap;
import Treasure.*;

import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) {
        LinkedHashMap<Class<? extends Treasure>, Double> treasureProbability = new LinkedHashMap<>();
        treasureProbability.put(SuperStar.class, 0.1);
        treasureProbability.put(Poison.class, 0.35); //0.25
        treasureProbability.put(AcceleratingPotion.class, 0.55); //0.2
        treasureProbability.put(HealingPotion.class, 0.7); //0.15
        treasureProbability.put(DevilFruit.class, 0.8); //0.1
        treasureProbability.put(KingRock.class, 0.9);
        treasureProbability.put(DokodemoDoor.class, 1.0); //0.1

        GameMap gameMap = new GameMap();
        gameMap.createMap(2, 2, 1, 1, 1, treasureProbability);

        gameMap.start();
    }
}
