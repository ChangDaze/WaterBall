package AdapterPattern.A2;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;

public class GraphAdapter implements RelationshipGraph{
    private final Graph<String, DefaultEdge> friendGraph;

    public GraphAdapter(Graph<String, DefaultEdge> friendGraph){
        this.friendGraph = friendGraph;
    }

    @Override
    public boolean hasConnection(String name1, String name2){
        // 判斷兩點是否連通
        ConnectivityInspector<String, DefaultEdge> inspector = new ConnectivityInspector<>(friendGraph);
        return inspector.pathExists(name1, name2);
    }
}
