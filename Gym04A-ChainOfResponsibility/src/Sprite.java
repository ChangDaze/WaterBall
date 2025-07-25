public class Sprite {
    private String type;
    private int hp = 30;

    public Sprite(String type) {
        this.type = type;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHp() {
        return hp;
    }

    public String getType() {
        return type;
    }
}
