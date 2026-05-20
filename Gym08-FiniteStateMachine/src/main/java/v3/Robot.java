package v3;

import FSMv2.State;
import FSMv2.StateMachine;
import FSMv2.Transition;

import java.util.List;

public class Robot extends StateMachine {
    public Robot(State initialState, List<Transition> transitions) {
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
