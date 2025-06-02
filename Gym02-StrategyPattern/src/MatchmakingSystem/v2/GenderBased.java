package MatchmakingSystem.v2;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class GenderBased implements MatchType{
    @Override
    public Individual Match(Individual[] members, Individual individual){
        Individual matched = null;

        for (Individual member : members) {
            if(individual.getId() == member.getId()){
                continue;
            }

            if (
                (matched == null && member.getGender() == individual.getGender()) || //要比較，不然不同gender在matched = null時也會被選中
                (member.getGender() == individual.getGender() && member.getId() < matched.getId()) //相同性別但id較小
            )
            {
                matched = member;
            }
        }
        return matched;
    }
}
