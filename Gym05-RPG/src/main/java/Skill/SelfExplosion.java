package Skill;

import Battle.Battle;
import Battle.Troop;
import Battle.Unit;

import java.util.List;
import java.util.stream.Collectors;

public class SelfExplosion implements Skill{
    @Override
    public List<Unit> chooseTarget(Unit from) {
        Battle battle = from.getBattle();

        return battle.getTroops().stream()
                .flatMap(t -> t.getUnits().stream().filter(u -> !u.isDead() && u != from)) // 攤平非自己的所有 unit
                .collect(Collectors.toList());
    }

    @Override
    public void apply(Unit from, Unit to) {
        to.damageReceived(from, to, 150 + from.getFollowUpDamage());
        //from.followUpApply(to);
    }

    @Override
    public Integer getNeedHp(Unit from) {
        return from.getHp();
    }

    @Override
    public Integer getNeedMp(Unit from) {
        return 200;
    }

    @Override
    public Integer getTargetRequiredNumber() {
        return 0;
    }

    @Override
    public String getSkillName(){
        return "自爆";
    }
}
