package v5.fsm;

import v5.events.Event;

/**
 * CompositeState as clean class only use state machine function run as state.
 * state machine encapsulate the flow of state change state and do action.
 */
public class CompositeState implements State {
    private final StateMachine inner;

    public CompositeState(StateMachine inner){
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
