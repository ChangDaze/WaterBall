package v5.fsm;

import org.junit.jupiter.api.Test;
import v5.events.Event;
import v5.fsm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class StateMachineTest {
    static class E1 implements Event {}

    static class E2 implements Event {}

    static class LoggingState implements State{
        final String name;
        final List<String> log;

        LoggingState(String name, List<String> log) {
            this.name = name;
            this.log = log;
        }

        @Override
        public void onEnter() {
            log.add("enter" + name);
        }

        @Override
        public void onExit() {
            log.add("exit" + name);
        }

        @Override
        public void handle(Event e) {
            log.add("handle" + name);
        }
    }

    final List<String> log = new ArrayList<>();
    final LoggingState a = new LoggingState("A", log);
    final LoggingState b = new LoggingState("B", log);

    private StateMachine machineStrartAt(State initial) {
        return new StateMachine(() -> initial);
    }

    @Test
    void start_enters_initial_state(){
        StateMachine m = machineStrartAt(a);
        m.start();
        assertEquals(List.of("enterA"), log);
        assertSame(a, m.currentState());
    }

    @Test
    void handle_invokes_current_states_handle(){
        StateMachine m = machineStrartAt(a);
        m.start();
        log.clear();
        m.handle(new E1());
        assertEquals(List.of("handleA"), log);
    }

    @Test
    void golden_fire_order_handle_exit_action_enter() {
        StateMachine m = machineStrartAt(b);
        m.addTransition(new Transition(b, E2.class, Guard.ALWAYS, e -> log.add("actionB2"), a));
        m.start();
        log.clear();
        m.handle(new E2());
        assertEquals(List.of("handleB", "exitB", "actionB2", "enterA"), log);
        assertSame(a, m.currentState());
    }

    @Test
    void guard_false_means_no_transition(){
        StateMachine m = machineStrartAt(b);
        m.addTransition(new Transition(b, E2.class, e -> false, e -> log.add("actionB2"), a));
        m.start();
        log.clear();
        m.handle(new E2());
        assertEquals(List.of("handleB"), log);
        assertSame(b, m.currentState());
    }

    @Test
    void wrong_event_type_never_reaches_guard() {
        List<Event> guardSaw = new ArrayList<>();
        Guard spy = e -> {
            guardSaw.add(e);
            return true;
        };
        StateMachine m = machineStrartAt(b);
        m.addTransition(new Transition(b, E2.class, spy, Action.NONE, a));
        m.start();
        m.handle(new E1());
        assertTrue(guardSaw.isEmpty(), "guard must not be invoked for a non-matching event type");
        assertSame(b, m.currentState());
    }

    @Test
    void first_matching_transition_wins_and_order_flips_result() {
        LoggingState c = new LoggingState("C", log);
        Supplier<StateMachine> fresh = () -> machineStrartAt(b);

        StateMachine m1 = fresh.get();
        m1.addTransition(new Transition(b, E2.class, Guard.ALWAYS, Action.NONE, a));
        m1.addTransition(new Transition(b, E2.class, Guard.ALWAYS, Action.NONE, c));
        m1.start();
        m1.handle(new E2());
        assertSame(a, m1.currentState());

        StateMachine m2 = fresh.get();
        m2.addTransition(new Transition(b, E2.class, e -> false, Action.NONE, a));
        m2.addTransition(new Transition(b, E2.class, Guard.ALWAYS, Action.NONE, c));
        m2.start();
        m2.handle(new E2());
        assertSame(c, m2.currentState(), "first PASSING transition wins");

        StateMachine m3 = fresh.get();
        m3.addTransition(new Transition(b, E2.class, Guard.ALWAYS, Action.NONE, c));
        m3.addTransition(new Transition(b, E2.class, Guard.ALWAYS, Action.NONE, a));
        m3.start();
        m3.handle(new E2());
        assertSame(c, m3.currentState(), "swapping registration order flips the result");
    }
}
