package Gym04;

public class Sprite {
    private int positionIndex;
    private World world;

    public Sprite(int positionIndex, World world) {
        this.positionIndex = positionIndex;
        this.world = world;
    }

    public void moveInWorld(int index) {
        int oldIndex = this.positionIndex;
        world.getMaps()[this.positionIndex] = null;
        world.getMaps()[index] = this;
        this.positionIndex = index;
        System.out.println("  [Move] " + this + " moved from position [" + oldIndex + "] -> [" + index + "]");
    }

    public void removeFromWorld() {
        int oldIndex = this.positionIndex;
        world.getMaps()[positionIndex] = null;
        System.out.println("  [Remove] " + this + " at position [" + oldIndex + "] was removed from the world");
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public String getSymbol() {
        return "S";
    }

    @Override
    public String toString() {
        return "Sprite";
    }
}
