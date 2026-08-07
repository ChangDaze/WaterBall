package ExampleFSM;

import FSM.Trigger;

public class C2toC1Trigger implements Trigger {
    private EventPlatform eventPlatform;

    public C2toC1Trigger(EventPlatform eventPlatform) {
        this.eventPlatform = eventPlatform;
    }

    @Override
    public boolean guard() {
        return eventPlatform.getEvent().equals("C2toC1");
    }

    @Override
    public void action() {
        System.out.println("Transitioning from C2 to C1...");
    }
}
