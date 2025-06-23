package TemplateMethodPattern.pratice2.v1;

public class SearchEmptyMessageIndex extends SearchTemplate<Integer> {
    private int index = -1;

    @Override
    protected Integer defaultSearchResult() {
        return index;
    }

    @Override
    protected boolean searchEnd(int messageIndex, String[] messages) {
        return messages[messageIndex].isEmpty();
    }

    @Override
    protected Integer updateSearchResult(int messageIndex, String[] messages) {
        if(messages[messageIndex].isEmpty()) {
            index = messageIndex;
        }
        return index;
    }
}
