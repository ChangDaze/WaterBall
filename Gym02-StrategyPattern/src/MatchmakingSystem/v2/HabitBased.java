package MatchmakingSystem.v2;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class HabitBased implements MatchType{
    @Override
    public Individual Match(Individual[] members, Individual individual){
        Individual matched = null;
        Set<String> baseHabitSet = Arrays.stream(individual.getHabits().split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
        int maxCount = 0;
        for (Individual member : members) {
            if(individual.getId() == member.getId()){
                continue;
            }

            Set<String> memberHabitSet = Arrays.stream(member.getHabits().split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());
            int count = 0 ;

            for (String habit : memberHabitSet) {
                if (baseHabitSet.contains(habit)){
                    count++;
                }
            }

            if ( matched == null || //還沒配對到
                    count > maxCount || //有更多相同的興趣
                    (count == maxCount && member.getId() < matched.getId()) //相同數量的興趣但id較小
            )
            {
                matched = member;
                maxCount = count;
            }
        }
        return matched;
    }
}
