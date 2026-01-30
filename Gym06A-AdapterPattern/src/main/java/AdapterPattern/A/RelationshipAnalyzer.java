package AdapterPattern.A;

import java.util.List;

public interface RelationshipAnalyzer {
    void parse(String script);
    List<String> getMutualFriends(String name1, String name2);
}
