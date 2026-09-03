package Gym04;

public class HeroWaterHandler extends CollisionHandler {

    public HeroWaterHandler(CollisionHandler next) {
        super(next);
    }

    @Override
    protected boolean check(Sprite c1, Sprite c2) {
        return (c1 instanceof Hero && c2 instanceof Water) || (c1 instanceof Water && c2 instanceof Hero);
    }

    @Override
    protected void handle(Sprite c1, Sprite c2, int c2Index) {
        System.out.println("  [Handler: HeroWater] Hero meets Water at [" + c2Index + "]! Hero gains +10 HP, Water is consumed.");
        if (c1 instanceof Hero) {
            Hero hero = (Hero) c1;
            c2.removeFromWorld();
            hero.setHp(hero.getHp() + 10);
            hero.moveInWorld(c2Index);
        } else {
            Hero hero = (Hero) c2;
            c1.removeFromWorld();
            hero.setHp(hero.getHp() + 10);
        }
    }
}
