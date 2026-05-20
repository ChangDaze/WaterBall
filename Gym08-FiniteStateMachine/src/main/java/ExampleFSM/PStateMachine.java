package ExampleFSM;

import FSM.State;
import FSM.StateMachine;
import FSM.Transition;

public class PStateMachine extends StateMachine {
    public PStateMachine(State initialState, Transition[] transitions) {
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
}
