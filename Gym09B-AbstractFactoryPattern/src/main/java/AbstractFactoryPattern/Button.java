package AbstractFactoryPattern;

public abstract class Button {

    protected abstract char getTopLeftCorner();
    protected abstract char getTopRightCorner();
    protected abstract char getBottomLeftCorner();
    protected abstract char getBottomRightCorner();
    protected abstract char getHorizontalEdge();
    protected abstract char getVerticalEdge();

    public String update(String input, ButtonParam param) {
        String[] lines = input.split("\n");
        int canvasHeight = lines.length;
        int canvasWidth = lines[0].length();
        char[][] canvas = new char[canvasHeight][canvasWidth];

        for (int i = 0; i < canvasHeight; i++) {
            canvas[i] = lines[i].toCharArray();
        }

        int x = param.getX();
        int y = param.getY();
        String text = param.getText();
        int paddingWidth = param.getPaddingWidth();
        int paddingHeight = param.getPaddingHeight();

        int buttonWidth = 1 + paddingWidth + text.length() + paddingWidth + 1;
        int buttonHeight = 1 + paddingHeight + 1 + paddingHeight + 1;
        int textRowIndex = 1 + paddingHeight;

        for (int r = 0; r < buttonHeight; r++) {
            for (int c = 0; c < buttonWidth; c++) {
                int canvasY = y + r;
                int canvasX = x + c;

                if (canvasY >= 0 && canvasY < canvasHeight && canvasX >= 0 && canvasX < canvasWidth) {
                    char fillChar = ' ';

                    /*
                    1. judge corner
                    2. judge HorizontalEdge, VerticalEdge
                    3. judge text
                    */

                    if (r == 0 && c == 0) fillChar = getTopLeftCorner();
                    else if (r == 0 && c == buttonWidth - 1) fillChar = getTopRightCorner();
                    else if (r == buttonHeight - 1 && c == 0) fillChar = getBottomLeftCorner();
                    else if (r == buttonHeight - 1 && c == buttonWidth - 1) fillChar = getBottomRightCorner();
                    else if (r == 0 || r == buttonHeight - 1) fillChar = getHorizontalEdge();
                    else if (c == 0 || c == buttonWidth - 1) fillChar = getVerticalEdge();
                    else if (r == textRowIndex) {
                        int textStartCol = 1 + paddingWidth;
                        int textEndCol = textStartCol + text.length() - 1;
                        if (c >= textStartCol && c <= textEndCol) {
                            fillChar = text.charAt(c - textStartCol);
                        }
                    }

                    canvas[canvasY][canvasX] = fillChar;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < canvasHeight; i++) {
            sb.append(canvas[i]);
            if (i < canvasHeight - 1) sb.append("\n");
        }
        return sb.toString();
    }
}
