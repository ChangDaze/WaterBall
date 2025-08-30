package CommandPattern.v2;

public class TurnOnTvCommand implements Command{
    private final Television tv;

    public TurnOnTvCommand(Television tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.turnOn();
    }

    @Override
    public void undo() {
        tv.turnOff();
    }
}
