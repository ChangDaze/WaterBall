package AbstractFactoryPattern.v3;

public abstract class PortalDecorator implements Portal {
    protected Portal next;

    public PortalDecorator(Portal next) {
        this.next = next;
    }

    //Override沒有額外加東西的用default hook就好
    @Override
    public Portable getTarget(Player player) {
        return next.getTarget(player);
    }

    @Override
    public void destroy() {
        next.destroy();
    }

    @Override
    public int id() {
        return next.id();
    }

    @Override
    public boolean equals(Object obj) {
        return next.equals(obj);
    }

    @Override
    public int hashCode() {
        return next.hashCode();
    }
}
