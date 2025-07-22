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
            if(checkCard(currentCard, hand)) {
                getHands().remove(i);
                System.out.printf("%s show %s %d \r\n", getName(), hand.getColor().toString(), hand.getNumber());
                return hand;
            }
        }

        System.out.printf("%s not show card, draw a card \r\n", getName());
        return null;
    }

    protected boolean checkCard(UnoCard currentCard, UnoCard showCard) {
        return showCard.getColor() == currentCard.getColor() || showCard.getNumber() == currentCard.getNumber();
    }
}
