package ExampleFSM;

import FSM.Trigger;

public class C1toC2Trigger implements Trigger {
    private EventPlatform eventPlatform;

    public C1toC2Trigger(EventPlatform eventPlatform) {
        this.eventPlatform = eventPlatform;
    }

    @Override
    public boolean guard() {
        return eventPlatform.getEvent().equals("C1toC2");
    }

    @Override
    public void action() {
        System.out.println("Transitioning from C1 to C2...");
    }
}
