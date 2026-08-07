package fsm.composite;

import events.Event;
import fsm.Action;
import fsm.Guard;
import fsm.State;
import fsm.StateMachine;
import fsm.Transition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Items 7–11 of the fsm test checklist (DESIGN.md §3).
 */
class CompositeStateTest {

    static class E1 implements Event {
    }

    static class E2 implements Event {
    }

    static class E3 implements Event {
    }

    final List<String> log = new ArrayList<>();

    class Named implements State {
        final String name;

        Named(String name) {
            this.name = name;
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

    /** Composite whose own boundary logs too, so cascades are observable. */
    class NamedComposite extends CompositeState {
        final String name;

        NamedComposite(String name, StateMachine inner) {
            super(inner);
            this.name = name;
        }

        @Override
        public void onEnter() {
            log.add("enter" + name);
            super.onEnter();
        }

        @Override
        public void onExit() {
            super.onExit();
            log.add("exit" + name);
        }
    }

    @Test
    void events_reach_inner_current_state() {
        Named inner = new Named("Inner");
        StateMachine innerMachine = new StateMachine(() -> inner);
        CompositeState composite = new CompositeState(innerMachine);

        StateMachine root = new StateMachine(() -> composite);
        root.start();
        log.clear();
        root.handle(new E1());
        assertEquals(List.of("handleInner"), log);
    }

    @Test
    void exit_cascades_inside_out() {
        Named inner = new Named("Inner");
        NamedComposite composite = new NamedComposite("Composite", new StateMachine(() -> inner));

        StateMachine root = new StateMachine(() -> composite);
        root.start();
        log.clear();
        root.stop();
        assertEquals(List.of("exitInner", "exitComposite"), log);
    }

    @Test
    void reentry_resets_inner_and_supplier_is_reevaluated_each_start() {
        AtomicInteger supplierCalls = new AtomicInteger();
        Named inner1 = new Named("Inner1");
        Named inner2 = new Named("Inner2");
        StateMachine innerMachine = new StateMachine(
                () -> supplierCalls.incrementAndGet() % 2 == 1 ? inner1 : inner2);
        CompositeState composite = new CompositeState(innerMachine);
        Named other = new Named("Other");

        StateMachine root = new StateMachine(() -> composite);
        root.addTransition(new Transition(composite, E1.class, Guard.ALWAYS, Action.NONE, other));
        root.addTransition(new Transition(other, E2.class, Guard.ALWAYS, Action.NONE, composite));

        root.start();
        assertEquals(1, supplierCalls.get());
        assertSame(inner1, innerMachine.currentState());

        root.handle(new E1());   // leave composite → inner stops
        assertNull(innerMachine.currentState(), "inner machine must be stopped when composite exits");

        root.handle(new E2());   // re-enter composite → inner restarts, supplier re-resolved
        assertEquals(2, supplierCalls.get());
        assertSame(inner2, innerMachine.currentState(), "re-entry must reset via a fresh supplier resolution");
    }

    @Test
    void depth_three_nesting_works() {
        Named leaf = new Named("Leaf");
        NamedComposite level2 = new NamedComposite("L2", new StateMachine(() -> leaf));
        NamedComposite level1 = new NamedComposite("L1", new StateMachine(() -> level2));

        StateMachine root = new StateMachine(() -> level1);
        root.start();
        assertEquals(List.of("enterL1", "enterL2", "enterLeaf"), log);

        log.clear();
        root.handle(new E1());
        assertEquals(List.of("handleLeaf"), log, "events must tunnel to the deepest current state");

        log.clear();
        root.stop();
        assertEquals(List.of("exitLeaf", "exitL2", "exitL1"), log, "exit must cascade inside-out");
    }

    /**
     * Spec example: composite B (inner b1→b2 on E2, action b2) sitting next to
     * plain A; root goes B→A on E3. A single E2 while in B must produce the
     * inner reaction + inner transition; E3 must cascade B's exit then enter A.
     */
    @Test
    void spec_example_a_b_e1_e2_e3_ordering() {
        Named a = new Named("A");
        Named b1 = new Named("B1");
        Named b2 = new Named("B2");

        StateMachine innerB = new StateMachine(() -> b1);
        innerB.addTransition(new Transition(b1, E2.class, Guard.ALWAYS, e -> log.add("actionB2"), b2));
        NamedComposite b = new NamedComposite("B", innerB);

        StateMachine root = new StateMachine(() -> b);
        root.addTransition(new Transition(b, E3.class, Guard.ALWAYS, e -> log.add("actionA"), a));

        root.start();
        assertEquals(List.of("enterB", "enterB1"), log);

        log.clear();
        root.handle(new E1());   // nobody cares about E1 → inner state reacts, nothing else
        assertEquals(List.of("handleB1"), log);

        log.clear();
        root.handle(new E2());   // inner transition: handle → exit → action → enter (all inside B)
        assertEquals(List.of("handleB1", "exitB1", "actionB2", "enterB2"), log);

        log.clear();
        root.handle(new E3());   // root transition: inner handle first, then B's exit cascades
        assertEquals(List.of("handleB2", "exitB2", "exitB", "actionA", "enterA"), log);
        assertSame(a, root.currentState());
    }
}
