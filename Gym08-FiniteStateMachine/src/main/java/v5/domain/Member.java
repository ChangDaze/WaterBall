package v5.domain;

public class Member {
    private final String userId;
    private final boolean isAdmin;
    private final boolean isBot; //use on like chatPrefix
    private final int loginOrder;

    public Member(String userId, boolean isAdmin, boolean isBot, int loginOrder) {
        this.userId = userId;
        this.isAdmin = isAdmin;
        this.isBot = isBot;
        this.loginOrder = loginOrder;
    }

    public String userId() {
        return userId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isBot() {
        return isBot;
    }

    public int loginOrder() {
        return loginOrder;
    }

    /**
     * fixed prefix in chat message.
     */
    public String chatPrefix() {
        return isBot ? "🤖" : ("💬 " + userId);
    }
}
