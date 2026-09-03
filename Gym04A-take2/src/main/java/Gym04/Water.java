package Gym04;

public class Water extends Sprite {
    public Water(int positionIndex, World world) {
        super(positionIndex, world);
    }

    @Override
    public String getSymbol() {
        return "W";
    }

    @Override
    public String toString() {
        return "Water";
    }
}
