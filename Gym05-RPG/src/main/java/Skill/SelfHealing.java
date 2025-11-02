package Skill;

import Battle.Battle;
import Battle.Troop;
import Battle.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelfHealing implements Skill{
    @Override
    public List<Unit> chooseTarget(Unit from) {
        return new ArrayList<>(List.of(from));
    }

    @Override
    public void apply(Unit from, Unit to) {
        from.setHp(to.getHp() + 150);
    }

    @Override
    public Integer getNeedHp(Unit from) {
        return 0;
    }

    @Override
    public Integer getNeedMp(Unit from) {
        return 50;
    }

    @Override
    public Integer getTargetRequiredNumber() {
        return 0;
    }

    @Override
    public String getSkillName(){
        return "自我治療";
    }
}
