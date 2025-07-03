package CardGame;

import Showdown.ShowdownCard;
import Showdown.ShowdownDeck;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Player<C> {
    protected String name;
    protected List<C> hand;

    /*
    player 命名
     */
    protected void nameHimself() {
        UUID guid = UUID.randomUUID();
        name = guid.toString();
    }

    /*
    出牌
     */
    protected C show(C currentCard){
        var card = hand.getLast();
        hand.removeLast();
        return card;
    }

    /*
    加入手牌
     */
    protected void addHand(C card) {
        hand.add(card);
    }

    public String getName() {
        return name;
    }
}
