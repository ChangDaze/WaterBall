package ExampleFSM;

import FSM.State;
import FSM.StateMachine;
import FSM.Transition;

public class C2StateMachine extends StateMachine {
    public C2StateMachine(State initialState, Transition[] transitions) {
        super(initialState, transitions);
    }


    @Override
    public void extraExecuteAhead() {
        //no implement
    }

    @Override
    public void extraExecuteAfter() {
        //no implement
    }

    @Override
    public void enterState() {
        System.out.println("Entering C2StateMachine...");
    }

    @Override
    public void exitState() {
        System.out.println("Exiting C2StateMachine...");
    }
}
