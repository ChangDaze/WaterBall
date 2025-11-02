package Skill.OnePunch;

import Battle.Unit;
import State.*;
import State.State;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class CheeredupOnePunch extends OnePunch {
    @Override
    protected boolean match(Unit from, Unit to) {
        State targetState = to.getState();
        return targetState instanceof CheeredupState;
    }

    @Override
    protected void punch(Unit from, Unit to) {
        to.damageReceived(from, to, 100 + from.getFollowUpDamage());
        //from.followUpApply(to);
        to.setState(
            NormalState.builder()
                .startRound(to.getRound())
                .unit(to)
                .build()
        );
    }
}
