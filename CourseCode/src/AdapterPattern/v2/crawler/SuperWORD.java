package AdapterPattern.v2.crawler;

import AdapterPattern.v2.Word;

import java.util.List;
import java.util.Map;

public class SuperWORD {
    public String raw;
    public List<String> definitions;

    public SuperWORD(String raw, List<String> definitions){
        this.raw = raw;
        this.definitions = definitions;
    }
}
