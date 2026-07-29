package v4.Community.EventHandler;

import v4.Community.Event;

public abstract class EventHandler {
    protected EventHandler next;

    public EventHandler(EventHandler next) {
        this.next = next;
    }

    public boolean handle(Event event){
        if(match(event)) {
            return doHandling(event);
        } else if (next != null) {
            return next.handle(event);
        } else {
            System.out.println("UnSupport event !");
            return false;
        }
    }

    public abstract boolean match(Event event);

    public abstract boolean doHandling(Event event);
}
