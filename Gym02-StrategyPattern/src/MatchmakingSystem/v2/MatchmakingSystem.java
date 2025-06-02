package MatchmakingSystem.v2;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class MatchmakingSystem {
    private final Individual[] members;
    private final MatchType matchType;

    public MatchmakingSystem(Individual[] members, MatchType matchType){
        this.members = members;
        this.matchType = matchType;
    }

    public Individual Match(Individual individual){
        return matchType.Match(members, individual);
    }
}
