package MatchmakingSystem.v3;

import java.util.Collections;
import java.util.List;

public class ReverseDecorator implements MatchType {
    private final MatchType matchType;

    public ReverseDecorator(MatchType matchType) {
        this.matchType = matchType;
    }
    @Override
    public List<Individual> Match(List<Individual> members, Individual individual) {
        List<Individual> result = matchType.Match(members, individual);
        Collections.reverse(result); // 反轉排序結果
        return result;
    }
}
