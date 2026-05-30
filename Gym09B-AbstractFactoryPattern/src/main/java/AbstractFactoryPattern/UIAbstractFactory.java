package AbstractFactoryPattern;

public interface UIAbstractFactory {
    Button createButton();
    NumberedList createNumberedList();
    Text createText();
}
