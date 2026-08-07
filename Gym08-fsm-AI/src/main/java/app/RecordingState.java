package app;

import domain.Community;
import domain.Member;
import domain.events.SpeakEvent;
import events.Event;
import fsm.State;

/**
 * Buffers everything spoken on air. The replay flushes in onExit ON PURPOSE:
 * exiting happens both when the broadcast stops (inner transition to Waiting)
 * and when record mode itself ends (composite exit cascade), so one flush
 * point covers both paths.
 */
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
        buffer.setLength(0);
    }

    @Override
    public void handle(Event e) {
        if (e instanceof SpeakEvent s) {
            if (!buffer.isEmpty()) {
                buffer.append('\n');
            }
            buffer.append(s.speak().content());
        }
    }

    @Override
    public void onExit() {
        if (buffer.isEmpty()) {
            return;
        }
        community.sendMessage(self, "[Record Replay] " + buffer,
                new String[]{recordCtx.recorder.userId()});
        buffer.setLength(0);
    }
}
