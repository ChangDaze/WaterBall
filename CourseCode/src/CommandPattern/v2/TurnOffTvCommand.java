package CommandPattern.v2;

public class TurnOffTvCommand implements Command{
    private final Television tv;

    public TurnOffTvCommand(Television tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.turnOff();
    }

    @Override
    public void undo() {
        tv.turnOn();
    }
}
