package bot;

import events.Event;
import events.EventListener;
import fsm.StateMachine;

/**
 * Template Method: the self-filter is MANDATORY because the community
 * notifies listeners on the bot's own replies too (re-entrant). Without
 * the filter, a reply would trigger a reply — an event storm.
 *
 * The base class cannot name domain event types, so the filter is a hook.
 */
public abstract class Bot implements EventListener {
    protected final StateMachine machine;

    protected Bot(StateMachine machine) {
        this.machine = machine;
    }

    public final void onEvent(Event e) {
        if (isMyOwn(e)) {
            return;
        }
        machine.handle(e);
    }

    /** Hook: is this event something I authored myself? */
    protected abstract boolean isMyOwn(Event e);

    public void start() {
        machine.start();
    }
}
