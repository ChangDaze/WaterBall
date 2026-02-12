package CompositePattern.v2;

import java.util.ArrayList;
import java.util.List;

public class Directory extends Item{
    private final List<Item> children = new ArrayList<>();

    //應該只是明確地給出root要建立的提示方法?
    public static Directory root()
    {
        return new Directory("root");
    };

    public Directory(String name){
        super(name);
    }

    public Item getItem(String name) {
        for(Item child : children) {
            if(name.equals(child.getName())){
                return child;
            }
        }
        return null;
    }

    public void addItem(Item item) {
        children.add(item);
        item.setParent(this);
    }

    @Override
    public long bytes() {
        long total = 0;
        for (Item child : children){
            total += child.bytes();
        }
        return total;
    }
}
