package Showdown;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ShowdownPlayer {
    private String name;
    private List<ShowdownCard> hand;
    private int point=0;

    public ShowdownPlayer() {
        hand = new ArrayList<>();
    }

    public void nameHimself() {
        try {
            Thread.sleep(100); // 每次暫停 1 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        name = now.format(formatter);
    }

    public ShowdownCard show(){
        var card = hand.getLast();
        hand.removeLast();

        System.out.printf("%s show %s %s \r\n", name, card.getSuit().toString(), card.getRank().toString());

        return card;
    }

    public void drawCard(ShowdownDeck deck) {
        hand.add(deck.drawCard());
    }

    public void addPoint(){
        point++;
    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }
}
