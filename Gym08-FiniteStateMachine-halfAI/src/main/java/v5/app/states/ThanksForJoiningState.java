package v5.app.states;

import v5.domain.Community;
import v5.domain.Member;
import v5.fsm.State;

import java.util.Map;

/**
 * part of bot's knowledge flow
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
        //after 20 second bot would back to normal state
        kingCtx.thanksDeadline = community.clock().now().plusSeconds(20);
        String announcement = winnerAnnouncement();

        // a little conflict but think fine currently, this is bot for after knowledge king broadcast to meet the community machinist.
        if(community.broadcast().isOnAir()) {
            community.sendMessage(self, announcement, new String[0]);
        } else {
            community.goBroadcasting(self);
            community.speak(self, announcement);
            community.stopBroadcasting(self);
        }
    }

    private String winnerAnnouncement(){
        //bot announce tie or winner.
        int top = kingCtx.scores.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        long topCount = kingCtx.scores.values().stream().filter(s -> s == top).count();
        if(top == 0 || topCount > 1) {
            return "Tie!";
        }
        Member winner = kingCtx.scores.entrySet().stream()
                .filter(entry -> entry.getValue() == top)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
        return  "The winner is " + winner.userId();
    }
}
