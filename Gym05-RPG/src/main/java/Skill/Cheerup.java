package Skill;

import Battle.Battle;
import Battle.Troop;
import Battle.Unit;
import State.CheeredupState;
import State.PoisonedState;

import java.util.List;
import java.util.stream.Collectors;

public class Cheerup implements Skill{
    @Override
    public List<Unit> chooseTarget(Unit from) {
        Battle battle = from.getBattle();
        Troop allyTroop = from.getTroop();

        List<Unit> potentialTargets = battle.getTroops().stream()
                .filter(t -> t == allyTroop)                 // 只對自己所在的 troop 使用
                .flatMap(t -> t.getUnits().stream().filter(u -> !u.isDead() && u != from))  //攤平非自己的友軍 unit
                .collect(Collectors.toList());

        if(getTargetRequiredNumber() >= potentialTargets.size()){
            return potentialTargets;
        }

        return from.getStrategy().chooseTarget(potentialTargets, getTargetRequiredNumber());
    }

    @Override
    public void apply(Unit from, Unit to) {
        to.setState(
                CheeredupState.builder()
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
        return 100;
    }

    @Override
    public Integer getTargetRequiredNumber() {
        return 3;
    }

    @Override
    public String getSkillName(){
        return "鼓舞";
    }
}
