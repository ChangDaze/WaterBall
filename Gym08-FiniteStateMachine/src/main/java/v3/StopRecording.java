package v3;

import FSMv2.Trigger;

public class StopRecording implements Trigger {
    private Community community;

    public StopRecording(Community community) {
        this.community = community;
    }

    @Override
    public boolean guard() {
        return community.getEvent().equals("C22toC21");
    }

    @Override
    public void action() {
        System.out.println("Transitioning from C22 to C21...");
    }
}
