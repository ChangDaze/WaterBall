package TemplateMethodPattern.pratice2.v1;

public abstract class SearchTemplate<T> {
    public T search(String[] messages) {
        T result = defaultSearchResult();

        for (int i = 0; i < messages.length; i++) {
            result = updateSearchResult(i, messages);
            System.out.println(messages[i]);
            if (searchEnd(i, messages)){
                break;
            }
        }

        return result;
    }

    protected T defaultSearchResult() {
        return null;
    }

    protected boolean searchEnd(int messageIndex, String[] messages) {
        return false;
    }

    protected abstract T updateSearchResult(int messageIndex, String[] messages);
}
