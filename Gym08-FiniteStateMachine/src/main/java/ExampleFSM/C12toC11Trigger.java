package ExampleFSM;

import FSM.Trigger;

public class C12toC11Trigger implements Trigger {
    private EventPlatform eventPlatform;

    public C12toC11Trigger(EventPlatform eventPlatform) {
        this.eventPlatform = eventPlatform;
    }

    @Override
    public boolean guard() {
        return eventPlatform.getEvent().equals("C12toC11");
    }

    @Override
    public void action() {
        System.out.println("Transitioning from C12 to C11");
    }
}
