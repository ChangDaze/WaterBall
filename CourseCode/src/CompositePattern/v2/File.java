package CompositePattern.v2;

public class File extends Item{
    private final byte[] content;

    public File(String name, String content){
        super(name);
        this.content = content.getBytes();
    }

    @Override
    public long bytes(){return content.length;}
}
