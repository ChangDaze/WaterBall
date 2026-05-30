package AbstractFactoryPattern;

public class PrettyNumberedList extends NumberedList {
    @Override
    protected String getPrefix(int index) {
        return toRoman(index + 1) + ".";
    }

    private String toRoman(int number) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanLetters = {"m", "cm", "d", "cd", "c", "xc", "l", "xl", "x", "ix", "v", "iv", "i"};
        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                number -= values[i];
                roman.append(romanLetters[i]);
            }
        }
        return roman.toString();
    }
}
