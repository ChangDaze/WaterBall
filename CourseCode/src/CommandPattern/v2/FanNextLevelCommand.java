package CommandPattern.v2;

public class FanNextLevelCommand implements Command{
    private final Fan fan;

    public FanNextLevelCommand(Fan fan) {
        this.fan = fan;
    }

    @Override
    public void execute(){
        fan.nextLevel();
    }

    @Override
    public void undo() {
        fan.previousLevel();
    }
}
