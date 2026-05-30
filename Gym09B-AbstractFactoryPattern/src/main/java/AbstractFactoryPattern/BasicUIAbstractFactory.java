package AbstractFactoryPattern;

public class BasicUIAbstractFactory implements UIAbstractFactory{
    @Override
    public Button createButton() {
        return new BasicButton();
    }

    @Override
    public NumberedList createNumberedList() {
        return new BasicNumberedList();
    }

    @Override
    public Text createText() {
        return new BasicText();
    }
}
