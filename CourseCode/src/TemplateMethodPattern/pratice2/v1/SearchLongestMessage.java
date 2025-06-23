package TemplateMethodPattern.pratice2.v1;

public class SearchLongestMessage extends SearchTemplate<String> {
    private String longestMessage = "";

    @Override
    protected String updateSearchResult(int messageIndex, String[] messages) {
        if(messages[messageIndex].length() > longestMessage.length()) {
            longestMessage = messages[messageIndex];
        }
        return longestMessage;
    }
}
