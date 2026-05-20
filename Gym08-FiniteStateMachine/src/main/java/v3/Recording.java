package v3;

import FSMv2.LeafState;

public class Recording extends LeafState{
    private Community community;

    public Recording(Community community) {
        this.community = community;
    }

    @Override
    public void execute() {
        System.out.println("Event..." + community.getEvent());
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
