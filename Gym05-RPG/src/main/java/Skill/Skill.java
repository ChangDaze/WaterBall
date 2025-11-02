package Skill;

import Battle.Unit;

import java.util.List;

public interface Skill {
    List<Unit> chooseTarget(Unit from);

    void apply(Unit from, Unit to);

    Integer getNeedHp(Unit from);

    Integer getNeedMp(Unit from);

    Integer getTargetRequiredNumber();

    String getSkillName();
}
