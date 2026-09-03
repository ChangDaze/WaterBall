package Game.Pocker;

import Game.Card;
import Game.Player;

import java.util.List;
import java.util.Random;

public class HumanPockerPlayer extends Player {
    private static int count = 0;
    private final Random random = new Random();

    public HumanPockerPlayer() {
    }

    public HumanPockerPlayer(String name) {
        this.name = name;
    }

    @Override
    public void nameHimself() {
        if (this.name == null || this.name.isEmpty()) {
            this.name = "HumanPockerPlayer" + (++count);
        }
    }

    @Override
    public Card show(List<Card> shows) {
        if (hands == null || hands.isEmpty()) {
            return null;
        }
        int index = random.nextInt(hands.size());
        Card card = hands.remove(index);
        System.out.println(this.name + " shows " + card);
        return card;
    }
}
