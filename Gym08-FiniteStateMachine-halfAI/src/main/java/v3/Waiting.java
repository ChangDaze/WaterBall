package v3;

import FSMv2.LeafState;

public class Waiting extends LeafState {
    private Community community;

    public Waiting(Community community) {
        this.community = community;
    }

    @Override
    public void execute() {
        System.out.println("Event..." + community.getEvent());
        System.out.println("C2LeafState1 executing...");
    }

    @Override
    public void enterState() {
        System.out.println("Entering C2LeafState1...");
    }

    @Override
    public void exitState() {
        System.out.println("Exiting C2LeafState1...");
    }
}
