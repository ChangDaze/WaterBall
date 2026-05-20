package v3;

import FSMv2.Trigger;

public class ToKnowledgeKing implements Trigger {
    private Community community;

    public ToKnowledgeKing(Community community) {
        this.community = community;
    }

    @Override
    public boolean guard() {
        return community.getEvent().equals("C1toC2");
    }

    @Override
    public void action() {
        System.out.println("Transitioning from C1 to C2...");
    }
}
