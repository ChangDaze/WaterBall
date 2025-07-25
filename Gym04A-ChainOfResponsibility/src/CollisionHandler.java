public abstract class CollisionHandler {
    protected final CollisionHandler next;

    public CollisionHandler(CollisionHandler next) {
        this.next = next;
    }

    public boolean collision(Sprite c1, Sprite c2) {
        if(match(c1, c2)) {
            return doMove(c1, c2);
        } else if (next != null) {
            return next.collision(c1, c2);
        }
        return false;
    }

    protected abstract boolean match(Sprite c1, Sprite c2);

    protected abstract boolean doMove(Sprite c1, Sprite c2);
}
