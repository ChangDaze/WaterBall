package Skill.OnePunch;

import Battle.Unit;
import State.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class PoisonedPetrochemicalOnePunch extends OnePunch {
    @Override
    protected boolean match(Unit from, Unit to) {
        State targetState = to.getState();
        return targetState instanceof PoisonedState ||
                targetState instanceof PetrochemicalState;
    }

    @Override
    protected void punch(Unit from, Unit to) {
        for (int i = 0; i<3; i++) {
            to.damageReceived(from, to, 80 + from.getFollowUpDamage());
            //from.followUpApply(to);
            if (to.isDead()) {
                break;
            }
        }
    }
}
