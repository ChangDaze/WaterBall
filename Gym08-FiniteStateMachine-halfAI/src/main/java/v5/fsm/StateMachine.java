package v5.fsm;

import v5.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * base on handle event, state machine can change state and do action by state and transitions.
 */
public class StateMachine {
    private final Supplier<State> initialState;
    private final List<Transition> transitions = new ArrayList<>();
    private State current;

    public StateMachine(Supplier<State> initialState){
        this.initialState = initialState;
    }

    public void addTransition(Transition t){
        transitions.add(t);
    }

    public void start(){
        //Supplier can help judgement which state would be get when get() be called
        //let the initialState can be lazy get from the conditions at the moment state machine start()
        //but not at the moment create state machine
        //ex: conditions : current online member counts decide which initial state should be
        current = initialState.get();
        current.onEnter();
    }

    public void stop(){
        if (current == null){
            return;
        }

        current.onExit();
        current = null;
    }

    public void handle(Event e) {
        if(current == null) {
            return;
        }
        current.handle(e);
        for(Transition t:transitions){
            //match transition condition then test the guard
            if(t.matches(current, e) && t.guard().test(e)){
                //pass the guard then do action
                current.onExit();
                t.action().execute(e);
                current = t.to();
                current.onEnter();
                return;
            }
        }
    }

    public State currentState(){
        return current;
    }
}