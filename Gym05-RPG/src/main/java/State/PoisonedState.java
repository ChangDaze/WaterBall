package State;

import Battle.Unit;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class PoisonedState extends State{
    @Override
    public boolean roundStartEffect() {
        super.getUnit().damageReceived(30);
        return !super.getUnit().isDead();
    }

    @Override
    public Integer getRoundLimit() {
        return 3;
    }

    @Override
    public void followUpApply(Unit unit) {

    }

    @Override
    public String toString() {
        return "中毒";
    }
}
