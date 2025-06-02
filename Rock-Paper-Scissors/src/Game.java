import java.util.Map;

public class Game {

    private final Player p1;
    private final Player p2;

    public  Game(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        if (p1.getNumber() == p2.getNumber()) {
            throw new IllegalStateException("Player number must be unique.");
        }
    }

    private final static Map<Decision, Decision> counterMap =
            Map.of(Decision.SCISSORS, Decision.PAPER,
                    Decision.PAPER, Decision.ROCK,
                    Decision.ROCK, Decision.SCISSORS);

    public void Start() {
//        var a = counterMap.get(Decision.ROCK);
//        var b = counterMap.get(Decision.PAPER);
//        var c = counterMap.get(Decision.SCISSORS);

        Decision p1Decision = p1.decide();
        Decision p2Decision = p2.decide();

        System.out.printf("玩家 #%d 出了 %s\n", p1.getNumber(), p1Decision);
        System.out.printf("玩家 #%d 出了 %s\n", p2.getNumber(), p2Decision);

        if (p1Decision == p2Decision) {
            System.out.println("平手 !");
        } else if (counterMap.get(p1Decision) == p2Decision) {
            System.out.printf("玩家 #%d 獲勝!\n", p1.getNumber());
        } else {
            System.out.printf("玩家 #%d 獲勝!\n", p2.getNumber());
        }
    }
}
