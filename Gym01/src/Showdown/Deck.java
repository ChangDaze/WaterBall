package Showdown;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards;

    public Deck(){
        this.shuffle();
    }

    public void shuffle() {
        int[] suits = {1, 2, 3, 4};
        int[] ranks = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

        List<Card> newCards = new ArrayList<>();

        for (int suit : suits) {
            for (int rank : ranks) {
                Card card = new Card(rank, suit);
                newCards.add(card);
            }
        }

        Collections.shuffle(newCards);
        this.cards = newCards;
    }

    public Card drawCard() {
        Card card = this.cards.getFirst();
        this.cards.removeFirst();
        return card;
    }
}
