import Battle.Battle;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Battle battle = Battle.builder().troops(new ArrayList<>()).round(0).build();
        battle.createBattle();
        battle.startBattle();
    }
}
