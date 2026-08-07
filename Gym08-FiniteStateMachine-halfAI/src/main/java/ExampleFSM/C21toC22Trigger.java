package ExampleFSM;

import FSM.Trigger;

public class C21toC22Trigger implements Trigger {
    private EventPlatform eventPlatform;

    public C21toC22Trigger(EventPlatform eventPlatform) {
        this.eventPlatform = eventPlatform;
    }

    @Override
    public boolean guard() {
        return eventPlatform.getEvent().equals("C21toC22");
    }

    @Override
    public void action() {
        System.out.println("Transitioning from C21 to C22...");
    }
}
