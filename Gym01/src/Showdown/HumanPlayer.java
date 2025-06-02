package Showdown;

import java.util.List;
import java.util.Scanner;

public class HumanPlayer extends Player  {
    private final static Scanner in = new Scanner(System.in);
    public HumanPlayer() {
        super();
    }

    @Override
    public void nameHimself() {
        System.out.println("請命名:");
        this.name = in.next();
    }

    @Override
    public Card show() {
        System.out.println("請出:");
        for (int i = 0; i < cards.size(); i++) {
            System.out.printf("(%d) rank : %d, suit : %d\n", i, cards.get(i).getRank(), cards.get(i).getSuit());
        }

        int num = in.nextInt();

        Card showCard = this.cards.get(num);
        cards.remove(num);
        return showCard;
    }

    @Override
    public void exchangeHands(List<Player> players, int turns) {
        if (this.switchStartTurn == 0) { //未使用換牌

            System.out.println("是否換牌 (Y) 、(N) :");
            String change = in.next();

            if (!change.equals("Y")) {
                return;
            }

            System.out.println("請選擇換牌對象:");
            int num = in.nextInt();

            this.switchStartTurn = turns;
            this.switchPlayer = players.get(num);
            List<Card> temp = this.cards;
            this.cards = switchPlayer.cards;
            switchPlayer.cards = temp;
        }
        else if (this.switchPlayer == null) { //已使用過換牌
            return;
        }
        else  { //換牌中
            if (turns - this.switchStartTurn >= 3){
                System.out.println("換牌結束");
                List<Card> temp = this.cards;
                this.cards = switchPlayer.cards;
                switchPlayer.cards = temp;

                this.switchPlayer = null;
            }
        }

    }
}
