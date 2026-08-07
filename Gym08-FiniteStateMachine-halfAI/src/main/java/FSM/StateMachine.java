package FSM;

public abstract class StateMachine extends State {
    protected State state;
    protected Transition[] transitions;

    public StateMachine(State initialState, Transition[] transitions) {
        this.state = initialState;
        this.transitions = transitions;
    }

    @Override
    public void execute() {
        extraExecuteAhead();
        this.state.execute();
        extraExecuteAfter();
        transitionCheck();
    }

    @Override
    public void enterState() {
        //no implement
    }

    @Override
    public void exitState() {
        //no implement
    }

    public abstract void extraExecuteAhead();

    public abstract void extraExecuteAfter();

    public void enterState(State newState) {
        if (state != null) {
            state.exitState();
        }
        state = newState;
        state.enterState();
    }

    public void transitionCheck() {
        for (Transition transition : transitions) {
            if (transition.transition()) {
                enterState(transition.getToState());
                break;
            }
        }
    }
}
