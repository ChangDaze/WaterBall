package AbstractFactoryPattern;

public class BasicText extends Text {
    @Override
    protected String formatText(String originalText) {
        return originalText;
    }
}
