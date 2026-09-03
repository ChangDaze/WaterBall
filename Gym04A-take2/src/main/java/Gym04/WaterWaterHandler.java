package Gym04;

public class WaterWaterHandler extends CollisionHandler {
    public WaterWaterHandler(CollisionHandler next) {
        super(next);
    }

    @Override
    protected boolean check(Sprite c1, Sprite c2) {
        return c1 instanceof Water && c2 instanceof Water;
    }

    @Override
    protected void handle(Sprite c1, Sprite c2, int c2Index) {
        System.out.println("  [Handler: WaterWater] Water meets Water at [" + c2Index + "]. They merge into one wave (no action).");
    }
}
