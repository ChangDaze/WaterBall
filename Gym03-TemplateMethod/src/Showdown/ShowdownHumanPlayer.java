package Showdown;

import java.util.Scanner;
import java.util.UUID;

public class ShowdownHumanPlayer extends ShowdownPlayer{
    private final static Scanner in = new Scanner(System.in);

    @Override
    public void nameHimself() {
        System.out.println("請命名:");
        setName(in.next());
    }

    @Override
    public ShowdownCard show(ShowdownCard currentCard){
        System.out.println("請出:");
        var cards = getHands();
        for (int i = 0; i < cards.size(); i++) {
            System.out.printf("(%d) %s %s \n", i, cards.get(i).getSuit().toString(), cards.get(i).getRank().toString());
        }

        int num = in.nextInt();

        var card = cards.get(num);
        cards.remove(num);
        System.out.printf("%s show %s %s \r\n", getName(), card.getSuit().toString(), card.getRank().toString());

        return card;
    }
}
