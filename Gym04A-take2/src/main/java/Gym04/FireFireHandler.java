package Gym04;

public class FireFireHandler extends CollisionHandler {
    public FireFireHandler(CollisionHandler next) {
        super(next);
    }

    @Override
    protected boolean check(Sprite c1, Sprite c2) {
        return c1 instanceof Fire && c2 instanceof Fire;
    }

    @Override
    protected void handle(Sprite c1, Sprite c2, int c2Index) {
        System.out.println("  [Handler: FireFire] Fire meets Fire at [" + c2Index + "]. They burn together (no action).");
    }
}
