package MatchmakingSystem;

public class Main {
    public static void main(String[] args) {
        Individual[] individuals = new Individual[] {
                new Individual(
                        1,
                        Gender.MALE,
                        24,
                        "Loves outdoor activities and photography.",
                        new String[] { "Photography", "Hiking", "Cycling" },
                        12,
                        45
                ),
                new Individual(
                        2,
                        Gender.FEMALE,
                        29,
                        "Coffee enthusiast and tech blogger.",
                        new String[] { "Reading", "Blogging", "Coffee Brewing" },
                        88,
                        15
                ),
                new Individual(
                        3,
                        Gender.FEMALE,
                        22,
                        "Passionate about indie game development and pixel art.",
                        new String[] { "Gaming", "Drawing", "Music" },
                        34,
                        76
                )
        };

        Individual individual = new Individual(
                4,
                Gender.FEMALE,
                22,
                "Passionate about indie game development and pixel art.",
                new String[] { "Gaming", "Drawing", "Music" },
                88,
                15
        );

        Strategy strategy = new DistanceBased();
        MatchmakingSystem matchmakingSystem = new MatchmakingSystem(individuals, strategy);

        System.out.println(matchmakingSystem.match(individual));
    }
}