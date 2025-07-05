package Uno;

import CardGame.Player;

public class UnoPlayer  extends Player<UnoCard> {

    @Override
    public UnoCard show(UnoCard currentCard){
        if (currentCard == null) {
            var card = getHands().getLast();
            getHands().removeLast();
            System.out.printf("%s show %s %d \r\n", getName(), card.getColor().toString(), card.getNumber());
            return card;
        }

        for (int i = 0; i < getHands().size(); i++) {
            var hand = getHands().get(i);
            if(hand.getColor() == currentCard.getColor() || hand.getNumber() == currentCard.getNumber()) {
                getHands().remove(i);
                System.out.printf("%s show %s %d \r\n", getName(), hand.getColor().toString(), hand.getNumber());
                return hand;
            }
        }

        System.out.printf("%s not show card, draw a card \r\n", getName());
        return null;
    }
}
