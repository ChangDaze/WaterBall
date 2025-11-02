package Skill.OnePunch;

import Battle.Battle;
import Battle.Troop;
import Battle.Unit;
import Skill.Skill;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@SuperBuilder
public class HighHpOnePunch extends OnePunch {
    @Override
    protected boolean match(Unit from, Unit to) {
        return to.getHp() >= 500;
    }

    @Override
    protected void punch(Unit from, Unit to) {
        to.damageReceived(from, to, 300 + from.getFollowUpDamage());
        //from.followUpApply(to);
    }
}
