package AbstractFactoryPattern.v3;

import AbstractFactoryPattern.v3.factories.PortalFactory;
import AbstractFactoryPattern.v3.factories.RegionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.min;
import static java.util.Collections.shuffle;

public class Floor implements Portable {
    private final static Random RANDOM = new Random();

    protected PortalFactory portalFactory;
    protected RegionFactory regionFactory;

    protected final Floor nextFloor;
    protected final int maxRegions;//region 上限
    protected final List<Region> regions = new ArrayList<>();

    protected final Region initialRegion;
    protected final Game game;
    protected final String name;

    public Floor(Game game, String name, Floor nextFloor, RegionFactory regionFactory, PortalFactory portalFactory) {
        this(game, name, nextFloor, 5, regionFactory, portalFactory);
    }

    public Floor(Game game, String name, Floor nextFloor, int maxRegions, RegionFactory regionFactory, PortalFactory portalFactory) {
        this.game = game;
        this.name = name;
        this.nextFloor = nextFloor;
        this.regionFactory = regionFactory;
        this.portalFactory = portalFactory;
        //spawnNewRegion會用到factory所以要比spawnNewRegion先注入
        this.initialRegion = spawnNewRegion();//Floor初始Region由Floor觸發生成
        if(maxRegions == 0) {
            throw new IllegalStateException("#maxRegions cannot less than 0");
        }
        this.maxRegions = maxRegions;
    }

    @Override
    public void access(Player player){
        System.out.printf("Enter %s%n", this.name);
        //player進入floor
        player.setCurrentFloor(this);
        //player進入floor初始地區，進入region開始stage
        initialRegion.access(player);
        //還沒遊戲結束
        while (!game.isGameOver() && player.isAt(this))
        {
            System.out.printf("==== [%s] %s ====%n", name, player.getCurrentRegion().getName());
            if(player.isAlive()){
                //隨機發生三件事之一
                handlePostStageEvents(player);
                //player選擇下一步
                Portal portal = player.selectPortal(player.getCurrentRegion().getPortals());
                //繼續access下個區域
                portal.access(player);
            }else {
                game.over();
            }
        }
    }

    private void handlePostStageEvents(Player player){
        //floor Portal已滿就要隨機破壞 (不算在三個隨機事件中)
        if(!hasSpaceForMorePortal()){
            randomlyDestroyonePortal();
        }

        //只有一個region時只能觸發1和2
        //region達到上限時只能觸發0和2
        //其他情況0,1,2都能觸發
        int choice = regions.size() == 1 ? (RANDOM.nextBoolean() ? 1 : 2)
                : maxRegions == regions.size() ? (RANDOM.nextBoolean() ? 0 : 2)
                : RANDOM.nextInt(3);

        switch (choice){
            //生成一道傳送門，隨機連接此floor兩個地區(sublist size = 2)
            case 0 -> makePortal(randomlyPickRegionsHaveSpaceForPortals(2));
            //生成一個新地區，生成一個傳送門連接玩家目前地區和新地區
            case 1 -> makePortal(player.getCurrentRegion(), spawnNewRegion());
            //隨機在一地區中，生成通往下個floor的傳送門
            case 2 -> makePortal(randomlyPickRegionsHaveSpaceForPortals(1).get(0), nextFloor);
        }

    }

    private Portal makePortal(Portable p1, Portable p2) {
        return portalFactory.createPortal(p1, p2);
    }

    private Portal makePortal(List<? extends Portable> twoPortables) {
        return portalFactory.createPortal(twoPortables.get(0), twoPortables.get(1));
    }

    private void randomlyDestroyonePortal(){
        //把這層Floor所有Region的Portals扁平化成一個List
        List<Portal> portals = regions.stream()
                .flatMap(r -> r.getPortals().stream()).toList();
        //隨機選出一個portal讓portal被GC
        Portal portal = portals.get(RANDOM.nextInt(portals.size()));
        portal.destroy();
    }

    //查看這層floor的regions是否還放得下新portal
    private boolean hasSpaceForMorePortal(){
        return regions.stream().anyMatch(Region::hasSpaceForMorePortals);
    }

    private List<Region> randomlyPickRegionsHaveSpaceForPortals(int regions){
        //取出還能放portal的region
        List<Region> temp = new ArrayList<>(
                this.regions.stream()
                    .filter(Region::hasSpaceForMorePortals).toList());
        //洗亂順序
        shuffle(temp);
        //取出input要求數量的sublist
        return temp.subList(0, min(temp.size(), regions));
    }

    private Region spawnNewRegion(){
        //region 在 floor底下所以number也是同floor底下才唯一
        Region newRegion = regionFactory.createRegion(regions.size(),this);
        regions.add(newRegion);
        System.out.printf("Floor %s New region: %s%n", name, newRegion.getName());
        return newRegion;
    }

    @Override
    public String getName() {
        return name;
    }
}
