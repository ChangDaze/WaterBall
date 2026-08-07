package fsm;

import events.Event;

/**
 * Contract: only {@link StateMachine} ever calls these lifecycle methods.
 * Client code talks to the machine, never to a state directly.
 */
public interface State {
    default void onEnter() {
    }

    default void onExit() {
    }

    default void handle(Event e) {
    }
}
