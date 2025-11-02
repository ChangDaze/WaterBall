package Skill.OnePunch;

import Battle.Battle;
import Battle.Troop;
import Battle.Unit;
import Skill.Skill;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@SuperBuilder
public abstract class OnePunch implements Skill {
    protected final OnePunch next;

    @Override
    public List<Unit> chooseTarget(Unit from) {
        Battle battle = from.getBattle();
        Troop allyTroop = from.getTroop();

        List<Unit> potentialTargets = battle.getTroops().stream()
                .filter(t -> t != allyTroop)                 // 過濾掉自己所在的 troop
                .flatMap(t -> t.getUnits().stream().filter(u -> !u.isDead()))       // 攤平成所有 unit
                .collect(Collectors.toList());

        if(getTargetRequiredNumber() >= potentialTargets.size()){
            return potentialTargets;
        }

        return from.getStrategy().chooseTarget(potentialTargets, getTargetRequiredNumber());
    }

    @Override
    public void apply(Unit from, Unit to) {
        if (match(from, to)) {
            punch(from, to);
        } else if (next != null) {
            next.apply(from, to);
        } else {
            // 沒有符合的 handler，也沒有 next
            System.out.println("No OnePunch rule matched.");
        }
    }

    @Override
    public Integer getNeedHp(Unit from) {
        return 0;
    }

    @Override
    public Integer getNeedMp(Unit from) {
        return 180;
    }

    @Override
    public Integer getTargetRequiredNumber() {
        return 1;
    }

    @Override
    public String getSkillName(){
        return "一拳攻擊";
    }

    protected abstract boolean match(Unit from, Unit to);

    protected abstract void punch(Unit from, Unit to);
}
