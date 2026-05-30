package AbstractFactoryPattern;

public class PrettyText extends Text {
    @Override
    protected String formatText(String originalText) {
        return originalText.toUpperCase();
    }
}
