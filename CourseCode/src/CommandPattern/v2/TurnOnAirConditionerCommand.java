package CommandPattern.v2;


public class TurnOnAirConditionerCommand implements Command{
    private final AirConditioner ac;

    public TurnOnAirConditionerCommand(AirConditioner ac) {
        this.ac = ac;
    }

    @Override
    public  void execute(){
        ac.turnOn();
    }

    @Override
    public void undo() {
        ac.turnOff();
    }
}
