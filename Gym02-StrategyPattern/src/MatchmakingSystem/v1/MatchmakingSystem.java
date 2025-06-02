package MatchmakingSystem.v1;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class MatchmakingSystem {
    private final Individual[] members;
    private final String matchType;

    public MatchmakingSystem(Individual[] members, String matchType){
        this.members = members;
        this.matchType = matchType;
    }

    public Individual Match(Individual individual){
        Individual matched = null;

        switch (matchType){
            case "Distance-Based":
                double maxDistance = -1;
                for (Individual member : members) {

                    if(individual.getId() == member.getId()){
                        continue;
                    }

                    int[] coordMatch = individual.getCoord();
                    int[] coordMatched = member.getCoord();
                    int dx = coordMatched[0] - coordMatch[0];
                    int dy = coordMatched[1] - coordMatch[1];
                    double distance = Math.sqrt(dx * dx + dy * dy);


                    if ( matched == null || //還沒配對到
                        distance < maxDistance || //有更近的距離
                        (distance == maxDistance && member.getId() < matched.getId()) //相同距離但id較小
                    )
                    {
                        matched = member;
                        maxDistance = distance;
                    }
                }
                break;
            case "Habit-Based":
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
                break;
        }

        return matched;
    }
}
