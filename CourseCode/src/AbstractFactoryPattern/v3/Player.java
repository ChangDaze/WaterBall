package AbstractFactoryPattern.v3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class Player {
    private final Scanner in = new Scanner(System.in);
    private boolean alive = true;
    private Floor currentFloor;
    private Region currentRegion;

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    public Region getCurrentRegion() {
        return currentRegion;
    }

    public void setCurrentFloor(Floor floor) {
        this.currentFloor = floor;
    }

    public void setCurrentRegion(Region Region) {
        this.currentRegion = Region;
    }

    public boolean isAt(Portable portable) {
        //決定比較Floor 還 Region，再決定用目前player的Floor 還 Region比較
        if(portable instanceof Region) {
            return currentRegion.equals(portable);
        } else if(portable instanceof Floor) {
            return currentFloor.equals(portable);
        }
        throw new IllegalArgumentException("Unexpected type");
    }

    public void loseGame(){
        alive = false;
    }

    public boolean isAlive(){
        return alive;
    }

    public Portal selectPortal(Collection<Portal> portals){
        List<Portal> portalList = new ArrayList<>(portals);//用前端傳入所在區域有的Portal
        //把傳入的portal list 對應 player 通 portal 去到的位置 列成 輸出文字選項
        System.out.printf("Select a portal (You're at %s): %n%s%n",
                currentRegion.getName(),
                IntStream.range(0, portalList.size())
                        .mapToObj(i -> format("(%d) --> %s", i , portalList.get(i).getTarget(this).getName()))
                        .collect(joining("\n")));
        return portalList.get(in.nextInt());//讓 player 透過文字選項選擇portal
    }

    //player輸入解答
    public String answer(){
        return in.next();
    }
}
