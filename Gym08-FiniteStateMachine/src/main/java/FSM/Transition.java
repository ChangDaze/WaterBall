package FSM;

public class Transition {
    private State toState;
    private Trigger trigger;

    public Transition(State toState, Trigger trigger) {
        this.toState = toState;
        this.trigger = trigger;
    }

    public boolean transition() {
        if (trigger.guard()) {
            trigger.action();
            return true;
        }
        return false;
    }

    public State getToState() {
        return toState;
    }
}
