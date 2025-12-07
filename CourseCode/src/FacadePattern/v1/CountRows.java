package FacadePattern.v1;

import static java.lang.String.format;

public class CountRows implements StatsOperation{
    private final String rowName;

    public CountRows(String statsName)
    {
        this.rowName = statsName;
    }

    @Override
    public String getName() {
        return format("Count %s", rowName);
    }

    @Override
    public Object perform(Table table) {
        return table.getData().size();
    }
}
