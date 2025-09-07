package Big2;

public enum Suit {
    CLUBS(0, "C"),
    DIAMONDS(1, "D"),
    HEARTS(2, "H"),
    SPADES(3, "S");

    private final int value;
    private final String shortName;

    Suit(int value, String shortName) {
        this.value = value;
        this.shortName = shortName;
    }

    public int getValue() {
        return value;
    }

    public String getShortName() {
        return shortName;
    }
}

