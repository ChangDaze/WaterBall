public class WaterFireHandler extends CollisionHandler {
    public WaterFireHandler(CollisionHandler next) {
        super(next);
    }

    @Override
    protected boolean match(Sprite c1, Sprite c2) {
        return (c1.getType().equals("W") && c2.getType().equals("F")) || (c1.getType().equals("F") && c2.getType().equals("W"));
    }

    @Override
    protected boolean doMove(Sprite c1, Sprite c2) {
        c1.setHp(0);
        c2.setHp(0);

        System.out.println("Water 從世界中被移除");
        System.out.println("Fire 從世界中被移除");

        return false;
    }
}
