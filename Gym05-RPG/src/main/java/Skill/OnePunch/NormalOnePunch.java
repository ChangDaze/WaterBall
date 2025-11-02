package Skill.OnePunch;

import Battle.Unit;
import State.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NormalOnePunch extends OnePunch {
    @Override
    protected boolean match(Unit from, Unit to) {
        State targetState = to.getState();
        return targetState instanceof NormalState;
    }

    @Override
    protected void punch(Unit from, Unit to) {
        to.damageReceived(from, to, 100 + from.getFollowUpDamage());
        //from.followUpApply(to);
    }
}
