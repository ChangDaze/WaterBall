package v5.app.states;

import v5.domain.Community;
import v5.domain.Member;
import v5.domain.events.NewMessageEvent;
import v5.events.Event;
import v5.fsm.State;

import java.util.Arrays;

/**
 * part of bot's knowledge flow
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

    public boolean isFinished() {
        return index >= questionBank.size();
    }

    private void ask(){
        community.sendMessage(self, questionBank.render(index), new String[0]);
    }

    @Override
    public void onEnter() {
        kingCtx.scores.clear();
        index = 0;
        kingCtx.questionDeadline = community.clock().now().plusHours(1);
        kingCtx.thanksDeadline = null; //defence the king to normal transition fire during the mid-game.
        ask();
    }

    @Override
    public void handle(Event e) {
        //is finish then return
        if(!(e instanceof NewMessageEvent m) || isFinished()){
            return;
        }
        //not tag bot then return
        if(!Arrays.asList(m.message().tags()).contains(self.userId())){
            return;
        }
        //not choose A,B,C,D then return.
        String answer = m.message().content();
        if(!answer.matches("[ABCD]")){
            return;
        }
        //correct answer react
        if(answer.equals(questionBank.question(index).correctOption())){
            //merge insert or update
            //it computes Integer.sum and stores that
            //in the update case: apply(oldValue, value) — old first, then the value you passed
            kingCtx.scores.merge(m.creator(), 1, Integer::sum);
            community.sendMessage(self, "Congrats! you got the answer!", new String[]{m.creator().userId()});
            index++;
            //ask next question then return.
            if(!isFinished()){
                ask();
            }
        }
        // wrong answers are silent.
    }
}
