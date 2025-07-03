package Showdown;

public enum Suit {
    CLUBS(1,"梅花"),
    DIAMONDS(2,"菱形"),
    HEARTS(3,"愛心"),
    SPADES(4,"黑桃");

    private final int value;
    private final String description;

    // 建構子（必須為 private 或預設）
    Suit(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public  int getValue() {
        return value;
    }
}
