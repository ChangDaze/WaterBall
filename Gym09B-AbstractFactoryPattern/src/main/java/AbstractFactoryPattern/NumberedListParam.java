package AbstractFactoryPattern;

import java.util.List;

public class NumberedListParam {
    private int x;
    private int y;
    private String[] texts;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String[] getTexts() {
        return texts;
    }

    public NumberedListParam(int x, int y, String[] texts) {
        this.x = x;
        this.y = y;
        this.texts = texts;
    }
}
