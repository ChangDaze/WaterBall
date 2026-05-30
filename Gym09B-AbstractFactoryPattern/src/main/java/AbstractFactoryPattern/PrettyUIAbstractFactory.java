package AbstractFactoryPattern;

public class PrettyUIAbstractFactory implements UIAbstractFactory{
    @Override
    public Button createButton() {
        return new PrettyButton();
    }

    @Override
    public NumberedList createNumberedList() {
        return new PrettyNumberedList();
    }

    @Override
    public Text createText() {
        return new PrettyText();
    }
}
