package fsm;

import events.Event;

/**
 * Contract: actions WRITE, never decide. All decision logic belongs in
 * {@link Guard}s; by the time an action runs, the transition is committed.
 */
@FunctionalInterface
public interface Action {
    Action NONE = e -> {
    };

    void execute(Event e);
}
