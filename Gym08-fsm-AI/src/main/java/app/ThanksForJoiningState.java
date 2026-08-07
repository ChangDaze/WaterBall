package app;

import domain.Community;
import domain.Member;
import fsm.State;

import java.util.Map;

/**
 * KnowledgeKing epilogue: stamps the 20-second exit deadline and announces
 * the winner — over the running broadcast's chat if someone is on air,
 * otherwise by the bot broadcasting the announcement itself.
 */
public class ThanksForJoiningState implements State {
    private final Community community;
    private final Member self;
    private final KingContext kingCtx;

    public ThanksForJoiningState(Community community, Member self, KingContext kingCtx) {
        this.community = community;
        this.self = self;
        this.kingCtx = kingCtx;
    }

    @Override
    public void onEnter() {
        kingCtx.thanksDeadline = community.clock().now().plusSeconds(20);
        String announcement = winnerAnnouncement();
        if (community.broadcast().isOnAir()) {
            community.sendMessage(self, announcement, new String[0]);
        } else {
            community.goBroadcasting(self);
            community.speak(self, announcement);
            community.stopBroadcasting(self);
        }
    }

    private String winnerAnnouncement() {
        int top = kingCtx.scores.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        long topCount = kingCtx.scores.values().stream().filter(s -> s == top).count();
        if (top == 0 || topCount > 1) {
            return "Tie!";
        }
        Member winner = kingCtx.scores.entrySet().stream()
                .filter(entry -> entry.getValue() == top)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
        return "The winner is " + winner.userId();
    }
}
