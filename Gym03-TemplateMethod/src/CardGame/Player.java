package CardGame;

import Showdown.ShowdownCard;
import Showdown.ShowdownDeck;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Player<C> {
    private List<C> hand = new ArrayList<>();
    private String name;

    // <editor-fold desc="getter & setter">
    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public List<C> getHand() {
        return hand;
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
        var card = getHand().getLast();
        getHand().removeLast();
        return card;
    }

    /**
    加入手牌
     **/
    public void addHand(C card) {
        getHand().add(card);
    }
}
