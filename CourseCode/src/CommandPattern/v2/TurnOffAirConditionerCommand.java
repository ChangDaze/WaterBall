package CommandPattern.v2;

public class TurnOffAirConditionerCommand implements Command{
    private final AirConditioner ac;

    public TurnOffAirConditionerCommand(AirConditioner ac) {
        this.ac = ac;
    }

    @Override
    public  void execute(){
        ac.turnOff();
    }

    @Override
    public void undo() {
        ac.turnOn();
    }
}
