package Strategy;

import InputManager.InputManager;
import Battle.Unit;
import Skill.Skill;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuperBuilder
public class HeroStrategy extends RoleStrategy{
    @Override
    public Skill chooseSkill() {
        List<Skill> skills = super.getUnit().getSkills();
        InputManager input = InputManager.getInstance();
        String choice = input.nextLine();
        return skills.get(Integer.parseInt(choice));
    }

    @Override
    public List<Unit> chooseTarget(List<Unit> potentialTargets, Integer requiredNumber) {
        System.out.printf("選擇 %d 位目標: %s\n", requiredNumber,
                IntStream.range(0, potentialTargets.size())
                        .mapToObj(i -> String.format("(%d) %s", i, potentialTargets.get(i).getNameWithTroop()))
                        .collect(Collectors.joining(" ")));

        List<Unit> result = new ArrayList<>();
        InputManager input = InputManager.getInstance();
        String choice = input.nextLine();
        String[] indexes = choice.split(", ");

        for (String index : indexes) {
            result.add(potentialTargets.get(Integer.parseInt(index)));
        }

        return result;
    }
}
