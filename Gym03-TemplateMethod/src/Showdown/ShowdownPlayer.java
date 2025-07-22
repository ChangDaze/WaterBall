package Showdown;

import CardGame.Deck;
import CardGame.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ShowdownPlayer extends Player<ShowdownCard> {
    protected int point = 0;

    @Override
    public ShowdownCard show(ShowdownCard currentCard){
        var card = super.show(currentCard);
        System.out.printf("%s show %s %s \r\n", getName(), card.getSuit().toString(), card.getRank().toString());

        return card;
    }

    public void addPoint(){
        point++;
    }

    public int getPoint() {
        return point;
    }
}
