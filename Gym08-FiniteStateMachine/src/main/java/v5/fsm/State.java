package v5.fsm;

import v5.events.Event;

public interface State {
    default void onEnter() {
        // Default implementation does nothing
    }
    default void onExit() {
        // Default implementation does nothing
    }
    default void handle(Event e){
        // Default implementation does nothing
    }
}
