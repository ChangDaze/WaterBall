package CardGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Deck<C> {
    protected List<C> cards = new ArrayList<>();

    /*
    洗牌
     */
    protected void shuffle() {
        Collections.shuffle(cards);
    }

    /*
    抽出一張牌
     */
    protected C drawCard() {
        var card = cards.getLast();
        return cards.removeLast();
    }

    /*
    放入一張牌
     */
    protected void addCard(C card) {
        cards.add(card);
    }

    /*
    放入新牌堆
     */
    protected void setCards(List<C> newCards) {
        cards = newCards;
    }
}
