package v3;

import FSMv2.Trigger;

public class ToInteracting implements Trigger {
    private Community community;

    public ToInteracting(Community community) {
        this.community = community;
    }

    @Override
    public boolean guard() {
        return community.getEvent().equals("C11toC12");
    }

    @Override
    public void action() {
        System.out.println("Transitioning from C11 to C12");
    }
}
