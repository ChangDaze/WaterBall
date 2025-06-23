package TemplateMethodPattern.pratice2.v1;

public class CountNumberOfWaterballs extends SearchTemplate<Integer>{
    private int count = 0;

    @Override
    protected Integer defaultSearchResult() {
        return 0;
    }

    @Override
    protected Integer updateSearchResult(int messageIndex, String[] messages) {
        if("Waterball".equals(messages[messageIndex])) {
            count++;
        }
        return count;
    }
}
