package Game.Pocker;

import Game.Card;

public class PockerCard implements Card, Comparable<PockerCard> {
    private int rank;
    private String rankFace;
    private int suit;
    private String suitFace;

    public PockerCard(int rank, String rankFace, int suit, String suitFace) {
        this.rank = rank;
        this.rankFace = rankFace;
        this.suit = suit;
        this.suitFace = suitFace;
    }

    public int getRank() {
        return rank;
    }

    public String getRankFace() {
        return rankFace;
    }

    public int getSuit() {
        return suit;
    }

    public String getSuitFace() {
        return suitFace;
    }

    @Override
    public int compareTo(PockerCard other) {
        if (other == null) {
            return 1;
        }
        if (this.rank != other.rank) {
            return Integer.compare(this.rank, other.rank);
        }
        return Integer.compare(this.suit, other.suit);
    }

    @Override
    public String toString() {
        return suitFace + " " + rankFace;
    }
}
