package AbstractFactoryPattern;

public abstract class NumberedList {
    protected abstract String getPrefix(int index);

    // Attributes moved to method parameters
    public String update(String input, NumberedListParam param) {
        String[] lines = input.split("\n");
        int canvasHeight = lines.length;
        int canvasWidth = lines[0].length();
        char[][] canvas = new char[canvasHeight][canvasWidth];

        for (int i = 0; i < canvasHeight; i++) {
            canvas[i] = lines[i].toCharArray();
        }

        String[] texts = param.getTexts();
        int x = param.getX();
        int y = param.getY();

        for (int row = 0; row < texts.length; row++) {
            /*
            1. build String lineText = getPrefix(row) + " " + texts[row];
            2. calc canvasY, canvasX
            3. canvas[canvasY][canvasX] =  loop lineText;
            */

            String lineText = getPrefix(row) + " " + texts[row];
            int canvasY = y + row;

            if (canvasY >= 0 && canvasY < canvasHeight) {
                for (int col = 0; col < lineText.length(); col++) {
                    int canvasX = x + col;

                    if (canvasX >= 0 && canvasX < canvasWidth) {
                        canvas[canvasY][canvasX] = lineText.charAt(col);
                    }
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
