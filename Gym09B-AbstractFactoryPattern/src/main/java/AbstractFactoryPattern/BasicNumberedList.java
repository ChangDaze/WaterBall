package AbstractFactoryPattern;

public class BasicNumberedList extends NumberedList {
    @Override
    protected String getPrefix(int index) {
        return (index + 1) + ".";
    }
}
