package Gym04;

public class NoCollisionHandler extends CollisionHandler {
    public NoCollisionHandler(CollisionHandler next) {
        super(next);
    }

    @Override
    protected boolean check(Sprite c1, Sprite c2) {
        return c1 != null && c2 == null;
    }

    @Override
    protected void handle(Sprite c1, Sprite c2, int c2Index) {
        System.out.println("  [Handler: NoCollision] Target slot [" + c2Index + "] is empty.");
        c1.moveInWorld(c2Index);
    }
}
