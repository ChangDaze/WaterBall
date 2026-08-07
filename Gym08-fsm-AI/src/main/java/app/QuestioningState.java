package app;

import domain.Community;
import domain.Member;
import domain.events.NewMessageEvent;
import events.Event;
import fsm.State;

import java.util.Arrays;

/**
 * KnowledgeKing quiz round. Judges A/B/C/D answers, scores members and
 * advances the question index; the transition to Thanks reads
 * {@link #isFinished()} AFTER handle() ran (engine ordering), so the third
 * correct answer flips the guard within the same event.
 */
public class QuestioningState implements State {
    private final Community community;
    private final Member self;
    private final KingContext kingCtx;
    private final QuestionBank questionBank;
    private int index;

    public QuestioningState(Community community, Member self, KingContext kingCtx, QuestionBank questionBank) {
        this.community = community;
        this.self = self;
        this.kingCtx = kingCtx;
        this.questionBank = questionBank;
    }

    @Override
    public void onEnter() {
        kingCtx.scores.clear();
        index = 0;
        kingCtx.questionDeadline = community.clock().now().plusHours(1);
        // A stale thanks deadline from a previous round ("play again") must not
        // let the king→normal timeout transition fire mid-game.
        kingCtx.thanksDeadline = null;
        ask();
    }

    @Override
    public void handle(Event e) {
        if (!(e instanceof NewMessageEvent m) || isFinished()) {
            return;
        }
        // Answers only count when addressed at the bot (proven by knowledge-king.normal-play).
        if (!Arrays.asList(m.message().tags()).contains(self.userId())) {
            return;
        }
        String answer = m.message().content();
        if (!answer.matches("[ABCD]")) {
            return;
        }
        if (answer.equals(questionBank.question(index).correctOption())) {
            kingCtx.scores.merge(m.creator(), 1, Integer::sum);
            community.sendMessage(self, "Congrats! you got the answer!", new String[]{m.creator().userId()});
            index++;
            if (!isFinished()) {
                ask();
            }
        }
        // Wrong answers are silent (KnowledgeKing_IncorrectAnswer.out).
    }

    /** Read by the Questioning→Thanks transition guard. */
    public boolean isFinished() {
        return index >= questionBank.size();
    }

    private void ask() {
        community.sendMessage(self, questionBank.render(index), new String[0]);
    }
}
