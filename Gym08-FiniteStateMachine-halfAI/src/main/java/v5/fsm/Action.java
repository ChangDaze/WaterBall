package v5.fsm;

import v5.events.Event;

/**
 * only offer execute method for transition to use.
 * to execute action when do transition
 */
@FunctionalInterface
public interface Action {
    /**
     * the static default use for execute method can called by all implementations.
     */
    Action NONE = e -> {};
    void execute(Event e);
}
