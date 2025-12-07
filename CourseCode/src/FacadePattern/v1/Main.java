package FacadePattern.v1;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        MarkdownParser parser = new MarkdownParser();
        Table table = parser.parseTableFromFile("src/FacadePattern/data.table");
        TableStatsPerformer statsPerformer = new TableStatsPerformer();
        statsPerformer.addStatsOperation(new TotalColumn("Cost"));
        statsPerformer.addStatsOperation(new CountRows("Members"));
        statsPerformer.print(table);
    }
}
