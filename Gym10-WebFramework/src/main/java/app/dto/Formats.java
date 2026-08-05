package app.dto;

import java.util.regex.Pattern;

/**
 * The field rules shared by the request DTOs, in one place so that "email is 4–32 characters and
 * looks like an email" is stated once rather than three times.
 */
final class Formats {

    /** Deliberately simple: one @, a non-empty local part, and a dotted domain. */
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s.]+(\\.[^@\\s.]+)+$");

    private Formats() {
    }

    static boolean isLengthBetween(String value, int min, int max) {
        return value != null && value.length() >= min && value.length() <= max;
    }

    static boolean isEmail(String value) {
        return value != null && EMAIL.matcher(value).matches();
    }
}
