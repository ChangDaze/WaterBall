package AdapterPattern.A2;

public class Main {
    public static void main(String[] args) {
        RelationshipAnalyzer analyzer = new SuperRelationshipAnalyzerAdapter();
        RelationshipGraph graph = analyzer.parse("""
                A: B C D\r
                B: A D E\r
                C: A E G K M\r
                D: A B K P\r
                E: B C J K L\r
                F: Z""");
        System.out.println(analyzer.getMutualFriends("A","B"));
        System.out.println(analyzer.getMutualFriends("C","D"));
        System.out.println(analyzer.getMutualFriends("E","F"));
        System.out.println(graph.hasConnection("A","B"));
        System.out.println(graph.hasConnection("Z","A"));
        System.out.println(graph.hasConnection("C","M"));
    }
}
