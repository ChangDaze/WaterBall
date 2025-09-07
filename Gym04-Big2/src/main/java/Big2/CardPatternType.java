package Big2;

public enum CardPatternType {
    PASS(0, "PASS"),
    SINGLE(1, "單張"),
    PAIR(2, "對子"),
    STRAIGHT(3, "順子"),
    FULL_HOUSE(4, "葫蘆");

    private final int value;
    private final String displayName;

    CardPatternType(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }
}

