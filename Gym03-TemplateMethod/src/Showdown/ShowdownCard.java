package Showdown;

public class ShowdownCard {
    private final Rank rank;
    private final Suit suit;

    public ShowdownCard(Rank rank, Suit suit){
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }
}
