package CompositePattern.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    public static String shouldMatch(String regax, String content) {
        if(!Pattern.matches(regax, content)) {
            throw new IllegalArgumentException(String.format("Content '%s' doesn't match '%s'", content, regax));
        }
        return content;
    }
}
