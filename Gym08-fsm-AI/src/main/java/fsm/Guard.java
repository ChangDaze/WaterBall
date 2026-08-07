package fsm;

import events.Event;

/**
 * Contract: guards READ, never write. They may be evaluated speculatively
 * during the transition scan, so a guard with side effects is a bug.
 */
@FunctionalInterface
public interface Guard {
    Guard ALWAYS = e -> true;

    boolean test(Event e);
}
