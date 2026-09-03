package MatchmakingSystem;

public class MatchmakingSystem {
    private Individual[] individuals;
    private Strategy strategy;

    public MatchmakingSystem(Individual[] individuals, Strategy strategy) {
        this.individuals = individuals;
        this.strategy = strategy;
    }

    public Individual match(Individual i) {
        return strategy.match(i, individuals);
    }
}
