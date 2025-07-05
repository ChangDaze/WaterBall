package CardGame;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Player<C> {
    private List<C> hands = new ArrayList<>();
    private String name;

    // <editor-fold desc="getter & setter">
    public String getName() {
        return name;
    }

    public List<C> getHands() {
        return hands;
    }
    // </editor-fold>

    /**
    player 命名
     **/
    public void nameHimself() {
        UUID guid = UUID.randomUUID();
        name = guid.toString();
    }

    /**
    出牌
     **/
    public C show(C currentCard){
        var card = getHands().getLast();
        getHands().removeLast();
        return card;
    }

    /**
    加入手牌
     **/
    public void addHand(C card) {
        getHands().add(card);
    }
}
