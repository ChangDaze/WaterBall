package v3;

import FSMv2.Trigger;

public class ToRecord implements Trigger {
    private Community community;

    public ToRecord(Community community) {
        this.community = community;
    }

    @Override
    public boolean guard() {
        return community.getEvent().equals("C2toC1");
    }

    @Override
    public void action() {
        System.out.println("Transitioning from C2 to C1...");
    }
}
