package ExampleFSM;

import FSM.Trigger;

public class C22toC21Trigger implements Trigger {
    private EventPlatform eventPlatform;

    public C22toC21Trigger(EventPlatform eventPlatform) {
        this.eventPlatform = eventPlatform;
    }

    @Override
    public boolean guard() {
        return eventPlatform.getEvent().equals("C22toC21");
    }

    @Override
    public void action() {
        System.out.println("Transitioning from C22 to C21...");
    }
}
