package Gym04;

public class HeroHeroHandler extends CollisionHandler {

    public HeroHeroHandler(CollisionHandler next) {
        super(next);
    }

    @Override
    protected boolean check(Sprite c1, Sprite c2) {
        return c1 instanceof Hero && c2 instanceof Hero;
    }

    @Override
    protected void handle(Sprite c1, Sprite c2, int c2Index) {
        System.out.println("  [Handler: HeroHero] " + c1 + " collided with " + c2 + " at [" + c2Index + "]. They cannot pass each other (no action).");
    }
}
