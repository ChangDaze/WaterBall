package AbstractFactoryPattern;

public class UI {
    private int height;
    private int width;
    private ButtonParam[] buttonParams;
    private NumberedListParam[] numberedListParams;
    private TextParam[] textParams;
    private UIAbstractFactory uiAbstractFactory;

    private Button button;
    private NumberedList numberedList;
    private Text text;

    public UI(int height, int width, ButtonParam[] buttonParams, NumberedListParam[] numberedListParams, TextParam[] textParams, UIAbstractFactory uiAbstractFactory) {
        this.height = height;
        this.width = width;
        this.buttonParams = buttonParams;
        this.numberedListParams = numberedListParams;
        this.textParams = textParams;
        this.uiAbstractFactory = uiAbstractFactory;

        setController();
    }

    private void setController(){
        button = uiAbstractFactory.createButton();
        numberedList = uiAbstractFactory.createNumberedList();
        text = uiAbstractFactory.createText();
    }

    private String generateBox() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (row == 0 || row == height - 1 || col == 0 || col == width - 1) {
                    sb.append(".");
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void print(){
        String box = generateBox();

        // Loop through and update each button
        for (ButtonParam param : buttonParams) {
            box = button.update(box, param);
        }

        // Loop through and update each numbered list
        for (NumberedListParam param : numberedListParams) {
            box = numberedList.update(box, param);
        }

        // Loop through and update each text
        for (TextParam param : textParams) {
            box = text.update(box, param);
        }

        System.out.println(box);
    }
}
