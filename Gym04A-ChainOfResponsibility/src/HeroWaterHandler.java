public class HeroWaterHandler extends CollisionHandler  {
    public HeroWaterHandler(CollisionHandler next) {
        super(next);
    }

    @Override
    protected boolean match(Sprite c1, Sprite c2) {
        return (c1.getType().equals("H") && c2.getType().equals("W")) || (c1.getType().equals("W") && c2.getType().equals("H"));
    }

    @Override
    protected boolean doMove(Sprite c1, Sprite c2) {
        boolean moveResult = false;
        if (c1.getType().equals("H")) {
            c1.setHp(c1.getHp() + 10);
            c2.setHp(0);
            moveResult = true;
        } else {
            c1.setHp(0);
            c2.setHp(c1.getHp() + 10);
        }

        System.out.println("Hero 生命值增加 10 滴");
        System.out.println("Water 從世界中被移除");

        return moveResult;
    }
}
