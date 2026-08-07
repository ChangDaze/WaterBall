package v3;

import FSMv2.Trigger;

public class PlayAgain implements Trigger {
    private Community community;

    public PlayAgain(Community community) {
        this.community = community;
    }

    @Override
    public boolean guard() {
        return community.getEvent().equals("C12toC11");
    }

    @Override
    public void action() {
        System.out.println("Transitioning from C12 to C11");
    }
}
