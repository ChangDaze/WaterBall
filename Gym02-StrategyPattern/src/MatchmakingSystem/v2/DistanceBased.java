package MatchmakingSystem.v2;

public class DistanceBased implements MatchType{
    @Override
    public Individual Match(Individual[] members, Individual individual){
        Individual matched = null;
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
        return matched;
    }
}
