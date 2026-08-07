package fsm.composite;

import events.Event;
import fsm.State;
import fsm.StateMachine;

/**
 * Plugin (OCP): deleting this package leaves {@code fsm} compiling.
 *
 * A state that has-a whole inner machine. Exit cascades inside-out
 * (inner stop first), and re-entry RESETS the inner machine because
 * {@link StateMachine#start()} re-resolves its initial state supplier —
 * this is what restarts carousels.
 */
public class CompositeState implements State {
    private final StateMachine inner;

    public CompositeState(StateMachine inner) {
        this.inner = inner;
    }

    @Override
    public void onEnter() {
        inner.start();
    }

    @Override
    public void handle(Event e) {
        inner.handle(e);
    }

    @Override
    public void onExit() {
        inner.stop();
    }
}
