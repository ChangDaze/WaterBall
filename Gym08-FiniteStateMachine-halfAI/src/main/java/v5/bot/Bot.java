package v5.bot;

import v5.events.Event;
import v5.events.EventListener;
import v5.fsm.StateMachine;

/**
 * implement the bot observer and define bot use state machine to handle event
 * It offers the interface to bound the domain layer and app layer as observer via event package and fsm package.
 */
public abstract class Bot implements EventListener {
    protected final StateMachine machine;

    /**
     * use protected only because abstract class only be called constructor by subclass, use public sometime misleading.
     */
    protected Bot(StateMachine machine) {
        this.machine = machine;
    }

    /**
     * bot should ignore the event cause by itself, so subclass should implement this method to check if the event is caused by bot itself.
     */
    protected abstract boolean isMyOwn(Event e);

    public void start(){
        machine.start();
    }

    public final void onEvent(Event e){
        if(isMyOwn(e)){
            return;
        }
        machine.handle(e);
    }
}
