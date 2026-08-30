package MatchmakingSystem;

public interface Strategy {
    public Individual match(Individual i, Individual[] s);
}
