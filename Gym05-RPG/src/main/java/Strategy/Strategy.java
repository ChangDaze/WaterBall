package Strategy;

import Battle.Unit;
import Skill.Skill;

import java.util.List;

public interface Strategy {
    void action();

    List<Unit> chooseTarget(List<Unit> potentialTargets, Integer requiredNumber);
}
