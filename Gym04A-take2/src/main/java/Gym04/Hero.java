package Gym04;

public class Hero extends Sprite {
    private int hp = 30;

    public Hero(int positionIndex, World world) {
        super(positionIndex, world);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        int oldHp = this.hp;
        this.hp = hp;
        System.out.println("  [HP Change] " + this + " HP changed: " + oldHp + " -> " + this.hp);
    }

    @Override
    public String getSymbol() {
        return "H(" + hp + ")";
    }

    @Override
    public String toString() {
        return "Hero(HP:" + hp + ")";
    }
}
