package AbstractFactoryPattern;

public class Main {
    public static void main(String[] args) {
        // Create button parameters
        ButtonParam button1 = new ButtonParam(3, 1, "Hi, I miss u", 1, 0);
        ButtonParam button2 = new ButtonParam(3, 6, "No", 1, 0);
        ButtonParam button3 = new ButtonParam(12, 6, "Yes", 1, 0);
        ButtonParam[] buttonParams = {button1, button2, button3};

        // Create text parameter (two lines of text)
        TextParam textParam = new TextParam(4, 4, "Do u love me ?");
        TextParam textParam2 = new TextParam(4, 5, "Please tell...");
        TextParam[] textParams = {textParam, textParam2};

        // Create numbered list parameter
        String[] listItems = {"Let's Travel", "Back to home", "Have dinner"};
        NumberedListParam numberedListParam = new NumberedListParam(3, 9, listItems);
        NumberedListParam[] numberedListParams = {numberedListParam};

        // Create UI with BasicUIAbstractFactory
        UIAbstractFactory factory = new BasicUIAbstractFactory();
        UI ui = new UI(13, 22, buttonParams, numberedListParams, textParams, factory);

        // Print the UI
        ui.print();

        // Create UI with BasicUIAbstractFactory
        UIAbstractFactory factory2 = new PrettyUIAbstractFactory();
        UI ui2 = new UI(13, 22, buttonParams, numberedListParams, textParams, factory2);

        // Print the UI
        ui2.print();
    }
}
