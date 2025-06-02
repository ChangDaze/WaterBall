package MatchmakingSystem.v3;

import java.util.Arrays;

public class MatchmakingSystem {
    private final Individual[] members;
    private final MatchType matchType;

    public MatchmakingSystem(Individual[] members, MatchType matchType){
        this.members = members;
        this.matchType = matchType;
    }

    public Individual Match(Individual individual){

        return matchType.Match(Arrays.asList(members), individual).getFirst();
    }
}
