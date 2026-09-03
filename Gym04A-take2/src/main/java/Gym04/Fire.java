package Gym04;

public class Fire extends Sprite {
    public Fire(int positionIndex, World world) {
        super(positionIndex, world);
    }

    @Override
    public String getSymbol() {
        return "F";
    }

    @Override
    public String toString() {
        return "Fire";
    }
}
