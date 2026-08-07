package ExampleFSM;

import FSM.LeafState;

public class C1LeafState1 extends LeafState {
    private EventPlatform eventPlatform;

    public C1LeafState1(EventPlatform eventPlatform) {
        this.eventPlatform = eventPlatform;
    }

    @Override
    public void execute() {
        System.out.println("Event..." + eventPlatform.getEvent());
        System.out.println("C1LeafState1 executing...");
    }

    @Override
    public void enterState() {
        System.out.println("Entering C1LeafState1...");
    }

    @Override
    public void exitState() {
        System.out.println("Exiting C1LeafState1...");
    }
}
