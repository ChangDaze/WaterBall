package Uno;

import java.util.Scanner;

public class UnoHumanPlayer extends UnoPlayer{
    private final static Scanner in = new Scanner(System.in);

    @Override
    public void nameHimself() {
        System.out.println("請命名:");
        setName(in.next());
    }

    @Override
    public UnoCard show(UnoCard currentCard){
        System.out.printf("最新的牌 %s %d \n", currentCard.getColor().toString(), currentCard.getNumber());

        System.out.println("請出:");
        var cards = getHands();
        for (int i = 0; i < cards.size(); i++) {
            System.out.printf("(%d) %s %d \n", i, cards.get(i).getColor().toString(), cards.get(i).getNumber());
        }

        int num = in.nextInt();
        UnoCard showCard;
        if( num >= 0 && num < cards.size()) {
            showCard = cards.get(num);
            if(checkCard(currentCard, showCard)) {
                cards.remove(showCard);
                System.out.printf("%s show %s %d \r\n", getName(), showCard.getColor().toString(), showCard.getNumber());
                return showCard;
            }
            //不合法要另外處理
            throw new RuntimeException("不合法的牌： " + showCard.getColor().toString() + " " + showCard.getNumber());
        }
        else {
            //有牌就強制出牌
            for (int i = 0; i < cards.size(); i++) {
                var hand = cards.get(i);
                if(checkCard(currentCard, hand)) {
                    cards.remove(i);
                    System.out.printf("%s show %s %d \r\n", getName(), hand.getColor().toString(), hand.getNumber());
                    return hand;
                }
            }
        }

        System.out.printf("%s not show showCard, draw a showCard \r\n", getName());
        return null;
    }
}
