package AdapterPattern.A;

import java.util.*;

public class SuperRelationshipAnalyzer {
    Set<String> friendMap = new HashSet<>();
    public void init(String script)
    {
        String[]connects = script.split("\r\n");
        for(String connect: connects){
            String[] pair = connect.split(" -- ");
            friendMap.add(pair[0]+"-"+pair[1]);
            friendMap.add(pair[1]+"-"+pair[0]);
        }
    }

    public boolean isMutualFriend(String targetName, String name2, String name3)
    {
        return friendMap.contains(targetName+"-"+name2) && friendMap.contains(targetName+"-"+name3);
    }
}
