package AbstractFactoryPattern.v4;

import java.util.Collection;

//interface 間可以extend!
public interface Region extends Portable {
    String getName();

    void addPortal(Portal portal);

    void removePortal(Portal portal);

    boolean hasSpaceForMorePortals();

    Collection<Portal> getPortals();

    int getNumber();

    Floor getFloor();
}
