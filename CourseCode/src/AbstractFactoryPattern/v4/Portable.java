package AbstractFactoryPattern.v4;

public interface Portable {
    String getName();
    void access(Player player);
    default void addPortal(Portal portal){
        // default: do nothing
    }
    default void removePortal(Portal portal){
        // default: do nothing
    }
}
