package FacadePattern.v2;

import static java.lang.String.format;

public class TotalColumn implements StatsOperation {
    private final String fieldName;

    public TotalColumn(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getName(){
        return format("Total %s", fieldName);
    }

    @Override
    public Object perform(Table table) {
        return table.getColumn(fieldName)
                .stream().mapToDouble(Double::parseDouble)
                .sum();
    }
}
