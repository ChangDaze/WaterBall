package Gym04;

public class WaterFireHandler extends CollisionHandler {
    public WaterFireHandler(CollisionHandler next) {
        super(next);
    }

    @Override
    protected boolean check(Sprite c1, Sprite c2) {
        return (c1 instanceof Water && c2 instanceof Fire) || (c1 instanceof Fire && c2 instanceof Water);
    }

    @Override
    protected void handle(Sprite c1, Sprite c2, int c2Index) {
        System.out.println("  [Handler: WaterFire] Water meets Fire at [" + c2Index + "]! Both extinguish each other.");
        c1.removeFromWorld();
        c2.removeFromWorld();
    }
}
