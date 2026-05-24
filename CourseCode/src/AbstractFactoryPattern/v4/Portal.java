package AbstractFactoryPattern.v4;

public interface Portal {
    int id();
    void access(Player player);
    Portable getTarget(Player player);
    void destroy();
}
