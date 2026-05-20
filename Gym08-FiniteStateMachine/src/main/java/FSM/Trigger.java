package FSM;

public interface Trigger {
    boolean guard();
     void action();
}
