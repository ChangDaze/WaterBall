package AbstractFactoryPattern.v3;

public interface Portal {
    int id();
    void access(Player player);
    Portable getTarget(Player player);
    void destroy();
}
