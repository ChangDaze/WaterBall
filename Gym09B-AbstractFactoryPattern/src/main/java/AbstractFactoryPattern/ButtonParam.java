package AbstractFactoryPattern;

public class ButtonParam {
    private int x;
    private int y;
    private String text;
    private int paddingWidth;
    private int paddingHeight;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return text;
    }

    public int getPaddingWidth() {
        return paddingWidth;
    }

    public int getPaddingHeight() {
        return paddingHeight;
    }

    public ButtonParam(int x, int y, String text, int paddingWidth, int paddingHeight) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.paddingWidth = paddingWidth;
        this.paddingHeight = paddingHeight;
    }
}
