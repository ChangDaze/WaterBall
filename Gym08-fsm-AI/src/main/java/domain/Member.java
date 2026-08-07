package domain;

/**
 * Identity inside the community. Ids resolve to a Member at the door
 * (Community.findOnlineMember); inside the domain, objects only.
 */
public class Member {
    private final String userId;
    private final boolean isAdmin;
    private final boolean isBot;
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

    /** Chat display prefix: bots render 🤖, humans 💬 N. */
    public String chatPrefix() {
        return isBot ? "🤖" : "💬 " + userId;
    }
}
