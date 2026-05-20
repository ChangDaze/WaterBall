package v3;

import FSMv2.LeafState;

public class DefaultConversation extends LeafState {
    private Community community;

    public DefaultConversation(Community community) {
        this.community = community;
    }

    @Override
    public void execute() {
        System.out.println("Event..." + community.getEvent());
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
