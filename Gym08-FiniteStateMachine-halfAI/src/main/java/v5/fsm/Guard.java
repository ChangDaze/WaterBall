package v5.fsm;

import v5.events.Event;

/**
 * only offer test method for transition to use.
 * to test do transition or not
 */
@FunctionalInterface
public interface Guard {
    /**
     * the static default use for test method can called by all implementations.
     */
    Guard ALWAYS = e -> true;

    boolean test(Event e);
}
