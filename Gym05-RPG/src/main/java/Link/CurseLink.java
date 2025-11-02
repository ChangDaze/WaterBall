package Link;

import Battle.Unit;
import lombok.Builder;

@Builder
public class CurseLink implements Link{
    private final Unit linkSource;
    private final Unit linkDestination;

    @Override
    public void linkUpdate() {
        if(!linkSource.isDead()) {
            linkSource.setHp(linkSource.getHp() + linkDestination.getMp());
        }
    }

    @Override
    public Unit getLinkSource() {
        return linkSource;
    }
}
