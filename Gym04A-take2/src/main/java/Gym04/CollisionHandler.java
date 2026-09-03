package Gym04;

public abstract class CollisionHandler {
    protected CollisionHandler next;

    public CollisionHandler(CollisionHandler next) {
        this.next = next;
    }

    public void collision(Sprite c1, Sprite c2, int c2Index) {
        if (check(c1, c2)) {
            handle(c1, c2, c2Index);
            return;
        }

        if (next != null) {
            next.collision(c1, c2, c2Index);
        } else {
            System.out.println("  [Collision] No matching handler found for " + c1 + " and " + c2);
        }
    }

    protected abstract boolean check(Sprite c1, Sprite c2);

    protected abstract void handle(Sprite c1, Sprite c2, int c2Index);
}
