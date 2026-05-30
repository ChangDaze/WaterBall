package AbstractFactoryPattern;

public abstract class Text {

    // Pass the original string in, let the subclass decide how to format it
    protected abstract String formatText(String originalText);

    // Attributes moved to method parameters
    public String update(String input, TextParam param) {
        String[] lines = input.split("\n");
        int canvasHeight = lines.length;
        int canvasWidth = lines[0].length();
        char[][] canvas = new char[canvasHeight][canvasWidth];

        for (int i = 0; i < canvasHeight; i++) {
            canvas[i] = lines[i].toCharArray();
        }

        String text = param.getText();
        int x = param.getX();
        int y = param.getY();
        /*
        1. build String displayText = formatText(text);
        2. calc canvasY, canvasX
        3. canvas[canvasY][canvasX] =  loop displayText;
        */

        // Use the subclass's formatting logic
        String displayText = formatText(text);

        if (y >= 0 && y < canvasHeight) {
            for (int col = 0; col < displayText.length(); col++) {
                int canvasX = x + col;

                if (canvasX >= 0 && canvasX < canvasWidth) {
                    canvas[y][canvasX] = displayText.charAt(col);
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
