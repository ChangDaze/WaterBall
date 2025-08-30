package CommandPattern.v2;

public class FanPreviousLevelCommand implements Command{
    private final Fan fan;

    public FanPreviousLevelCommand(Fan fan) {
        this.fan = fan;
    }

    @Override
    public void execute(){
        fan.previousLevel();
    }


    @Override
    public void undo() {
        fan.nextLevel();
    }
}
