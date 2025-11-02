package State;

import Battle.Unit;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class CheeredupState extends State{
    @Override
    public boolean roundStartEffect() {
        return true;
    }

    @Override
    public Integer getRoundLimit() {
        return 3;
    }

    @Override
    public void followUpApply(Unit unit) {
        unit.setHp(unit.getHp() - 50);
    }

    @Override
    public String toString() {
        return "受到鼓舞";
    }

    @Override
    public Integer getFollowUpDamage() {
        return 50;
    }
}
