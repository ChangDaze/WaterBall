package AbstractFactoryPattern.v3;

import java.util.List;
import java.util.Objects;

//傳送門
public class BasePortal implements Portal{
    private static int count = 0; //總傳送門數
    private final int id; //傳送門唯一編號
    private final Portable p1; //Portal p1 端
    private final Portable p2; //Portal p2 端
    private int life = 3; //剩餘使用次數

    //連結兩個Portable (Floor or Region)
    public static BasePortal makePortal(Portable p1, Portable p2)
    {
        System.out.printf("[New portal connects %s <-> %s]", p1.getName(), p2.getName());
        return new BasePortal(p1, p2);
    }

    //constructor
    public BasePortal(Portable p1, Portable p2) {
        this.id = count++;
        this.p1 = p1;
        this.p2 = p2;
        p1.addPortal(this);
        p2.addPortal(this);
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    //使用傳送門
    public void access(Player player) {
        Portable target = getTarget(player);

        //移動到另一端Portable並觸發access
        target.access(player);
        //次數到上限摧毀
        if(--life <= 0) {
            destroy();
        }
    }

    @Override
    //確認Player在傳送們哪端決定傳送門會將Player送去哪
    public Portable getTarget(Player player) {
        if(player.isAt(p1)) {
            return p2;
        } else if(player.isAt(p2)) {
            return p1;
        } else {
            throw new IllegalStateException("The player is not in one of the region that can access this portal.");
        }
    }

    @Override
    //破壞傳送門
    public void destroy(){
        System.out.printf("[Portal (%s) is destroyed]%n", this);

        //清除關聯讓Portal 被 GC
        p1.removePortal(this);
        p2.removePortal(this);
    }

    //傳送門代名詞
    @Override
    public String toString() {
        return String.format("%s <-> %s", p1.getName(), p2.getName());
    }

    //有轉換就使用id來確認Portal
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        BasePortal that = (BasePortal) o;
        return id == that.id;
    }

    //傳送門唯一編號來產生hash
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
