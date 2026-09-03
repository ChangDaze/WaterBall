package Game.Uno;

import Game.Card;
import Game.Player;

import java.util.List;

public class AIUnoPlayer extends Player {
    private static int count = 0;

    public AIUnoPlayer() {
    }

    public AIUnoPlayer(String name) {
        this.name = name;
    }

    @Override
    public void nameHimself() {
        if (this.name == null || this.name.isEmpty()) {
            this.name = "AIUnoPlayer" + (++count);
        }
    }

    @Override
    public Card show(List<Card> shows) {
        if (hands == null || hands.isEmpty()) {
            return null;
        }

        if (shows == null || shows.isEmpty()) {
            Card card = hands.removeFirst();
            System.out.println(this.name + " shows " + card);
            return card;
        }

        UnoCard topCard = (UnoCard) shows.getLast();
        for (int i = 0; i < hands.size(); i++) {
            if (hands.get(i) instanceof UnoCard card) {
                if (card.getColor().equals(topCard.getColor()) || card.getNumber() == topCard.getNumber()) {
                    hands.remove(i);
                    System.out.println(this.name + " shows " + card);
                    return card;
                }
            }
        }

        return null;
    }
}
