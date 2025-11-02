package Skill;

import Battle.*;

import java.util.List;
import java.util.stream.Collectors;

public class Fireball implements Skill{
    @Override
    public List<Unit> chooseTarget(Unit from) {
        Battle battle = from.getBattle();
        Troop allyTroop = from.getTroop();

        return battle.getTroops().stream()
                .filter(t -> t != allyTroop)                 // 過濾掉自己所在的 troop
                .flatMap(t -> t.getUnits().stream().filter(u -> !u.isDead()))       // 攤平成所有 unit
                .collect(Collectors.toList());
    }

    @Override
    public void apply(Unit from, Unit to) {
        to.damageReceived(from, to, 50 + from.getFollowUpDamage());
        //from.followUpApply(to);
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
        return "火球";
    }
}
