package Strategy;

import Battle.Unit;
import Skill.Skill;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
public class AIStrategy extends RoleStrategy{
    private Integer seed = 0;

    @Override
    public Skill chooseSkill() {
        Skill result = super.getUnit().getSkills().get(seed % super.getUnit().getSkills().size());
        seed++;
        return result;
    }

    @Override
    public List<Unit> chooseTarget(List<Unit> potentialTargets, Integer requiredNumber) {
        List<Unit> result = new ArrayList<>();

        for (int i = 0; i < requiredNumber; i++) {
            result.add(potentialTargets.get((seed + i) % potentialTargets.size()));
        }

        seed++;
        return result;
    }
}
