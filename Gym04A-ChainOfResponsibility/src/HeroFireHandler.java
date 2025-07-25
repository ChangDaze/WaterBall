public class HeroFireHandler extends CollisionHandler {
    public HeroFireHandler(CollisionHandler next) {
        super(next);
    }

    @Override
    protected boolean match(Sprite c1, Sprite c2) {
        return (c1.getType().equals("H") && c2.getType().equals("F")) || (c1.getType().equals("F") && c2.getType().equals("H"));
    }

    @Override
    protected boolean doMove(Sprite c1, Sprite c2) {
        boolean moveResult = false;
        if (c1.getType().equals("H")) {
            c1.setHp(c1.getHp() - 10);
            c2.setHp(0);
            moveResult = true;
        } else {
            c1.setHp(0);
            c2.setHp(c1.getHp() - 10);
        }

        System.out.println("Hero 生命值減少 10 滴");
        System.out.println("Fire 從世界中被移除");

        return moveResult;
    }
}
