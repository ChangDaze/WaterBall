package CommandPattern.v2;

public class Television {
    private boolean on;

    public void turnOn() {
        this.on = true;
        System.out.println("TV Turned ON.");
    }

    public void turnOff() {
        this.on = false;
        System.out.println("TV Turned OFF.");
    }
}
