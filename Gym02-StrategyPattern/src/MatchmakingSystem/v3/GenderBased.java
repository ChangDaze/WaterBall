package MatchmakingSystem.v3;

import java.util.ArrayList;
import java.util.List;

public class GenderBased implements MatchType {
    @Override
    public List<Individual> Match(List<Individual> members, Individual individual){
        List<Boolean> genderCheck = new ArrayList<>();
        List<Individual> result = new ArrayList<>();

        for (Individual member : members) {
            if(individual.getId() == member.getId()){
                continue;
            }

            boolean check = member.getGender() == individual.getGender();

            int insertIndex = 0;
            for(; insertIndex < result.size(); insertIndex++){
                if (
                        (check && !genderCheck.get(insertIndex)) || //比較優先
                        (!check && !genderCheck.get(insertIndex) && member.getId() < result.get(insertIndex).getId()) || //同樣不優先比ID
                        (check && genderCheck.get(insertIndex) && member.getId() < result.get(insertIndex).getId()) //同樣優先比ID
                ){
                    break;
                }

                //確認不優先就繼續比 continue
            }

            result.add(insertIndex, member);
            genderCheck.add(insertIndex, check);
        }
        return result;
    }
}
