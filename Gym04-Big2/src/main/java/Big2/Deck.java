package Big2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck(Card[] cardsArray) {
        this.cards = new ArrayList<>();
        Collections.addAll(this.cards, cardsArray);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card dealOneCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.removeLast(); //從最後一張開始抽
    }

    public int remainingCards() {
        return cards.size();
    }

    public void reset(Card[] newCards) {
        cards.clear();
        Collections.addAll(cards, newCards);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}

