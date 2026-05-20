package ExampleFSM;

import FSM.*;

public class Main {
    public static void main(String[] args) {
        EventPlatform eventPlatform = new EventPlatform();
        eventPlatform.setEvent("init");
        //use PStateMachine with C1StateMachine and C2StateMachine as states and with transitions carry C1toC2Trigger and C2toC1Trigger
        //use C1StateMachine with C1LeafState1 and C1LeafState2 as states and with transitions carry C11toC12Trigger and C12toC11Trigger
        //use C2StateMachine with C2LeafState1 and C2LeafState2 as states and with transitions carry C21toC22Trigger and C22toC21Trigger

        // Build leaf states for C1
        C1LeafState1 c11 = new C1LeafState1(eventPlatform);
        C1LeafState2 c12 = new C1LeafState2(eventPlatform);

        // Transitions inside C1
        Transition[] c1Transitions = new Transition[]{
                new FSM.Transition(c12, new C11toC12Trigger(eventPlatform)),
                new FSM.Transition(c11, new C12toC11Trigger(eventPlatform))
        };

        // Create C1 state machine
        C1StateMachine c1 = new C1StateMachine(c11, c1Transitions);

        // Build leaf states for C2
        C2LeafState1 c21 = new C2LeafState1(eventPlatform);
        C2LeafState2 c22 = new C2LeafState2(eventPlatform);

        // Transitions inside C2
        Transition[] c2Transitions = new Transition[]{
                new FSM.Transition(c22, new C21toC22Trigger(eventPlatform)),
                new FSM.Transition(c21, new C22toC21Trigger(eventPlatform))
        };

        // Create C2 state machine
        C2StateMachine c2 = new C2StateMachine(c21, c2Transitions);

        // Transitions inside P
        Transition[] pTransitions = new Transition[]{
                new Transition(c2, new C1toC2Trigger(eventPlatform)),
                new Transition(c1, new C2toC1Trigger(eventPlatform))
        };

        // Create P state machine
        PStateMachine p = new PStateMachine(c1, pTransitions);

        // A simple sequence of events to demonstrate transitions
        String[] events = new String[]{
                "init",
                "C11toC12",
                "C1toC2",
                "C21toC22",
                "C22toC21",
                "C2toC1",
                "C12toC11"
        };

        for (String e : events) {
            System.out.println("\n---- Setting event: " + e + " ----");
            eventPlatform.setEvent(e);
            p.execute();
        }
    }
}
