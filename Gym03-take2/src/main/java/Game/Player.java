package Game;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    protected List<Card> hands = new ArrayList<>();
    protected String name;

    public abstract void nameHimself();

    public void addHand(Card card){
        hands.add(card);
    }

    public abstract Card show(List<Card> shows);

    public String getName() {
        return name;
    }

    public List<Card> getHands() {
        return hands;
    }
}
