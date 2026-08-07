package fsm;

import events.Event;

/**
 * One row of a transition table.
 * Contract: no nulls — use {@link Guard#ALWAYS} / {@link Action#NONE}.
 */
public record Transition(State from, Class<? extends Event> eventType, Guard guard, Action action, State to) {
    public Transition {
        if (from == null || eventType == null || guard == null || action == null || to == null) {
            throw new IllegalArgumentException("Transition fields must not be null — use Guard.ALWAYS / Action.NONE");
        }
    }

    /**
     * Structural match only (source state + event type); the guard is
     * evaluated separately so wrong-typed events never reach it.
     */
    public boolean matches(State current, Event e) {
        return from == current && eventType.isInstance(e);
    }
}
