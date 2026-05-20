package ExampleFSM;

public class C11toC12Trigger implements FSM.Trigger {
    private EventPlatform eventPlatform;

    public C11toC12Trigger(EventPlatform eventPlatform) {
        this.eventPlatform = eventPlatform;
    }

    @Override
    public boolean guard() {
        return eventPlatform.getEvent().equals("C11toC12");
    }

    @Override
    public void action() {
        System.out.println("Transitioning from C11 to C12");
    }
}
