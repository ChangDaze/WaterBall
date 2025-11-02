package State;

import Battle.Unit;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NormalState extends State{
    @Override
    public boolean roundStartEffect() {
        return true;
    }

    @Override
    public Integer getRoundLimit() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void followUpApply(Unit unit) {

    }

    @Override
    public String toString() {
        return "正常";
    }
}
