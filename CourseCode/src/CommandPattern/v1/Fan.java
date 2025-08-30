package CommandPattern.v1;

public class Fan {
    private int level;

    public int getLevel() {
        return level;
    }

    public void nextLevel(){
        level = (level + 1) % 4;//單純讓他可以不用歸零
        System.out.println("Fan Level ->" + level);
    }

    public void previousLevel(){
        level = level - 1 == -1 ? 3 : level - 1;
        System.out.println("Fan Level ->" + level);
    }
}
