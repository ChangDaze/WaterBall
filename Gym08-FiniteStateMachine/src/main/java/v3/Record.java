package v3;

import FSMv2.State;
import FSMv2.StateMachine;
import FSMv2.Transition;

import java.util.List;

public class Record extends StateMachine {
    public Record(State initialState, List<Transition> transitions) {
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
