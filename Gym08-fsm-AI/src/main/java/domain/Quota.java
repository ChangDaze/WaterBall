package domain;

/**
 * ONE shared pool for the whole community; the 額度 numbers on commands
 * are COSTS per use, drawn from this pool.
 */
public class Quota {
    private int remaining;

    public Quota(int remaining) {
        this.remaining = remaining;
    }

    public boolean canAfford(int cost) {
        return remaining >= cost;
    }

    public void consume(int cost) {
        remaining -= cost;
    }

    public int remaining() {
        return remaining;
    }
}
