package MatchmakingSystem.v3;

import java.util.ArrayList;
import java.util.List;

public class DistanceBased implements MatchType {
    @Override
    public List<Individual> Match(List<Individual> members, Individual individual){
        List<Double> distances = new ArrayList<>();
        List<Individual> result = new ArrayList<>();
        for (Individual member : members) {
            if(individual.getId() == member.getId()){
                continue;
            }

            int[] coordMatch = individual.getCoord();
            int[] coordMatched = member.getCoord();
            int dx = coordMatched[0] - coordMatch[0];
            int dy = coordMatched[1] - coordMatch[1];
            double distance = Math.sqrt(dx * dx + dy * dy);

            int insertIndex = 0;
            for(; insertIndex < distances.size(); insertIndex++){
                if (
                        distance < distances.get(insertIndex) ||
                        distance == distances.get(insertIndex) && member.getId() < result.get(insertIndex).getId()
                ){
                    break;
                }
            }

            result.add(insertIndex, member);
            distances.add(insertIndex, distance);
        }

        return result;
    }
}
