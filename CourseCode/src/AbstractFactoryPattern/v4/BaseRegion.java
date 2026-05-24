package AbstractFactoryPattern.v4;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BaseRegion implements Region {
    private final int number; //Region 辨識代號 number
    private final Floor floor;
    private final Set<Portal> portals = new HashSet<>(4); //一個Region最多4個Portal

    public BaseRegion(int number, Floor floor){
        this.number = number;
        this.floor = floor;
    }

    @Override
    public String getName() {
        return String.format("Region %d", number);
    }

    //進入player Region
    @Override
    public void access(Player player) {
        System.out.printf("Enter %s%n", getName());
        player.setCurrentRegion(this);
        Stage stage = floor.getFactory().createStage(this);
        stage.play(player);
    }

    @Override
    public void addPortal(Portal portal){
        if(hasSpaceForMorePortals()){
            portals.add(portal);
        } else {
            throw new IllegalStateException("The number of portals in a region must not exceed 4.");
        }
    }

    @Override
    public void removePortal(Portal portal){
        portals.remove(portal);
    }

    @Override
    public boolean hasSpaceForMorePortals(){
        return portals.size() < 4;
    }

    @Override
    public Collection<Portal> getPortals(){
        return portals;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public Floor getFloor() {
        return floor;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        BaseRegion that = (BaseRegion) o;
        return number == that.number; //使用number辨別Region
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);//number是唯一辨識代號
    }
}
