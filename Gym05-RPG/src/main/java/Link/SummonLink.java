package Link;

import Battle.Unit;
import lombok.Builder;

@Builder
public class SummonLink implements Link{
    private final Unit linkSource;

    @Override
    public void linkUpdate() {
        if(!linkSource.isDead()) {
            linkSource.setHp(linkSource.getHp() + 30);
        }
    }

    @Override
    public Unit getLinkSource() {
        return linkSource;
    }
}
