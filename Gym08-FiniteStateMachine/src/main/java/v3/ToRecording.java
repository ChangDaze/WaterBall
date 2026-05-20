package v3;

import FSMv2.Trigger;

public class ToRecording implements Trigger {
    private Community community;

    public ToRecording(Community community) {
        this.community = community;
    }

    @Override
    public boolean guard() {
        return community.getEvent().equals("C21toC22");
    }

    @Override
    public void action() {
        System.out.println("Transitioning from C21 to C22...");
    }
}
