package app.domain;

/**
 * A bearer token and the member it identifies.
 */
public final class Token {

    private final String value;
    private final int userId;

    public Token(String value, int userId) {
        this.value = value;
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public int getUserId() {
        return userId;
    }
}
