package FacadePattern.v2;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        StatsFacade statsFacade = new StatsFacade();
        statsFacade.printMarkdownTableStats("src/FacadePattern/data.table", StatsFacade.TOTAL_COST | StatsFacade.COUNT_MEMBERS);
    }
}
