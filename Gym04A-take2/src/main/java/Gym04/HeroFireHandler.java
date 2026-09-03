package Gym04;

public class HeroFireHandler extends CollisionHandler {
    public HeroFireHandler(CollisionHandler next) {
        super(next);
    }

    @Override
    protected boolean check(Sprite c1, Sprite c2) {
        return (c1 instanceof Hero && c2 instanceof Fire) || (c1 instanceof Fire && c2 instanceof Hero);
    }

    @Override
    protected void handle(Sprite c1, Sprite c2, int c2Index) {
        System.out.println("  [Handler: HeroFire] Hero meets Fire at [" + c2Index + "]! Hero takes 10 damage, Fire is extinguished.");
        if (c1 instanceof Hero) {
            Hero hero = (Hero) c1;
            hero.setHp(hero.getHp() - 10);
            c2.removeFromWorld();

            if (hero.getHp() <= 0) {
                System.out.println("  [Death] " + hero + " has run out of HP and died!");
                hero.removeFromWorld();
            } else {
                hero.moveInWorld(c2Index);
            }
        } else {
            Hero hero = (Hero) c2;
            hero.setHp(hero.getHp() - 10);
            c1.removeFromWorld();

            if (hero.getHp() <= 0) {
                System.out.println("  [Death] " + hero + " has run out of HP and died!");
                hero.removeFromWorld();
            }
        }
    }
}
