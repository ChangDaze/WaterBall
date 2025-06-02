package MatchmakingSystem.v3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HabitBased implements MatchType {
    @Override
    public List<Individual> Match(List<Individual> members, Individual individual){
        List<Integer> countOrder = new ArrayList<>();
        List<Individual> result = new ArrayList<>();
        Set<String> baseHabitSet = Arrays.stream(individual.getHabits().split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

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

            int insertIndex = 0;
            for(; insertIndex < countOrder.size(); insertIndex++){
                if (
                        count > countOrder.get(insertIndex) ||
                        count == countOrder.get(insertIndex) && member.getId() < result.get(insertIndex).getId()
                ){
                    break;
                }
            }

            result.add(insertIndex, member);
            countOrder.add(insertIndex, count);
        }

        return result;
    }
}
