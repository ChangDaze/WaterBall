package CompositePattern.v2;

import static CompositePattern.utils.ValidationUtils.shouldMatch;

public abstract class Item {
    protected final String name;
    protected Directory parent;

    public Item(String name) {this.name = shouldMatch("[A-Za-z0-9.\\-_]+", name);}

    public abstract long bytes();

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Directory getParent() {
        return parent;
    }
}
