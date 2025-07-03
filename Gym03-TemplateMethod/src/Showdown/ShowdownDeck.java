package Showdown;

import java.util.ArrayList;
import java.util.List;

public class ShowdownDeck {
    private List<ShowdownCard> cards = new ArrayList<>();

    public ShowdownDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new ShowdownCard(rank, suit));
            }
        }
    }

    public void shuffle() {

    }

    public ShowdownCard drawCard() {
        var card = cards.getLast();
        return cards.removeLast();
    }

    public void addCard(ShowdownCard card) {
        cards.add(card);
    }
}
