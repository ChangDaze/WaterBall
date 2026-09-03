package Gym04;

public class Main {
    public static void main(String[] args) {
        CollisionHandler collisionHandler = new HeroHeroHandler(
            new HeroFireHandler(
                new HeroWaterHandler(
                    new WaterFireHandler(
                        new WaterWaterHandler(
                            new FireFireHandler(
                                new NoCollisionHandler(
                                    null
                                )
                            )
                        )
                    )
                )
            )
        );

        World world = new World(collisionHandler);
        world.start();
    }
}
