package MatchmakingSystem.v3;

import java.util.List;

public interface MatchType {
    /**
     * 返回List，index越小越匹配
     */
    List<Individual> Match(List<Individual> members, Individual individual);
}
