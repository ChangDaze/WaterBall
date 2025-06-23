package TemplateMethodPattern.pratice2.v0;

public class CountNumberOfWaterballs {
    public int count(String[] messages) {
        int count = 0;
        for (String message : messages) {
            if ("Waterball".equals(message)) {
                count ++;
            }
            System.out.println(message);
        }
        return count;
    }
}
