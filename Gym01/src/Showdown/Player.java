package Showdown;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Player {

    private final UUID uniqueID;
    protected String name;
    protected Player switchPlayer;
    protected int switchStartTurn = 0;
    protected List<Card> cards = new ArrayList<>();
    private int score;

    public Player() {
        this.uniqueID = UUID.randomUUID();
    }

    public abstract void nameHimself();

    public abstract Card show();

    public abstract void exchangeHands(List<Player> players, int turns);

    public UUID getUniqueID() {
        return uniqueID;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addScore() {
        this.score++;
    }

    public void takeCard(Card card) {
        this.cards.add(card);
    }
}
