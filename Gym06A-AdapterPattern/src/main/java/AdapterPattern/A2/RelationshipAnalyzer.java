package AdapterPattern.A2;

import java.util.List;

public interface RelationshipAnalyzer {
    RelationshipGraph parse(String script);
    List<String> getMutualFriends(String name1, String name2);
}
