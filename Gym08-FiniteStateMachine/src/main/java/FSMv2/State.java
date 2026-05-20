package FSMv2;

public abstract class State {
    public abstract void execute();
    public abstract void enterState();
    public abstract void exitState();
}
