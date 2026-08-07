package ExampleFSM;

import FSM.LeafState;

public class C2LeafState2 extends LeafState{
    private EventPlatform eventPlatform;

    public C2LeafState2(EventPlatform eventPlatform) {
        this.eventPlatform = eventPlatform;
    }

    @Override
    public void execute() {
        System.out.println("Event..." + eventPlatform.getEvent());
        System.out.println("C2LeafState2 executing...");
    }

    @Override
    public void enterState() {
        System.out.println("Entering C2LeafState2...");
    }

    @Override
    public void exitState() {
        System.out.println("Exiting C2LeafState2...");
    }
}
