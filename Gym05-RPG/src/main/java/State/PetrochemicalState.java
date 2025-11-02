package State;

import Battle.Unit;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class PetrochemicalState extends State{
    @Override
    public boolean roundStartEffect() {
        return false;
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
        return "石化";
    }
}
