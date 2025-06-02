package Showdown;

import java.util.List;

public class AIPlayer extends Player {
    public AIPlayer() {
        super();
    }

    @Override
    public void nameHimself() {
        this.name = this.getUniqueID().toString();
    }

    @Override
    public Card show() {
        Card showCard = this.cards.get(0);
        cards.remove(0);
        return showCard;
    }

    @Override
    public void exchangeHands(List<Player> players, int turns) {
    }
}
