package CommandPattern.v1;

public class AirConditioner {
    private boolean on;

    public boolean isOn(){
        return on;
    }

    public void turnOn(){
        this.on = true;
        System.out.println("AC Turned ON.");
    }

    public void turnOff(){
        this.on = false;
        System.out.println("AC Turned OFF.");
    }
}
