package FacadePattern.v1;

import java.util.ArrayList;
import java.util.List;

public class TableStatsPerformer {
    private final List<StatsOperation> statsOperations = new ArrayList<>();

    public void addStatsOperation(StatsOperation statsOperation) {
        statsOperations.add(statsOperation);
    }

    public void print(Table table) {
        StringBuilder stats = new StringBuilder();
        for (StatsOperation operation : statsOperations) {
            stats.append(operation.getName()).append(": ")
                    .append(operation.perform(table)).append("\n");
        }
        System.out.println(stats);
    }
}
