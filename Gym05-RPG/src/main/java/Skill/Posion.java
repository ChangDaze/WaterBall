package Skill;

import Battle.Battle;
import Battle.Troop;
import Battle.Unit;
import State.PetrochemicalState;
import State.PoisonedState;

import java.util.List;
import java.util.stream.Collectors;

public class Posion implements Skill{
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
        to.setState(
                PoisonedState.builder()
                .startRound(to.getRound())
                .unit(to)
                .build()
        );
    }

    @Override
    public Integer getNeedHp(Unit from) {
        return 0;
    }

    @Override
    public Integer getNeedMp(Unit from) {
        return 80;
    }

    @Override
    public Integer getTargetRequiredNumber() {
        return 1;
    }

    @Override
    public String getSkillName(){
        return "下毒";
    }
}
