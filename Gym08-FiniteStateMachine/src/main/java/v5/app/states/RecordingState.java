package v5.app.states;

import v5.domain.Community;
import v5.domain.Member;
import v5.domain.events.SpeakEvent;
import v5.events.Event;
import v5.fsm.State;

public class RecordingState implements State {
    private final Community community;
    private final Member self;
    private final RecordContext recordCtx;
    private final StringBuilder buffer = new StringBuilder();

    public RecordingState(Community community, Member self, RecordContext recordCtx) {
        this.community = community;
        this.self = self;
        this.recordCtx = recordCtx;
    }

    @Override
    public void onEnter() {
        //buffer.setLength(0) truncates the StringBuilder to zero characters — it becomes empty,
        buffer.setLength(0);
    }

    @Override
    public void handle(Event e) {
        //algorithm to structure the replay content.
        if(e instanceof SpeakEvent s) {
            if(!buffer.isEmpty()){
                buffer.append('\n');
            }
            buffer.append(s.speak().content());
        }
    }

    @Override
    public void onExit() {
        //bot end recording and record replay
        if (buffer.isEmpty()){
            return;
        }
        community.sendMessage(self, "[Record Replay] " + buffer,
                new String[]{recordCtx.recorder.userId()});
        buffer.setLength(0);
    }
}
