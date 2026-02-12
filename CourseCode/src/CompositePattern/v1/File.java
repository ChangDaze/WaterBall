package CompositePattern.v1;

import static CompositePattern.utils.ValidationUtils.shouldMatch;

public class File {
    private Directory parent;
    private final String name;
    private final byte[] content;

    public File(String name, String content){
        this.name = shouldMatch("[A-Za-z0-9.\\-_]+", name);
        this.content = content.getBytes();
    }

    public Directory getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public long bytes(){return content.length;}

    public void setParent(Directory parent) {
        this.parent = parent;
    }
}
