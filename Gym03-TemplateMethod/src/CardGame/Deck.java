package CardGame;

import Showdown.ShowdownCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Deck<C> {
    private List<C> cards = new ArrayList<>();

    /*
    洗牌
     */
    public void shuffle() {
        Collections.shuffle(this.getCards());
    }

    /*
    抽出一張牌
     */
    public C drawCard() {
        var card = this.getCards().getLast();
        return this.getCards().removeLast();
    }

    /*
    放入一張牌
     */
    public void addCard(C card) {
        this.getCards().add(card);
    }

    /*
    取得牌堆
     */
    public List<C> getCards() {
        return cards;
    }

    /*
    放入新牌堆
     */
    public void setCards(List<C> newCards) {
        cards = newCards;
    }
}
