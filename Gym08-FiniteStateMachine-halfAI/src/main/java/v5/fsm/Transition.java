package v5.fsm;

import v5.events.Event;

/**
 * record type let you no need to do assign field, declare getter every time, but not offer setter
 */
public record Transition(State from, Class<? extends Event> eventType, Guard guard, Action action, State to)
{
    /**
     * the compact constructor is the record's validation/normalization gate,
     * It's the hook for validation and normalization before the components are stored.
     */
    public Transition
    {
        if(from == null || eventType == null || guard == null || action == null || to == null){
            throw new IllegalArgumentException("Transition fields must not be null - use Guard.ALWAYS / Action.NONE");
        }
    }

    /**
     * check input event match the judge eventType and the state in state machine reuse in memory so direct use ==
     */
    public boolean matches(State current, Event e) {
        return from == current && eventType.isInstance(e);
    }
}
