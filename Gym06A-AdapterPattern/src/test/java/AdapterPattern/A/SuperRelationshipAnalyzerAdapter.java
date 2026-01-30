package AdapterPattern.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperRelationshipAnalyzerAdapter implements RelationshipAnalyzer{
    private final Map<String, String[]> friendList = new HashMap<>();
    private final SuperRelationshipAnalyzer analyzer = new SuperRelationshipAnalyzer();

    @Override
    public void parse(String script)
    {
        String[] lists = script.split("\r\n");
        for(String list: lists){
            String[] pair = list.split(": ");
            friendList.put(pair[0], pair[1].split(" "));
        }

        StringBuilder convertInput = new StringBuilder();
        for (Map.Entry<String, String[]> list : friendList.entrySet()) {
            String[] values = list.getValue();
            for (String name : values){
                convertInput.append(list.getKey()).append(" -- ").append(name).append(System.lineSeparator());
            }
        }

        analyzer.init(convertInput.toString());
    }

    @Override
    public List<String> getMutualFriends(String name1, String name2)
    {
        String[] list = friendList.getOrDefault(name1, new String[0]); //共同朋友一定是其中一位的朋友，所以loop其中一位即可
        List<String> result = new ArrayList<>();
        for (String name : list){
            if(analyzer.isMutualFriend(name, name1, name2)){
                result.add(name);
            }
        }
        return result;
    }
}
