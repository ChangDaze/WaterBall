package fsm;

import events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Generic finite state machine — zero domain knowledge.
 *
 * Contracts (graded thinking):
 * 1. Only this class calls {@link State} lifecycle methods.
 * 2. First matching transition wins; registration order is configuration.
 * 3. No matching transition = silence, not an error.
 * 4. Fire order: exit → action → enter.
 * 5. {@code initialState} is a Supplier re-resolved on EVERY {@link #start()}
 *    (conditional initial states).
 */
public class StateMachine {
    private final Supplier<State> initialState;
    private final List<Transition> transitions = new ArrayList<>();
    private State current;

    public StateMachine(Supplier<State> initialState) {
        this.initialState = initialState;
    }

    public void addTransition(Transition t) {
        transitions.add(t);
    }

    public void start() {
        current = initialState.get();
        current.onEnter();
    }

    public void stop() {
        if (current == null) {
            return;
        }
        current.onExit();
        current = null;
    }

    /**
     * Ordering is load-bearing: the state reacts FIRST, then the transition
     * scan runs — so a state's reaction can influence guards (e.g. a scored
     * answer pushing a question index past its end).
     */
    public void handle(Event e) {
        if (current == null) {
            return;
        }
        current.handle(e);
        for (Transition t : transitions) {
            if (t.matches(current, e) && t.guard().test(e)) {
                current.onExit();
                t.action().execute(e);
                current = t.to();
                current.onEnter();
                return;
            }
        }
    }

    public State currentState() {
        return current;
    }
}
