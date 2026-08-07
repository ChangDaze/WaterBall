package ExampleFSM;

import FSM.LeafState;

public class C1LeafState2 extends LeafState {
    private EventPlatform eventPlatform;

    public C1LeafState2(EventPlatform eventPlatform) {
        this.eventPlatform = eventPlatform;
    }

    @Override
    public void execute() {
        System.out.println("Event..." + eventPlatform.getEvent());
        System.out.println("C1LeafState2 executing...");
    }

    @Override
    public void enterState() {
        System.out.println("Entering C1LeafState2...");
    }

    @Override
    public void exitState() {
        System.out.println("Exiting C1LeafState2...");
    }
}
