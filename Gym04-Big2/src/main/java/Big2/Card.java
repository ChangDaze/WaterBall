package Big2;

import java.util.Objects;

public class Card  implements Comparable<Card> {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return suit.getShortName() + "[" + rank.getDisplayName() + "]";
    }

    public boolean sameCard(Suit suit, Rank rank) {
        return this.suit == suit && this.rank == rank;
    }

    /**
     * if(result > 0) {
     *     System.out.println("card1 大於 card2");
     * } else if(result < 0) {
     *     System.out.println("card1 小於 card2");
     * } else {
     *     System.out.println("card1 等於 card2");
     * }
     */
    @Override
    public int compareTo(Card other) {
        // 先比 Rank（點數）
        int rankCompare = Integer.compare(this.rank.getValue(), other.rank.getValue());
        if (rankCompare != 0) {
            return rankCompare;
        }
        // Rank 相同再比 Suit（花色）
        return Integer.compare(this.suit.getValue(), other.suit.getValue());
    }

    /**
     * 正確覆寫了 equals() 和 hashCode() 方法 才能使用removeAll
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card other = (Card) o;
        return this.rank == other.rank && this.suit == other.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}

