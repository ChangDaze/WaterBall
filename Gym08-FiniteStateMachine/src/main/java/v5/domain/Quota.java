package v5.domain;

/**
 * Quota extend the ability of community, and all quotas calculate together.
 */
public class Quota {
    private int remaining;

    public Quota(int remaining) {
        this.remaining = remaining;
    }

    public boolean canAfford(int cost){
        return remaining >= cost;
    }

    public void consume(int cost){
        remaining -= cost;
    }

    public int remaining(){
        return remaining;
    }
}
