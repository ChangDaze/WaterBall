package State;

import Battle.Unit;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class State {
    private Integer startRound;
    private Unit unit;

    /**
    依照return true 或 false決定回合是否繼續
     */
    public boolean roundStart() {
        //這是遺留的，之前跑roundStartEffect前還會做其他事但現在拔掉了，所以roundStart = roundStartEffect，之後可以改掉
        //或整合成其他template method
        return roundStartEffect();
    }

    public void checkStateLimit() {
        if(unit.getRound() - startRound >= getRoundLimit()) {
            unit.setState(
                    NormalState.builder()
                            .startRound(unit.getRound())
                            .unit(unit)
                            .build()
            );
        }
    }

    /**
     執行state回合起始效果並依照return true 或 false決定回合是否繼續
     */
    public abstract boolean roundStartEffect();

    public abstract Integer getRoundLimit();

    public abstract void followUpApply(Unit unit);

    @Override
    public abstract String toString();

    public Integer getFollowUpDamage() {
        return 0;
    }
}
