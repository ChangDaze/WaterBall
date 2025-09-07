package Big2;

public enum Rank {
    THREE(0, "3", 3),
    FOUR(1, "4", 4),
    FIVE(2, "5", 5),
    SIX(3, "6", 6),
    SEVEN(4, "7", 7),
    EIGHT(5, "8", 8),
    NINE(6, "9", 9),
    TEN(7, "10", 10),
    JACK(8, "J", 11),
    QUEEN(9, "Q", 12),
    KING(10, "K", 13),
    ACE(11, "A", 1),
    TWO(12, "2", 2);

    private final int value;
    private final String displayName;
    private final int faceValue;  // 檯面點數

    Rank(int value, String displayName, int faceValue) {
        this.value = value;
        this.displayName = displayName;
        this.faceValue =faceValue;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }
}

