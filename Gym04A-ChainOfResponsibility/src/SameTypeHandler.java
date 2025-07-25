public class SameTypeHandler extends CollisionHandler {
    public SameTypeHandler(CollisionHandler next) {
        super(next);
    }

    @Override
    protected boolean match(Sprite c1, Sprite c2) {
        return c1.getType().equals(c2.getType());
    }

    @Override
    protected boolean doMove(Sprite c1, Sprite c2) {
        return false;
    }
}
